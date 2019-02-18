package com.wxsoft.fcare.ui.patient

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityPatientEmrBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.core.utils.inTransaction
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider

import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class PatientEmrActivity : BaseActivity() {


    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var viewModel:ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        viewModel.patientId=patientId
        viewModel.preHos=false
        DataBindingUtil.setContentView<ActivityPatientEmrBinding>(this,R.layout.activity_patient_emr).apply {
            viewModel=this@PatientEmrActivity.viewModel
            lifecycleOwner = this@PatientEmrActivity
        }

        back.setOnClickListener { onBackPressed() }
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, EmrFragment.newInstance(patientId,false))
        }
    }

    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

}
