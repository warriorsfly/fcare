package com.wxsoft.fcare.ui.launch

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.version.Version
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.service.JPushReceiver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.ecg.EcgActivity
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.main.MainActivity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LauncherActivity : BaseActivity(){

    @Inject
    lateinit var factory: ViewModelFactory

    private var receiver: RegistrationBroadcastReceiver? = null

    private lateinit var viewModel: LauncherViewModel

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
    }

    private fun getLocalVersion():Long{
        return if (Build.VERSION.SDK_INT <Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName,0).versionCode.toLong()

        }else{
            packageManager.getPackageInfo(packageName,0).longVersionCode
        }
    }

    private fun checkPhotoTaking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                BaseActivity.UPGRADE_PERMISSION_REQUEST
            )

        }else{

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            BaseActivity.UPGRADE_PERMISSION_REQUEST ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }

}
