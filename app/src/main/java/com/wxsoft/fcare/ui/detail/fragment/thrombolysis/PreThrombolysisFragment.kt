package com.wxsoft.fcare.ui.detail.fragment.thrombolysis

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentDrugBinding
import com.wxsoft.fcare.databinding.FragmentPreThrombolysisBinding
import com.wxsoft.fcare.databinding.ItemDiagnosisBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.detail.PatientDetailViewModel
import com.wxsoft.fcare.ui.detail.fragment.countdown.CountdownTimeFragment
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.activityViewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class PreThrombolysisFragment: WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentPreThrombolysisBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentPreThrombolysisBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@PreThrombolysisFragment)
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
        binding.startKnowTime.setOnClickListener{ _ ->
            if (viewModel.thrombolysis.value!!.start_Agree_Time.isEmpty()){
                viewModel.thrombolysis.value!!.start_Agree_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"start_Agree_Time")
            }
        }
        binding.signTime.setOnClickListener{ _ ->
            if (viewModel.thrombolysis.value!!.sign_Agree_Time.isEmpty()){
                viewModel.thrombolysis.value!!.sign_Agree_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"sign_Agree_Time")
            }
        }
        binding.startThrombolysisTime.setOnClickListener{ _ ->
            if (viewModel.thrombolysis.value!!.throm_Start_Time.isEmpty()){
                viewModel.thrombolysis.value!!.throm_Start_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"throm_Start_Time")
            }
        }
        binding.endThrombolysisTime.setOnClickListener{ _ ->
            if (viewModel.thrombolysis.value!!.throm_End_Time.isEmpty()){
                viewModel.thrombolysis.value!!.throm_End_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"throm_End_Time")
            }
        }
        binding.radiographyTime.setOnClickListener{ _ ->
            if (viewModel.thrombolysis.value!!.start_Radiography_Time.isEmpty()){
                viewModel.thrombolysis.value!!.start_Radiography_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"start_Radiography_Time")
            }
        }

        binding.item10Group.setOnCheckedChangeListener { chipGroup, i ->
            if (i>=0){
                when(i){
                    R.id.is_first ->{
                        viewModel.thrombolysis.value!!.throm_Drug_Type = "1"
                    }
                    R.id.is_second ->{
                        viewModel.thrombolysis.value!!.throm_Drug_Type = "2"
                    }
                    R.id.is_three ->{
                        viewModel.thrombolysis.value!!.throm_Drug_Type = "3"
                    }

                }
            }
        }

        binding.item11Group.setOnCheckedChangeListener { chipGroup, i ->
            if (i>=0){
                when(i){
                    R.id.is_all ->{
                        viewModel.thrombolysis.value!!.throm_Drug_Code = "1"
                    }
                    R.id.is_half ->{
                        viewModel.thrombolysis.value!!.throm_Drug_Code = "2"
                    }
                }
            }
        }

        binding.item4Group.setOnCheckedChangeListener { chipGroup, i ->
            if (i>=0){
                when(i){
                    R.id.is_otherhospital ->{
                        viewModel.thrombolysis.value!!.throm_Treatment_Place = "1"
                    }
                    R.id.not_otherhospital ->{
                        viewModel.thrombolysis.value!!.throm_Treatment_Place = "2"
                    }
                }
            }
        }


        binding.prethromSubmit.setOnClickListener{ _ ->
            viewModel.thrombolysis.value!!.pre_Hospital = true
            viewModel.thrombolysis.value!!.patientId = viewModel.patientId
            viewModel.saveThrom(viewModel.thrombolysis.value!!)
            dismiss()
        }



    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("start_Agree_Time")){
            viewModel.thrombolysis.value!!.start_Agree_Time = mTime
        }else if(type.equals("sign_Agree_Time")){
            viewModel.thrombolysis.value!!.sign_Agree_Time = mTime
        }else if(type.equals("throm_Start_Time")){
            viewModel.thrombolysis.value!!.throm_Start_Time = mTime
        }else if(type.equals("throm_End_Time")){
            viewModel.thrombolysis.value!!.throm_End_Time = mTime
        }else if(type.equals("start_Radiography_Time")){
            viewModel.thrombolysis.value!!.start_Radiography_Time = mTime
        }
    }


    companion object {
        const val DIALOG_PRE_THROMBOLYSIS = "dialog_pre_thrombolysis"
        const val REQ_PERMISSION_CODE = 0x12
    }


}

