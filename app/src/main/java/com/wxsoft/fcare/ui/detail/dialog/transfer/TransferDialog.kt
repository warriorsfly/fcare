package com.wxsoft.fcare.ui.detail.dialog.transfer


import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentTransferBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.utils.TimesUtils
import com.wxsoft.fcare.utils.viewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.layout_120_transfer.*
import kotlinx.android.synthetic.main.layout_from_others_transfer.*
import kotlinx.android.synthetic.main.layout_in_department_transfer.*
import kotlinx.android.synthetic.main.layout_self_coming_transfer.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class TransferDialog : WxDimDialogFragment(), HasSupportFragmentInjector, TimesUtils.SelectTimeListener {
    override fun theTime(mTime: String, type: String) {

        when(type){
            "self_arrive"-> self_arrive.text = mTime
            "self_admission"-> self_admission.text = mTime

            "department_admission"-> department_admission.text = mTime
            "department_leave"-> department_leave.text = mTime

            "other_admission"-> other_admission.text = mTime
            "other_ambulance"-> other_ambulance.text = mTime
            "other_arrive"-> other_arrive.text = mTime
            "other_leave_out"-> other_leave_out.text = mTime
            "other_leave_out_arrive"-> other_leave_out_arrive.text = mTime
            "other_transfer"-> other_transfer.text = mTime

            "arrive_120"-> arrive_120.text = mTime
            "arrive_scence_120"-> arrive_scence_120.text = mTime
            "admission_120"-> admission_120.text = mTime
        }
    }

    @Inject
    lateinit var factory: ViewModelFactory


    private lateinit var binding:FragmentTransferBinding
    private lateinit var viewModel: TransferViewModel

    var patientId:String=""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=viewModelProvider(factory)
        binding.viewModel=viewModel

        binding.viewModel?.patientId=patientId

        viewModel.result.observe(this, Observer {
            if(it!=null &&it.success)
                dismiss()
        })

        /**
         * 自行来院
         */
        self_arrive.setOnClickListener{

            if (self_arrive.text.isEmpty()){
                self_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"self_arrive")
            }

        }

        self_admission.setOnClickListener{

            if (self_admission.text.isEmpty()){
                self_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"self_admission")
            }

        }

        department_admission.setOnClickListener{

            if (department_admission.text.isEmpty()){
                department_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"department_admission")
            }

        }

        department_leave.setOnClickListener{

            if (department_leave.text.isEmpty()){
                department_leave.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"department_leave")
            }

        }

        other_admission.setOnClickListener{

            if (other_admission.text.isEmpty()){
                other_admission.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_admission")
            }

        }


        other_ambulance.setOnClickListener{

            if (other_ambulance.text.isEmpty()){
                other_ambulance.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_ambulance")
            }
        }
        other_arrive.setOnClickListener{

            if (other_arrive.text.isEmpty()){
                other_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_arrive")
            }
        }

        other_leave_out.setOnClickListener{

            if (other_leave_out.text.isEmpty()){
                other_leave_out.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_leave_out")
            }
        }
        other_leave_out_arrive.setOnClickListener{

            if (other_leave_out_arrive.text.isEmpty()){
                other_leave_out_arrive.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_leave_out_arrive")
            }
        }

        other_transfer.setOnClickListener{

            if (other_transfer.text.isEmpty()){
                other_transfer.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"other_transfer")
            }
        }

        arrive_120.setOnClickListener{

            if (arrive_120.text.isEmpty()){
                arrive_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"arrive_120")
            }
        }

        arrive_scence_120.setOnClickListener{

            if (arrive_scence_120.text.isEmpty()){
                arrive_scence_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"arrive_scence_120")
            }
        }

        admission_120.setOnClickListener{

            if (admission_120.text.isEmpty()){
                admission_120.text = TimesUtils.getCurrentTime()
            }else{
                TimesUtils.selectTime(this.context!!,this,"admission_120")
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentTransferBinding.inflate(inflater,container,false).apply {
            setLifecycleOwner(this@TransferDialog)
        }

        return binding.root
    }

    companion object {
        const val TAG = "transfer_dialog"
    }

}



