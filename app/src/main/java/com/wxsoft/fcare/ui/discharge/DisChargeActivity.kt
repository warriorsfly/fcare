package com.wxsoft.fcare.ui.discharge

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.databinding.ActivityDischargeBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_discharge.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DisChargeActivity : BaseActivity(), OnDateSetListener, View.OnClickListener {

    private var dialog: TimePickerDialog?=null
    override fun onClick(v: View?) {

        (v as? Button)?.let {
            selectedId = it.id
            val currentTime = it.text.toString().let { text ->
                return@let if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all");
        }

    }

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<Button>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
    }

    private var selectedId=0;

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: DisChargeViewModel
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityDischargeBinding>(this, R.layout.activity_discharge)
            .apply {
                viewModel = this@DisChargeActivity. viewModel
                lifecycleOwner = this@DisChargeActivity
            }
        patientId=intent.getStringExtra(DisChargeActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        back.setOnClickListener { onBackPressed() }

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.des.observe(this, Observer {
            if( diagnose_list.adapter==null){
                diagnose_list.adapter=DiagnoseAdapter(this@DisChargeActivity,viewModel)
            }

            (diagnose_list.adapter as DiagnoseAdapter).items=it?: emptyList()
        })

        viewModel.commitResult .observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        })

        start.setOnClickListener(this)
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
            .setCurrentMillseconds(if (time == 0L) System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(16)
            .build()
    }
}
