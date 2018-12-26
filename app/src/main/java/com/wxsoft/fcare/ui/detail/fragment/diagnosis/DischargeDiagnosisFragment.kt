package com.wxsoft.fcare.ui.detail.fragment.diagnosis


import android.os.Bundle
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentDischargeDiagnosisBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class DischargeDiagnosisFragment : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentDischargeDiagnosisBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentDischargeDiagnosisBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@DischargeDiagnosisFragment)
        }

        viewModel = activityViewModelProvider(factory)


        return binding.root

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel=viewModel

        binding.sureDiagnosisTime.setOnClickListener{

            if (binding.sureDiagnosisTime.text.isEmpty()){
                viewModel.outHospitalDiagnosis.value!!.diagnosis_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"sureDiagnosisTime")
            }
        }


        binding.outHospitalDiagnosisSubmit.setOnClickListener {
            viewModel.saveOutHospitalDiagnosis(viewModel.outHospitalDiagnosis.value!!)
            dismiss()
        }


    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("sureDiagnosisTime")){
            viewModel.outHospitalDiagnosis.value!!.diagnosis_Time = mTime
        }
    }


    companion object {
        const val DIALOG_DISCHARGE_DIAGNOSIS = "dialog_discharge_diagnosis"
        const val REQ_PERMISSION_CODE = 0x12
    }


}
