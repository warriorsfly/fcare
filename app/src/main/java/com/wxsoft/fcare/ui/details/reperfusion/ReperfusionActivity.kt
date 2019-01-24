package com.wxsoft.fcare.ui.details.reperfusion

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityReperfusionBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

class ReperfusionActivity : BaseActivity() {

    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"

    }

    private lateinit var viewModel: ReperfusionViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityReperfusionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityReperfusionBinding>(this, R.layout.activity_reperfusion)
            .apply {
                setLifecycleOwner(this@ReperfusionActivity)
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel


    }
}
