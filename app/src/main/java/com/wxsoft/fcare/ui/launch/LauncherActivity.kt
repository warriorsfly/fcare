package com.wxsoft.fcare.ui.launch

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
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.BuildConfig
import com.wxsoft.fcare.core.data.entity.version.Version
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.service.JPushReceiver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.main.MainActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.lang.ref.WeakReference
import javax.inject.Inject
import androidx.core.content.FileProvider


class LauncherActivity : BaseActivity(){

    private val downloadManager:DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

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

        viewModel.version.observe(this, Observer {
//            if(!it.changing) {
            if(it.changing) {
                var dialog = UpgradeDialog()
                dialog.show(supportFragmentManager, UpgradeDialog.DIALOG_UPGRADE)
            }
        })

        viewModel.success.observe(this, Observer {

            val intent = if(it) Intent(this, MainActivity::class.java) else Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

        viewModel.update.observe(this, Observer {
            viewModel.version.value?.let(::download)
        })

        disposable.add(Single.fromCallable { getLocalVersion() }.subscribe(::doVersion,::error))

        receiver =RegistrationBroadcastReceiver(viewModel)
        val intentFilter = IntentFilter()
        // 2. 设置接收广播的类型
        intentFilter.addAction(JPushReceiver.RegistrationId)
        registerReceiver(receiver, intentFilter)
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

        completeReceiver?.let{unregisterReceiver(it)}
    }

    private fun getLocalVersion():Long{
        return if (Build.VERSION.SDK_INT <Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName,0).versionCode.toLong()

        }else{
            packageManager.getPackageInfo(packageName,0).longVersionCode
        }
    }

//    private fun checkPhotoTaking(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                BaseActivity.UPGRADE_PERMISSION_REQUEST
//            )
//
//        }else{
//
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            BaseActivity.UPGRADE_PERMISSION_REQUEST ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }


    private var completeReceiver: CompleteReceiver? = null


    private fun download(version:Version){

        val request = DownloadManager.Request(Uri.parse(BuildConfig.ENDPOINT+version.url)).apply {

            file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "无限急救.apk")
            setDestinationUri(Uri.fromFile(file))
        }
        downloadManager.enqueue(request)
        completeReceiver = CompleteReceiver(downloadManager,file)
        registerReceiver(
            completeReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    class CompleteReceiver ( download: DownloadManager,fil:File): BroadcastReceiver() {
        private val manager = WeakReference(download)
        private val file = WeakReference(fil)

        override fun onReceive(context: Context, intent: Intent) {

           install(context)
        }

        private fun install(context: Context) {
           val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
           context.startActivity(intent)

//            file.get()?.let {
//                val uri = FileProvider.getUriForFile(
//                    context,
//                    context.applicationContext.packageName + ".fileProvider",
//                    it
//                    )
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(uri, "application/vnd.android.package-archive")
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//4.0以上系统弹出安装成功打开界面
//                context.startActivity(intent)
//            }
        }
    }

}
