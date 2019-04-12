package com.wxsoft.fcare.ui.details.diagnose.diagnosenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDiagnoseNewBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DiagnoseNewActivity : BaseActivity() , OnDateSetListener {
    private var dialog: TimePickerDialog?=null


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
//            R.id.start_4 -> viewModel.talk.value?.startTime = DateTimeUtils.formatter.format(millseconds)
//            R.id.start_9 -> viewModel.talk.value?.endTime = DateTimeUtils.formatter.format(millseconds)
        }
//        viewModel.talk.value?.judgeTime()
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
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_DIAGNOSE_TYPE = 10
    }

    private lateinit var viewModel: DiagnoseNewViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDiagnoseNewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDiagnoseNewBinding>(this, R.layout.activity_diagnose_new)
            .apply {
                viewModel = this@DiagnoseNewActivity.viewModel
                line1.setOnClickListener {
                    toSelectSonDiagnose()
                }
                line4.setOnClickListener {
                    showDatePicker(findViewById(R.id.start_4))
                }
                line9.setOnClickListener {
                    showDatePicker(findViewById(R.id.start_9))
                }
                lifecycleOwner = this@DiagnoseNewActivity
            }
        patientId=intent.getStringExtra(DiagnoseActivity.PATIENT_ID)?:""

        setSupportActionBar(toolbar)
        title="诊断"
    }





    fun toSelectSonDiagnose(){
        val intent = Intent(this, SelectDiagnoseActivity::class.java).apply {
            putExtra(SelectDiagnoseActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, SELECT_DIAGNOSE_TYPE)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_DIAGNOSE_TYPE ->{
                    val diagnose = data?.getSerializableExtra("haveSelectedDiagnose") as Diagnosis
                    viewModel.loadDiagnosis.value = diagnose
                }

            }
        }
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{

                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
