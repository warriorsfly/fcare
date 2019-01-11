package com.wxsoft.fcare.ui.details.vitalsigns

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityVitalSignsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class VitalSignsActivity : BaseActivity() {

    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: VitalSignsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityVitalSignsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityVitalSignsBinding>(this, R.layout.activity_vital_signs)
            .apply {
                setLifecycleOwner(this@VitalSignsActivity)
            }
        back.setOnClickListener { onBackPressed() }
        patientId=intent.getStringExtra(PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        viewModel.loadVitalSign()

        viewModel.backToLast.observe(this, Observer { onBackPressed() })

    }


}
