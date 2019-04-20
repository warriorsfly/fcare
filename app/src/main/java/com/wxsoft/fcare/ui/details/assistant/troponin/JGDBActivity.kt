package com.wxsoft.fcare.ui.details.assistant.troponin

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityJgdbBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class JGDBActivity : BaseActivity() , OnDateSetListener {

    private var dialog: TimePickerDialog?=null
    private val selectedIndex= mutableListOf<Int>()


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.draw_blood_time -> viewModel.lisCr.value?.samplingTime = DateTimeUtils.formatter.format(millseconds)
            R.id.draw_blood_report_time -> viewModel.lisCr.value?.reportTime = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { txt->
                if(txt.isEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private var selectedId=0



    private lateinit var patientId:String
    private lateinit var recordId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val RECORD_ID = "RECORD_ID"
    }

    private lateinit var viewModel: TroponinViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityJgdbBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityJgdbBinding>(this, R.layout.activity_jgdb)
            .apply {
                viewModel = this@JGDBActivity.viewModel
                lifecycleOwner = this@JGDBActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        recordId=intent.getStringExtra(RECORD_ID)?:""
        viewModel.patientId = patientId
        viewModel.getCrById(patientId)
        viewModel.lisCr.observe(this, Observer {  })
        setSupportActionBar(toolbar)
        title="肌钙蛋白"
//        viewModel.loadTroponin()

        viewModel.clickEdit.observe(this, Observer {
            when(it){
                "1"-> showDatePicker(binding.drawBloodTime)
                "2"-> showDatePicker(binding.drawBloodReportTime)
                "success" -> {

//                    onDestroy()
                }
            }
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
            .setWheelItemTextSize(16)
            .build()
    }



}
