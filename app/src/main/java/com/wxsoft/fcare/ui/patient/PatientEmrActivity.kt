package com.wxsoft.fcare.ui.patient

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityPatientEmrBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.utils.inTransaction
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider

import kotlinx.android.synthetic.main.activity_patient_emr.*
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
            setLifecycleOwner(this@PatientEmrActivity)
        }

        supportFragmentManager.inTransaction {
            replace(R.id.fragment_container, EmrFragment.newInstance(patientId,false))
        }
    }


    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

}
