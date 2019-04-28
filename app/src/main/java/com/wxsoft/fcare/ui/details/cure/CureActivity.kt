package com.wxsoft.fcare.ui.details.cure

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityCureBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.DiagnoseNewActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import com.wxsoft.fcare.utils.ActionCode
import kotlinx.android.synthetic.main.layout_cure_pci.*
import kotlinx.android.synthetic.main.layout_cure_thrombolysis.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class CureActivity : BaseActivity() , OnDateSetListener {

    private var dialog: TimePickerDialog?=null
    private val selectedIndex= mutableListOf<Int>()


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.start_thromboly_time -> viewModel.thrombolysis.value?.throm_Start_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.end_thromboly_time -> viewModel.thrombolysis.value?.throm_End_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.end_thromboly_radiography_time -> viewModel.thrombolysis.value?.start_Radiography_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.decide_cabg_time -> viewModel.cabg.value?.decisionOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.start_cabg_time -> viewModel.cabg.value?.startOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.end_cabg_time -> viewModel.cabg.value?.endOperationTime = DateTimeUtils.formatter.format(millseconds)
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

    private fun selectPlace(){
        val list= viewModel.docs.map { it.trueName }.toTypedArray()

        val selectedItems= viewModel.docs.map { user ->

            viewModel.intervention.value?.interventionMateIds?.contains(user.id)?:false
        }.toBooleanArray()

        AlertDialog.Builder(this).setMultiChoiceItems(list,selectedItems)
        { _, which, isChecked ->
            if(selectedIndex.contains(which) && !isChecked){
                selectedIndex.remove(which)

            }else if(!selectedIndex.contains(which) && isChecked){
                selectedIndex.add(which)
            }

            viewModel.intervention.value?.interventionMateIds=selectedIndex.joinToString {
                viewModel.docs[it].id
            }

            viewModel.intervention.value?.interventionMates=selectedIndex.joinToString {
                viewModel.docs[it].trueName
            }
        }.show()
    }

    private var selectedId=0


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_DIAGNOSE_TYPE = 10
        const val PCI_INFORMED_CONSENT = 60
    }

    private lateinit var viewModel: CureViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityCureBinding

    private lateinit var placeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityCureBinding>(this, R.layout.activity_cure)
            .apply {
                viewModel = this@CureActivity.viewModel
                lifecycleOwner = this@CureActivity
            }
        patientId=intent.getStringExtra(DiagnoseActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        setSupportActionBar(toolbar)
        title="治疗操作"

        placeDialog = Dialog(this)
        viewModel.informed.observe(this, Observer {  })



        viewModel.modifySome.observe(this, Observer {
            when(it){
                "0"->selectPlace()
                "1"->showDatePicker(findViewById(R.id.start))
                "2"->showDatePicker(findViewById(R.id.end_thromboly_time))
                "3"->showDatePicker(findViewById(R.id.patient_arrive))
                "4"->showDatePicker(findViewById(R.id.start_puncture))
                "5"->showDatePicker(findViewById(R.id.punctured))
                "6"->showDatePicker(findViewById(R.id.start_angiography))
                "7"->showDatePicker(findViewById(R.id.angiographied))
                "8"->showDatePicker(findViewById(R.id.wire))
                "9"->showDatePicker(findViewById(R.id.end))
                "10"->showDatePicker(findViewById(R.id.leave))

                "place" -> selectPlace1()
                "informedConsent" ->{
                    if (viewModel.thrombolysis.value?.informedConsentId.isNullOrEmpty()){
                        toInformedConsent()
                    }else{
                        toSeeInformedConsent()
                    }
                }

                "HidenDialog" ->placeDialog.dismiss()
                "ModifyStartInformedTime" -> showDatePicker(findViewById(R.id.start_informed_time))
                "ModifyEndInformedTime" -> showDatePicker(findViewById(R.id.end_informed_time))
                "ModifyStartThromTime" -> showDatePicker(findViewById(R.id.start_thromboly_time))
                "ModifyEndThromTime" -> showDatePicker(findViewById(R.id.end_thromboly_time))
                "ModifyRadiographyTime" -> showDatePicker(findViewById(R.id.end_thromboly_radiography_time))
                "3-1"-> showDatePicker(findViewById(R.id.decide_cabg_time))
                "3-2"-> showDatePicker(findViewById(R.id.start_cabg_time))
                "3-3"-> showDatePicker(findViewById(R.id.end_cabg_time))

                "saveSuccess" ->{
                    Intent().let { intent->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
                "isnull" ->{
                    AlertDialog.Builder(this@CureActivity,R.style.Theme_FCare_Dialog)
                        .setTitle("请先完成诊断")
                        .setMessage("完成诊断后才可以进行治疗操作哦")
                        .setPositiveButton("确定") { _, _ ->
                            val intent = Intent(this@CureActivity,  DiagnoseNewActivity::class.java).apply {
                                putExtra(DiagnoseNewActivity.PATIENT_ID, patientId)
                            }
                            startActivityForResult(intent, ActionCode.DIAGNOSE)
                        }
                        .setNegativeButton("取消") { _, _ ->
                            Intent().let { intent->
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        }.show()
                }


            }
        })

        radio_group1.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbt_1 ->{viewModel.cure.value?.throm?.throm_Drug_Type_Dt = "25-1"}
                R.id.rbt_2 ->{viewModel.cure.value?.throm?.throm_Drug_Type_Dt = "25-2"}
                R.id.rbt_3 ->{viewModel.cure.value?.throm?.throm_Drug_Type_Dt = "25-3"}
            }
        })
        radio_group2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbt2_1 ->{viewModel.cure.value?.throm?.throm_Drug_Code_Dt = "26-1"}
                R.id.rbt2_2 ->{viewModel.cure.value?.throm?.throm_Drug_Code_Dt = "26-2"}
            }
        })

        line13.setOnClickListener {
            if (viewModel.intervention.value?.informedConsentId.isNullOrEmpty()){
                toPciInformedConsent()
            }else{
                toSeePciInformedConsent()
            }
        }

    }


    private fun selectPlace1(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "ThromSelectPlace")
        }
        startActivityForResult(intent, ThrombolysisActivity.SELECT_PLACE)

    }

    private fun  toInformedConsent(){
        val intent = Intent(this@CureActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.TITLE_CONTENT,viewModel.informed.value?.content)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.INFORMED_CONSENT)
    }

    private fun toSeeInformedConsent(){
        val intent = Intent(this@CureActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TALK_ID,viewModel.talk.value?.id)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.INFORMED_CONSENT)
    }

    private fun  toPciInformedConsent(){
        val intent = Intent(this@CureActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.TITLE_CONTENT,viewModel.informed.value?.content)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, PCI_INFORMED_CONSENT)
    }

    private fun toSeePciInformedConsent(){
        val intent = Intent(this@CureActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TALK_ID,viewModel.intervention.value?.informedConsentId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, PCI_INFORMED_CONSENT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                ThrombolysisActivity.INFORMED_CONSENT ->{//知情同意书
                    viewModel.thrombolysis.value?.informedConsentId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.thrombolysis.value?.start_Agree_Time = data?.getStringExtra("startTime")?:""
                    viewModel.thrombolysis.value?.sign_Agree_Time = data?.getStringExtra("endTime")?:""
//                    viewModel.thrombolysis.value?.allTime = data?.getStringExtra("allTime")?:""

                }
                ThrombolysisActivity.SELECT_PLACE ->{//溶栓场所
                    val place = data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.thrombolysis.value?.thromTreatmentPlaceName = place.itemName
                    viewModel.thrombolysis.value?.throm_Treatment_Place = place.id
                }
                PCI_INFORMED_CONSENT ->{//pci知情同意书
                    viewModel.intervention.value?.informedConsentId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.talk.value?.startTime = data?.getStringExtra("startTime")?:""
                    viewModel.talk.value?.endTime = data?.getStringExtra("endTime")?:""
                    viewModel.talk.value?.allTime = data?.getStringExtra("allTime")?:""
                    viewModel.intervention.value?.start_Agree_Time = data?.getStringExtra("startTime")?:""
                    viewModel.intervention.value?.sign_Agree_Time = data?.getStringExtra("endTime")?:""
                }

                ActionCode.DIAGNOSE ->{
                    viewModel.loadCure()
                }

            }
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.save()
                true
            }
            else->super.onOptionsItemSelected(item)
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
            .setWheelItemTextSize(16)
            .setThemeColor(R.color.colorPrimary)
            .build()
    }

}
