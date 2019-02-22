package com.wxsoft.fcare.ui.patient

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityPatientEmrBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.core.utils.inTransaction
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment.Companion.BASE_INFO
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrViewModel

import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class PatientEmrActivity : BaseActivity() {


    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var viewModel:PatientEmrViewModel
    lateinit var emrViewModel: EmrViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        emrViewModel=viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityPatientEmrBinding>(this,R.layout.activity_patient_emr).apply {
            viewModel=this@PatientEmrActivity.viewModel
            emrViewModel=this@PatientEmrActivity.emrViewModel
            lifecycleOwner = this@PatientEmrActivity
        }
        viewModel.patientId = patientId
        viewModel.quality.observe(this, Observer {  })
        back.setOnClickListener { onBackPressed() }
        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, EmrFragment.newInstance(patientId,false,false))
        }
    }

    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                BASE_INFO -> {
                    emrViewModel.refreshBaseInfo()
                    setResult(RESULT_OK)
                }
            }
        }
    }

}
