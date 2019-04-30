package com.wxsoft.fcare.ui.launch

import android.Manifest
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BuildConfig
import com.wxsoft.fcare.core.data.entity.version.Version
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.service.JPushReceiver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.main.MainActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Inject


class LauncherActivity : BaseActivity(){

    private val downloadManager:DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private var dialog:UpgradeDialog?=null
    @Inject
    lateinit var factory: ViewModelFactory

    private var receiver: RegistrationBroadcastReceiver? = null

    private lateinit var viewModel: LauncherViewModel
    private lateinit var file: File

    private val disposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        viewModel = viewModelProvider(factory)
        receiver =RegistrationBroadcastReceiver(viewModel)
        val intentFilter = IntentFilter()
        // 2. 设置接收广播的类型
        intentFilter.addAction(JPushReceiver.RegistrationId)
        registerReceiver(receiver, intentFilter)
        viewModel.version.observe(this, Observer {

            if (it.changing) {
                checkUpgradePermission()
            }
        })

        viewModel.success.observe(this, Observer {

            val intent = Intent(this,if (it) MainActivity::class.java  else LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        viewModel.update.observe(this, Observer {
            viewModel.version.value?.let(::download)
        })


        checkStoragePermission()
    }

    class RegistrationBroadcastReceiver(var vm: LauncherViewModel?) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val pid = intent?.getStringExtra(JPushReceiver.RegistrationId)
            pid?.apply {
                vm?.setRegistration(this)
            }
        }
    }

    private fun error(throwable: Throwable){}
    private fun doVersion(code:Long){

        viewModel.local=code
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        receiver?.let {
            unregisterReceiver(receiver)
            receiver?.vm = null
        }
        if(completeReceiver!=null){
            unregisterReceiver(completeReceiver)
            completeReceiver=null
        }
    }

    private fun getLocalVersion():Long{
        return if (Build.VERSION.SDK_INT <Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName,0).versionCode.toLong()

        }else{
            packageManager.getPackageInfo(packageName,0).longVersionCode
        }
    }

    private fun showUpdateDialog(){
        if(dialog==null)
            dialog = UpgradeDialog()
        dialog?.show(supportFragmentManager, UpgradeDialog.DIALOG_UPGRADE)
    }
    private fun checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                UPGRADE_PERMISSION_REQUEST
            )

        } else {
            checkUpdate()
        }
    }
    private fun checkUpdate(){
        disposable.add(Single.fromCallable { getLocalVersion() }.subscribe(::doVersion, ::error))
    }

    private fun checkUpgradePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (packageManager.canRequestPackageInstalls()) {
                showUpdateDialog()
            } else {
                AlertDialog.Builder(this,R.style.Theme_FCare_Dialog)
                    .setTitle("提示")
                    .setMessage("请在设置允许安装未知应用的列表里找到无限急救，点击，选择允许")
                    .setPositiveButton("去设置") { _, _ ->
                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                        startActivityForResult(intent, UPGRADE_PERMISSION_SETTING)
                    }
                    .show()
            }
        }else{
            showUpdateDialog()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            UPGRADE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkUpdate()
                } else {
                    checkStoragePermission()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPGRADE_PERMISSION_SETTING->{
                checkUpgradePermission()
            }
        }
    }


    private var completeReceiver: CompleteReceiver? = null


    private fun download(version:Version){

        val request = DownloadManager.Request(Uri.parse("""${BuildConfig.ENDPOINT}${version.url}""")).apply {

            file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "无限急救.apk")
            setDestinationUri(Uri.fromFile(file))
        }
        downloadManager.enqueue(request)
        completeReceiver = CompleteReceiver()
        registerReceiver(
            completeReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    inner class CompleteReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId==-1L){
                viewModel.messageAction.value=Event("下载失败")
            }else {
                try {
                    val uri = downloadManager.getUriForDownloadedFile(completeDownloadId)
                    if(uri==null){
                        viewModel.messageAction.value=Event("下载失败")
                    }else {
                        install(uri)
                    }
                }catch (e:Exception){
                    viewModel.messageAction.value=Event(e.message?:"")
                }
            }
        }

        private fun install(uri:Uri) {

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//4.0以上系统弹出安装成功打开界面
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)

        }
    }

}
