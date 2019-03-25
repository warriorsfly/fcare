package com.wxsoft.fcare.ui.details.thrombolysis


import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityThrombolysisBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.details.complication.ComplicationActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject


class ThrombolysisActivity : BaseActivity(), OnDateSetListener {

    private var dialog: TimePickerDialog?=null

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.start_thromboly_time -> viewModel.thrombolysis.value?.throm_Start_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.end_thromboly_time -> viewModel.thrombolysis.value?.throm_End_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.end_thromboly_radiography_time -> viewModel.thrombolysis.value?.start_Radiography_Time = DateTimeUtils.formatter.format(millseconds)
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
    private lateinit var id:String
    private lateinit var thrombolysisId:String
    private lateinit var comeFrom:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val THROMBOLYSIS_ID = "THROMBOLYSIS_ID"
        const val COME_FROM = "COME_FROM"
        const val INFORMED_CONSENT = 20
        const val DRUG = 30
        const val COMPLICATION = 40
    }
    private lateinit var viewModel: ThrombolysisViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityThrombolysisBinding
    lateinit var drugAdapter: ThrombolysisDrugsAdapter

    private lateinit var placeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityThrombolysisBinding>(this, R.layout.activity_thrombolysis)
            .apply {
                lifecycleOwner = this@ThrombolysisActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        id=intent.getStringExtra(ID)?:""
        thrombolysisId=intent.getStringExtra(THROMBOLYSIS_ID)?:""
        comeFrom=intent.getStringExtra(COME_FROM)?:""

        viewModel.comefrom = comeFrom
        viewModel.patientId = patientId
        viewModel.id = id
        binding.viewModel = viewModel

        placeDialog = Dialog(this)

        back.setOnClickListener { onBackPressed() }

//        viewModel.loadThrombolysis(thrombolysisId)

//        viewModel.loadThrombolysis(id)

        viewModel.informed.observe(this, Observer {  })

        drugAdapter = ThrombolysisDrugsAdapter(this,viewModel)
        binding.medicalList.adapter = drugAdapter

        viewModel.thrombolysis.observe(this, Observer {
            drugAdapter.submitList(it.drugRecords)
            viewModel.drugs = it.drugRecords
        })

        viewModel.clickLine.observe(this, Observer {
            when(it){
                "place" -> selectPlace()
                "informedConsent" ->{
                    if (viewModel.thrombolysis.value?.informedConsentId.isNullOrEmpty()){
                        toInformedConsent()
                    }else{
                        toSeeInformedConsent()
                    }
                }
                "drugs" -> toDrugs()
                "complication" -> toComplication()
                "refreshDrugs" -> {
                    drugAdapter.submitList(viewModel.drugs)
//                    viewModel.thrombolysis.value?.drugRecords = viewModel.drugs
                }
            }
        })

        viewModel.modifySome.observe(this, Observer {
            when(it){
                "HidenDialog" ->placeDialog.dismiss()
                "ModifyStartInformedTime" -> showDatePicker(findViewById(R.id.start_informed_time))
                "ModifyEndInformedTime" -> showDatePicker(findViewById(R.id.end_informed_time))
                "ModifyStartThromTime" -> showDatePicker(findViewById(R.id.start_thromboly_time))
                "ModifyEndThromTime" -> showDatePicker(findViewById(R.id.end_thromboly_time))
                "ModifyRadiographyTime" -> showDatePicker(findViewById(R.id.end_thromboly_radiography_time))
                "saveSuccess" -> {
                    Intent().let { intent->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

    }

    private fun selectPlace(){
        val adapter = ThromPlaceAdapter(this,viewModel)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_thrombolysis_places, null)
        placeDialog.setContentView(view)
        viewModel.thromPlaces.observe(this, Observer { adapter.items = it ?: emptyList() })
        view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.place_list).adapter = adapter
        val attributes = placeDialog.window?.attributes
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes?.gravity = Gravity.BOTTOM
        attributes?.windowAnimations = R.style.picture_alert_dialog
        placeDialog.window?.attributes = attributes
        placeDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        placeDialog.show()
    }

    private fun  toInformedConsent(){
        val intent = Intent(this@ThrombolysisActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.TITLE_CONTENT,viewModel.informed.value?.content)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.INFORMED_CONSENT)
    }

    private fun toSeeInformedConsent(){
        val intent = Intent(this@ThrombolysisActivity, AddInformedActivity::class.java).apply {
            putExtra(AddInformedActivity.PATIENT_ID,patientId)
            putExtra(AddInformedActivity.TALK_ID,viewModel.thrombolysis.value?.informedConsentId)
            putExtra(AddInformedActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.INFORMED_CONSENT)
    }

    private fun toDrugs(){
        val intent = Intent(this, SelectDrugsActivity::class.java).apply {
            putExtra(SelectDrugsActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, ThrombolysisActivity.DRUG)
    }

    private fun toComplication(){
        val intent = Intent(this@ThrombolysisActivity, ComplicationActivity::class.java).apply {
            putExtra(ComplicationActivity.PATIENT_ID,patientId)
            putExtra(ComplicationActivity.SEN_TYPE,"223-4")//治疗中场景
        }
        startActivityForResult(intent, ThrombolysisActivity.COMPLICATION)
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
                ThrombolysisActivity.DRUG ->{//用药
                    var arr =  viewModel.thrombolysis.value?.drugRecords?.map { it }?: emptyList()
                    val drugs = data?.getSerializableExtra("selectedDrugs") as ArrayList<Drug>
                    val dlist = drugs.map { DrugRecord(it.id).apply {
                        drugName = it.name
                        dose = it.dose
                        doseUnit = it.doseUnit
                        drugId = it.id
                    } }as ArrayList<DrugRecord>
                    dlist.addAll(arr.filter { !dlist.contains(it) })
                    viewModel.thrombolysis.value?.drugRecords = dlist
                    viewModel.drugs = dlist
                    drugAdapter.submitList(dlist)
                }
                ThrombolysisActivity.COMPLICATION ->{//并发症
                    val otherIlls = data?.getStringExtra("otherIlls")
                    binding.ohterIll.setText(otherIlls)
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

}
