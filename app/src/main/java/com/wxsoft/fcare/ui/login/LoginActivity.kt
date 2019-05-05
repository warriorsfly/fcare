package com.wxsoft.fcare.ui.login

import androidx.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityLoginBinding
import com.wxsoft.fcare.service.JPushReceiver.Companion.RegistrationId
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private var receiver: RegistrationBroadcastReceiver? = null

    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this,
            R.layout.activity_login
        ).apply {
            lifecycleOwner = this@LoginActivity
        }

        viewModel = viewModelProvider(factory)

        receiver = RegistrationBroadcastReceiver(viewModel)
        val intentFilter = IntentFilter()
        // 2. 设置接收广播的类型
        intentFilter.addAction(RegistrationId)
        registerReceiver(receiver, intentFilter)


        binding.viewModel = viewModel
        binding.password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                return@OnEditorActionListener true
            }
            false
        })
        binding.emailSignInButton.setOnClickListener {

            binding.viewModel?.login()

        }

        viewModel.account.observe(this, Observer {
            if (it != null && it.success) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

    }

    class RegistrationBroadcastReceiver(var vm: LoginViewModel?) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val pid = intent?.getStringExtra(RegistrationId)
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
