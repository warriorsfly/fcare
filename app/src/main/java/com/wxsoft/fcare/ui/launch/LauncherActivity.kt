package com.wxsoft.fcare.ui.launch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.service.JPushReceiver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.main.MainActivity
import javax.inject.Inject

class LauncherActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private var receiver: RegistrationBroadcastReceiver? = null

    private lateinit var viewModel: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        viewModel = viewModelProvider(factory)

        viewModel.success.observe(this, Observer {

            val intent = if(it) Intent(this, MainActivity::class.java) else Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

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

    override fun onDestroy() {
        super.onDestroy()
        receiver?.let {
            unregisterReceiver(receiver)
            receiver?.vm = null
        }
    }

}
