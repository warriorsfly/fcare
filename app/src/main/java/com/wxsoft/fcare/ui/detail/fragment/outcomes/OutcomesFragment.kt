package com.wxsoft.fcare.ui.detail.fragment.outcomes


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentOutcomesBinding
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

class OutcomesFragment : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentOutcomesBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentOutcomesBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@OutcomesFragment)
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

        binding.activeConduitRoomTime.setOnClickListener{
            if (binding.activeConduitRoomTime.text.isEmpty()){
                viewModel.outCome.value!!.hand_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"activeConduitRoomTime")
            }
        }
        binding.item5Line1OutDoorTime.setOnClickListener {
            if (binding.item5Line1OutDoorTime.text.isEmpty()){
                viewModel.outCome.value!!.hand_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"item5Line1OutDoorTime")
            }
        }


        binding.item1Group1.setOnCheckedChangeListener { chipGroup, i ->
            if (i>=0){
                when(i){
                    R.id.is_out ->{
                        viewModel.outCome.value!!.treatment_Result_Code = "1"
                    }
                    R.id.is_toOtherHospital ->{
                        viewModel.outCome.value!!.treatment_Result_Code = "2"
                    }
                    R.id.is_toOtherDepartment ->{
                        viewModel.outCome.value!!.treatment_Result_Code = "3"
                    }
                    R.id.is_die ->{
                        viewModel.outCome.value!!.treatment_Result_Code = "4"
                    }

                }
            }
        }

        binding.item6Line1TransferTime.setOnClickListener {
            if (binding.item6Line1TransferTime.text.isEmpty()){
                viewModel.outCome.value!!.transfer_Date = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"item6Line1TransferTime")
            }
        }
        binding.outcomesItem7Line1DeathTime.setOnClickListener {
            if (binding.outcomesItem7Line1DeathTime.text.isEmpty()){
                viewModel.outCome.value!!.transfer_Date = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"outcomesItem7Line1DeathTime")
            }
        }

        binding.outcomesSubmit.setOnClickListener {
            viewModel.saveOutCome(viewModel.outCome.value!!)
            dismiss()
        }




    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("activeConduitRoomTime")){
            viewModel.outCome.value!!.hand_Time = mTime
        }else if(type.equals("item5Line1OutDoorTime")){
            viewModel.outCome.value!!.hand_Time = mTime
        }else if(type.equals("item5Line1OutDoorTime")){
            viewModel.outCome.value!!.transfer_Date = mTime
        }else if(type.equals("outcomesItem7Line1DeathTime")){
            viewModel.outCome.value!!.transfer_Date = mTime
        }
    }


    companion object {
        const val DIALOG_OUTCOMES = "dialog_outcomes"
        const val REQ_PERMISSION_CODE = 0x12
    }


}
