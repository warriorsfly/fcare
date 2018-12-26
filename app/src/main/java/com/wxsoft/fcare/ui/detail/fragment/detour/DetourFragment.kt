package com.wxsoft.fcare.ui.detail.fragment.detour


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.FragmentDetourBinding
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

class DetourFragment : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {


    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentDetourBinding

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<android.support.v4.app.Fragment>

    private lateinit var viewModel: PatientDetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<android.support.v4.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentDetourBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner(this@DetourFragment)
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

        binding.detourArriveTime.setOnClickListener {
            if (binding.detourArriveTime.text.isEmpty()){
                viewModel.detour.value!!.arrived_Emergency_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"detourArriveTime")
            }
        }

        binding.detourLeaveTime.setOnClickListener {
            if (binding.detourLeaveTime.text.isEmpty()){
                viewModel.detour.value!!.bypass_Emergency_Leave_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"detourLeaveTime")
            }

        }
        binding.detourCCUArriveTime.setOnClickListener {
            if (binding.detourCCUArriveTime.text.isEmpty()){
                viewModel.detour.value!!.arrived_Ccu_Date = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"detourCCUArriveTime")
            }

        }
        binding.detourModel2ArriveTime.setOnClickListener {
            if (binding.detourModel2ArriveTime.text.isEmpty()){
                viewModel.detour.value!!.arrived_Emergency_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"detourModel2ArriveTime")
            }
        }

        binding.model2ItemGroup.setOnCheckedChangeListener { chipGroup, i ->
            if (i>=0){
                when(i){
                    R.id.toDGS ->{
                        viewModel.detour.value!!.bypass_Emergency_Code = "1"
                    }
                    R.id.toCCU ->{
                        viewModel.detour.value!!.bypass_Emergency_Code = "2"
                    }
                    R.id.toXNKBF ->{
                        viewModel.detour.value!!.bypass_Emergency_Code = "3"
                    }
                    R.id.toOther ->{
                        viewModel.detour.value!!.bypass_Emergency_Code = "4"
                    }

                }
            }
        }


        binding.detourSubmit.setOnClickListener {
            viewModel.saveDetour(viewModel.detour.value!!)
            dismiss()
        }


    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("detourArriveTime")){
            viewModel.detour.value!!.arrived_Emergency_Time = mTime
        }else if(type.equals("detourLeaveTime")){
            viewModel.detour.value!!.bypass_Emergency_Leave_Time = mTime
        }else if(type.equals("detourCCUArriveTime")){
            viewModel.detour.value!!.arrived_Ccu_Date = mTime
        }else if(type.equals("detourModel2ArriveTime")){
            viewModel.detour.value!!.arrived_Emergency_Time = mTime
        }
    }

    companion object {
        const val DIALOG_DETOUR = "dialog_detour"
        const val REQ_PERMISSION_CODE = 0x12
    }
}

