package com.wxsoft.fcare.ui.detail.dialog.Check


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.wxsoft.fcare.databinding.FragmentCheckBinding
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


/**
 * A simple [Fragment] subclass.
 *
 */
class CheckDialog : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {


    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var binding:FragmentCheckBinding
    private lateinit var viewModel: PatientDetailViewModel

    var patientId:String=""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=activityViewModelProvider(factory)
        binding.viewModel=viewModel
        viewModel.loadTroponin()

        binding.drawBloodTime.setOnClickListener {
            if (binding.drawBloodTime.text.isEmpty()){
                viewModel.assistCheck.value!!.sampling_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"drawBloodTime")
            }
        }
        binding.drawBloodReportTime.setOnClickListener {
            if (binding.drawBloodReportTime.text.isEmpty()){
                viewModel.assistCheck.value!!.report_Time = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"drawBloodReportTime")
            }
        }


        binding.submit.setOnClickListener {
            viewModel.saveAssistCheck(viewModel.assistCheck.value!!)
            dismiss()
        }








    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCheckBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@CheckDialog)
        }
        return binding.root
    }

    override fun theTime(mTime: String, type: String) {
        if (type.equals("drawBloodTime")){
            viewModel.assistCheck.value!!.sampling_Time = mTime
        }else if(type.equals("drawBloodReportTime")){
            viewModel.assistCheck.value!!.report_Time = mTime
        }
    }


    companion object {
        const val TAG = "check_dialog"
    }

}
