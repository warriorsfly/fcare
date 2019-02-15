package com.wxsoft.fcare.ui.details.assistant.troponin


import android.arch.lifecycle.Observer
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.FragmentTroponinBinding
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.widget.WxDimDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject





class TroponinFragment : WxDimDialogFragment() , HasSupportFragmentInjector , OnDateSetListener {

    private var dialog: TimePickerDialog?=null


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (activity?.findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.draw_blood_time -> viewModel.lisCr.value?.samplingTime = DateTimeUtils.formatter.format(millseconds)
            R.id.draw_blood_report_time -> viewModel.lisCr.value?.reportTime = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime=it.text.toString()?.let {text->
                if(text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(childFragmentManager, "all");
        }
    }

    private var selectedId:Int = 0


    @Inject
    lateinit var factory: ViewModelFactory

    var patientId:String=""

    var recordId:String =""

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var binding: FragmentTroponinBinding
    private lateinit var viewModel: TroponinViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
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
        viewModel=viewModelProvider(factory)
        binding=FragmentTroponinBinding.inflate(inflater,container,false).apply {

            viewModel=this@TroponinFragment.viewModel
            lifecycleOwner = this@TroponinFragment
        }

        viewModel.patientId = patientId
        viewModel.getCrById(recordId)
        viewModel.lisCr.observe(this, Observer {  })

        viewModel.clickEdit.observe(this, Observer {
            when(it){
                "1"-> showDatePicker(binding.drawBloodTime)
                "2"-> showDatePicker(binding.drawBloodReportTime)
                "success" -> {
                    dismiss()
//                    onDestroy()
                }
            }
        })
        return binding.root
    }


    companion object {
        const val TAG = "check_dialog"
    }

    private fun createDialog(time:Long): TimePickerDialog {

        return TimePickerDialog.Builder()
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("选择时间")
            .setYearText("")
            .setMonthText("")
            .setDayText("")
            .setHourText("")
            .setMinuteText("")
            .setCyclic(false)
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(12)
            .build()
    }
//
    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val activity = getActivity()
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }



}
