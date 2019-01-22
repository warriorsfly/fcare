package com.wxsoft.fcare.ui.details.thrombolysis


import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
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
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityThrombolysisBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedConsentActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject


class ThrombolysisActivity : BaseActivity(), OnDateSetListener {

    private var dialog: TimePickerDialog?=null


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
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
            val currentTime=it.text.toString()?.let {text->
                return@let if(text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all");
        }
    }

    private var selectedId=0;

    private lateinit var patientId:String
    private lateinit var thrombolysisId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val THROMBOLYSIS_ID = "THROMBOLYSIS_ID"
        const val INFORMED_CONSENT = 20
        const val DRUG = 30
    }
    private lateinit var viewModel: ThrombolysisViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityThrombolysisBinding

    lateinit var placeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityThrombolysisBinding>(this, R.layout.activity_thrombolysis)
            .apply {
                setLifecycleOwner(this@ThrombolysisActivity)
            }
        patientId=intent.getStringExtra(ThrombolysisActivity.PATIENT_ID)?:""
        thrombolysisId=intent.getStringExtra(ThrombolysisActivity.THROMBOLYSIS_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        placeDialog = Dialog(this)

        back.setOnClickListener { onBackPressed() }

//        viewModel.loadThrombolysis(thrombolysisId)
        viewModel.loadThrombolysis("4c597994fa4449bdaa5dccfa718ea9e7")

        viewModel.informed.observe(this, Observer {  })

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
                    finish()
                }
            }
        })

    }

    fun selectPlace(){
        val adapter = ThromPlaceAdapter(this,viewModel)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_thrombolysis_places, null)
        placeDialog.setContentView(view);
        viewModel.thromPlaces.observe(this, Observer { it -> adapter.items = it ?: emptyList() })
        view.findViewById<RecyclerView>(R.id.place_list).adapter = adapter
        var  attributes = placeDialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.BOTTOM;
        attributes.windowAnimations = R.style.picture_alert_dialog;
        placeDialog.getWindow().setAttributes(attributes);
        placeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        placeDialog.show();
    }

    fun  toInformedConsent(){
        var intent = Intent(this@ThrombolysisActivity, AddInformedConsentActivity::class.java).apply {
            putExtra(AddInformedConsentActivity.PATIENT_ID,patientId)
            putExtra(AddInformedConsentActivity.TITLE_NAME,viewModel.informed.value?.name)
            putExtra(AddInformedConsentActivity.TITLE_CONTENT,viewModel.informed.value?.content)
            putExtra(AddInformedConsentActivity.INFORMED_ID,viewModel.informed.value?.id)
            putExtra(AddInformedConsentActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.INFORMED_CONSENT)
    }

    fun toSeeInformedConsent(){
        var intent = Intent(this@ThrombolysisActivity, InformedConsentDetailsActivity::class.java).apply {
            putExtra(InformedConsentDetailsActivity.PATIENT_ID,patientId)
            putExtra(InformedConsentDetailsActivity.TALK_ID,viewModel.thrombolysis.value?.informedConsentId)
            putExtra(InformedConsentDetailsActivity.TALK_NAME,viewModel.informed.value?.name)
            putExtra(InformedConsentDetailsActivity.INFORMED_ID,viewModel.informed.value?.id)
        }
        startActivity(intent)
    }

    fun toDrugs(){
        var intent = Intent(this@ThrombolysisActivity, PharmacyActivity::class.java).apply {
            putExtra(PharmacyActivity.PATIENT_ID,patientId)
            putExtra(PharmacyActivity.COME_FROM,"THROMBOLYSIS")
        }
        startActivityForResult(intent, ThrombolysisActivity.DRUG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                ThrombolysisActivity.INFORMED_CONSENT ->{//知情同意书
                    viewModel.thrombolysis.value?.informedConsentId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.thrombolysis.value?.start_Agree_Time = data?.getStringExtra("startTime")?:""
                    viewModel.thrombolysis.value?.sign_Agree_Time = data?.getStringExtra("endTime")?:""

                }
                ThrombolysisActivity.DRUG ->{//用药
                    var pharmacy = data?.getSerializableExtra("drugRecords") as Pharmacy
                    viewModel.thrombolysis.value?.drugRecords = emptyList()
                    viewModel.thrombolysis.value?.drugRecords = pharmacy.drugRecordDetails
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
