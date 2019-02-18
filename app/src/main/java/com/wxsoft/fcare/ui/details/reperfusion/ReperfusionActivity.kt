package com.wxsoft.fcare.ui.details.reperfusion

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityReperfusionBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class ReperfusionActivity : BaseActivity() , OnDateSetListener {

    private var dialog: TimePickerDialog?=null

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.decide_cabg_time -> viewModel.cabg.value?.decisionOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.start_cabg_time -> viewModel.cabg.value?.startOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.end_cabg_time -> viewModel.cabg.value?.endOperationTime = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { text->
                if(text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private var selectedId=0


    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"

    }

    private lateinit var viewModel: ReperfusionViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityReperfusionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityReperfusionBinding>(this, R.layout.activity_reperfusion)
            .apply {
                lifecycleOwner = this@ReperfusionActivity
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        back.setOnClickListener { onBackPressed() }

        viewModel.getCABG()

        viewModel.mSelectTime.observe(this, Observer {
            when(it){
                "1"->{showDatePicker(findViewById(R.id.decide_cabg_time))}
                "2"->{showDatePicker(findViewById(R.id.start_cabg_time))}
                "3"->{showDatePicker(findViewById(R.id.end_cabg_time))}
            }
        })


        viewModel.backToLast.observe(this, Observer {

            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })


        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this@ReperfusionActivity,it, Toast.LENGTH_SHORT).show()
        })
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


}
