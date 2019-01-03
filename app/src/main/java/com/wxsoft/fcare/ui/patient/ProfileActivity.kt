package com.wxsoft.fcare.ui.patient

import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityLoginBinding
import com.wxsoft.fcare.databinding.ActivityPatientProfileBinding
import com.wxsoft.fcare.service.JPushReceiver.Companion.RegistrationId
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class ProfileActivity : BaseActivity() {

    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var viewModel:ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityPatientProfileBinding>(
            this,
            R.layout.activity_patient_profile
        )

        viewModel = viewModelProvider(factory)
        binding.apply {
            setLifecycleOwner(this@ProfileActivity)

        }

    }

}
