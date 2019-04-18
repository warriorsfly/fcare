package com.wxsoft.fcare.ui.details.diagnose.diagnosenew

import android.app.Activity
import android.content.Intent
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
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Strategy
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDiagnoseNewBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug.ACSDrugActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment.TreatmentOptionsActivity
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import com.wxsoft.fcare.utils.ActionCode
import kotlinx.android.synthetic.main.activity_diagnose_new.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DiagnoseNewActivity : BaseActivity() , OnDateSetListener {
    private var dialog: TimePickerDialog?=null


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
//        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.start_4 -> viewModel.diagnosis.value?.diagnosisTime = DateTimeUtils.formatter.format(millseconds)
            R.id.start_12 -> viewModel.selectedTreatment.value?.selectiveOrTransportTime = DateTimeUtils.formatter.format(millseconds)
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
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_DIAGNOSE_TYPE = 10
        const val SELECT_TREATMENT = 20
        const val SELECT_NOREFUSHION_RESON = 30
        const val SELECT_ACSDRUG = 40
        const val INFORMED_CONSENT = 50
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
                line12.setOnClickListener {
                    showDatePicker(findViewById(R.id.start_12))
                }
                line9.setOnClickListener {
                    toSelectTreatment()
                }
                line10.setOnClickListener {
                    toSelectNoReperfusionReson()
                }
                line7.setOnClickListener {
                    toSelectDrugs()
                }
                lifecycleOwner = this@DiagnoseNewActivity
            }
        patientId=intent.getStringExtra(DiagnoseActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        setSupportActionBar(toolbar)
        title="诊断"

        viewModel.diagnosisTreatment.observe(this, Observer {  })
        viewModel.selectedTreatment.observe(this, Observer {  })
        viewModel.diagnosis.observe(this, Observer {  })
        viewModel.acsDrug.observe(this, Observer {  })
        viewModel.talk.observe(this, Observer {  })

        line8.setOnClickListener{
            val intent = Intent(this, RatingSubjectActivity::class.java).apply {
                putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
                putExtra(RatingSubjectActivity.RATING_NAME, "GRACE评分")
                putExtra(RatingSubjectActivity.RATING_ID, "5")
                putExtra(RatingSubjectActivity.RECORD_ID, viewModel.diagnosisTreatment.value?.graceRating?.id?:"")
            }
            startActivityForResult(intent, ActionCode.RATING)
        }
        line13.setOnClickListener {
            toInformedConsent()
        }

        viewModel.saveResult.observe(this, Observer {
            if (it.equals("success")){
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        })
    }





    fun toSelectSonDiagnose(){
        val intent = Intent(this, SelectDiagnoseActivity::class.java).apply {
            putExtra(SelectDiagnoseActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, SELECT_DIAGNOSE_TYPE)
    }

    fun toSelectTreatment(){
        val intent = Intent(this, TreatmentOptionsActivity::class.java).apply {
            putExtra(TreatmentOptionsActivity.TREATMENT_ID, viewModel.selectedTreatment.value?.strategyCode)
        }
        startActivityForResult(intent, SELECT_TREATMENT)
    }

    fun toSelectNoReperfusionReson(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "Treatment")
            putExtra(SelecterOfOneModelActivity.ID, viewModel.selectedTreatment.value?.noReperfusionCode)
        }
        startActivityForResult(intent, SELECT_NOREFUSHION_RESON)
    }

    fun toSelectDrugs(){
        val intent = Intent(this, ACSDrugActivity::class.java).apply {
            putExtra(ACSDrugActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, SELECT_ACSDRUG)
    }

    private fun  toInformedConsent(){
        val title = if (viewModel.selectedTreatment.value?.strategyCode.equals("14-1")) "PCI术前知情同意书" else "溶栓术前知情同意书"
        val typeId = if (viewModel.selectedTreatment.value?.strategyCode.equals("14-1")||viewModel.selectedTreatment.value?.strategyCode.equals("14-3")) "1" else "2"
        val intent = Intent(this@DiagnoseNewActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TITLE_NAME,title)
            putExtra(AddInformedActivity.TALK_ID,viewModel.diagnosisTreatment.value?.talk?.id)
            putExtra(AddInformedActivity.INFORMED_ID,typeId)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, INFORMED_CONSENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_DIAGNOSE_TYPE ->{
                    val diagnose = data?.getSerializableExtra("haveSelectedDiagnose") as Diagnosis
                    diagnose.patientId = this@DiagnoseNewActivity.patientId
                    viewModel.loadDiagnosis.value = diagnose
                    viewModel.diagnosisTreatment.value?.diagnosis = diagnose
                }

                SELECT_TREATMENT ->{
                    val dic = data?.getSerializableExtra("SelectOption") as Dictionary
                    viewModel.loadSelectedTreatment.value = Strategy("").apply {
                        strategyCode = dic.id
                        patientId = this@DiagnoseNewActivity.patientId
                        strategyCode_Name = dic.itemName
                        memo = dic.memo
                    }
//                    viewModel.diagnosisTreatment.value?.treatStrategy = Strategy("").apply {
//                        strategyCode = dic.id
//                        strategyCode_Name = dic.itemName
//                        memo = dic.memo
//                    }
                }

                ActionCode.RATING->{
                    val score=data?.getIntExtra("SCORE",0)
                    viewModel.graceScore.set(score)
                    viewModel.diagnosisTreatment.value?.graceRating?.score=score?:0
                }

                SELECT_NOREFUSHION_RESON ->{
                    val dic = data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.loadSelectedTreatment.value = Strategy("").apply {
                        strategyCode = "14-8"
                        strategyCode_Name = "无再灌注措施"
                        memo = "group3"
                        noReperfusionCode = dic.id
                        noReperfusionCodeName = dic.itemName
                        patientId = this@DiagnoseNewActivity.patientId
                    }
//                    viewModel.diagnosisTreatment.value?.treatStrategy = Strategy("").apply {
//                        strategyCode = "14-8"
//                        strategyCode_Name = "无再灌注措施"
//                        memo = "group3"
//                        noReperfusionCode = dic.id
//                        noReperfusionCodeName = dic.itemName
//                    }
                }
                SELECT_ACSDRUG ->{
                    val acsDrug = data?.getSerializableExtra("SelectedACSDrug") as ACSDrug
                    acsDrug.haveData = true
                    acsDrug.haveDrugs()
                    viewModel.loadAcsDrug.value = acsDrug
                }

                INFORMED_CONSENT ->{
                    viewModel.talk.value?.informedConsentId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.talk.value?.startTime = data?.getStringExtra("startTime")?:""
                    viewModel.talk.value?.endTime = data?.getStringExtra("endTime")?:""
                    viewModel.talk.value?.allTime = data?.getStringExtra("allTime")?:""
                    viewModel.talk.value?.informedConsent?.informedTypeName = data?.getStringExtra("typename")?:""
                    viewModel.talk.value?.judgeTime()
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
                viewModel.saveDiagnose()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
