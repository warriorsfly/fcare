package com.wxsoft.fcare.ui.details.informedconsent

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityInformedConsentBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class InformedConsentActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: InformedConsentViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityInformedConsentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityInformedConsentBinding>(this, R.layout.activity_informed_consent)
            .apply {
                setLifecycleOwner(this@InformedConsentActivity)
            }
        patientId=intent.getStringExtra(InformedConsentActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        back.setOnClickListener { onBackPressed() }


    }
}
