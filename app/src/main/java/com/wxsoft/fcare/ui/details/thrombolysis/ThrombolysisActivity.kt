package com.wxsoft.fcare.ui.details.thrombolysis


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.EntityIdName
import com.wxsoft.fcare.core.data.entity.ThromboStaff
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityThrombolysisBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.complication.ComplicationActivity
import com.wxsoft.fcare.ui.details.informedconsent.addinformed.AddInformedActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import kotlinx.android.synthetic.main.activity_thrombolysis.*
import kotlinx.android.synthetic.main.item_share_item.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject


class ThrombolysisActivity : BaseTimingActivity(){
    override fun selectTime(millseconds: Long) {

        if(viewModel.itemForChangeTime.value==null) {
            (findViewById<TextView>(selectedId))?.text = DateTimeUtils.formatter.format(millseconds)
            when (selectedId) {
                R.id.start_thromboly_time -> viewModel.thrombolysis.value?.throm_Start_Time =
                    DateTimeUtils.formatter.format(millseconds)
                R.id.end_thromboly_time -> viewModel.thrombolysis.value?.throm_End_Time =
                    DateTimeUtils.formatter.format(millseconds)
                R.id.end_thromboly_radiography_time -> viewModel.thrombolysis.value?.start_Radiography_Time =
                    DateTimeUtils.formatter.format(millseconds)
                R.id.start1 -> viewModel.thrombolysis.value?.start_Agree_Time =
                    DateTimeUtils.formatter.format(millseconds)
                R.id.start1_1 -> viewModel.thrombolysis.value?.sign_Agree_Time =
                    DateTimeUtils.formatter.format(millseconds)
            }
        }else{
            viewModel.itemForChangeTime.value?.excuteTime=DateTimeUtils.formatter.format(millseconds)
            viewModel.itemForChangeTime.value=null
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

    private fun showPicker(record:DrugRecord){

            val currentTime= record.excuteTime.let { txt ->
                if (txt.isNullOrEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }
            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
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
        const val SELECT_PLACE = 50
        const val SELECT_DOCTOR = 51
        const val SELECT_DOCTORS = 52
    }
    private lateinit var viewModel: ThrombolysisViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityThrombolysisBinding
    lateinit var drugAdapter: ThrombolysisDrugsAdapter
    lateinit var goAdapter: GoAdapter

    private lateinit var placeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        goAdapter = GoAdapter(this)
        drugAdapter = ThrombolysisDrugsAdapter(this,viewModel)

        binding = DataBindingUtil.setContentView<ActivityThrombolysisBinding>(this, R.layout.activity_thrombolysis)
            .apply {
                medicalList.adapter = drugAdapter
                goList.adapter=goAdapter


                ohterIll.addTextChangedListener(object:TextWatcher{
                    override fun afterTextChanged(p0: Editable?) {
                        if(throughagain.isChecked){
                            goAdapter.setCurrent("241-1")
                        }else{
                            if(ohter_ill.text.toString().trim().isNotEmpty()){
                                goAdapter.setCurrent("241-3")
                            }else{
                                goAdapter.setCurrent("241-5")
                            }
                        }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                })
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
        setSupportActionBar(toolbar)
        title="溶栓"
        viewModel.informed.observe(this, Observer {  })



        viewModel.apply {
            thrombolysis.observe(this@ThrombolysisActivity, Observer {

                viewModel.drugs.addAll( it.drugRecords)
                drugAdapter.submitList(viewModel.drugs)
                if(goAdapter.current()==null)
                    goAdapter.setCurrent(it.direction_Code?:"")
            })

            thromPlaces.observe(this@ThrombolysisActivity, Observer {
                goAdapter.submitList(it)
                if(goAdapter.current()==null && thrombolysis.value!=null){
                    goAdapter.setCurrent(thrombolysis.value?.direction_Code?:"")
                }

            })
            aa.observe(this@ThrombolysisActivity, Observer {
                goAdapter.setCurrent(it)
            })
        }


        viewModel.clickLine.observe(this, Observer {
            when(it){
                "place" -> selectPlace()
                "informedConsent" ->{
                    if (viewModel.thrombolysis.value?.talkRecordId.isNullOrEmpty()){
                        toInformedConsent()
                    }else{
                        toSeeInformedConsent()
                    }
                }
                "drugs" -> toDrugs()
                "doctors" -> selectDoctors()
                "complication" -> toComplication()
                "refreshDrugs" -> {
                    drugAdapter.notifyDataSetChanged()
//                    drugAdapter.submitList(viewModel.drugs)
//                    viewModel.thrombolysis.value?.drugRecords = viewModel.drugs
                }
            }
        })

        viewModel.modifySome.observe(this, Observer {
            when(it){
                "HidenDialog" ->placeDialog.dismiss()
                "ModifyStartInformedTime" -> showDatePicker(findViewById(R.id.start1))
                "ModifyEndInformedTime" -> showDatePicker(findViewById(R.id.start1_1))
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

        viewModel.itemForChangeTime.observe(this, Observer {
            it ?: return@Observer
//            viewModel.itemForChangeTime.value=null
            showPicker(it)

        })

        viewModel.itemForChangeDoctor.observe(this, Observer {
            it ?: return@Observer
//            viewModel.itemForChangeDoctor.value=null
            selectDoctor()

        })



    }

    private fun selectPlace(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "ThromSelectPlace")
        }
        startActivityForResult(intent,SELECT_PLACE)

    }

    private fun selectDoctor(){
        val intent = Intent(this, SelectDoctorActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent, SELECT_DOCTOR)

    }

    private fun selectDoctors(){

        viewModel.thrombolysis.value?.let {
            val intent = Intent(this, SelectDoctorActivity::class.java).apply {
                putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
                putExtra("doctorIds",it.thromStaffs?.map { it.staffId }?.toTypedArray()?: emptyArray())
                putExtra("single",false)
            }
            startActivityForResult(intent, SELECT_DOCTORS)
        }

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
            putExtra(AddInformedActivity.TALK_ID,viewModel.thrombolysis.value?.talkRecordId)
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
        startActivityForResult(intent, COMPLICATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                INFORMED_CONSENT ->{//知情同意书
                    viewModel.thrombolysis.value?.talkRecordId = data?.getStringExtra("informedConsentId")?:""
                    viewModel.thrombolysis.value?.start_Agree_Time = data?.getStringExtra("startTime")?:""
                    viewModel.thrombolysis.value?.sign_Agree_Time = data?.getStringExtra("endTime")?:""
                    viewModel.thrombolysis.value?.allTime = data?.getStringExtra("allTime")?:""

                }
                DRUG ->{//用药
                    var arr =  viewModel.thrombolysis.value?.drugRecords?.map { it }?: emptyList()
                    val drugs = data?.getSerializableExtra("selectedDrugs") as ArrayList<Drug>
                    val dlist = drugs.map { DrugRecord(it.id).apply {
                        drugName = it.name
                        doseString = it.dose.toString()
                        doseUnit = it.doseUnit
                        drugId = it.id
                    } }as ArrayList<DrugRecord>
                    dlist.addAll(arr.filter { !dlist.contains(it) })
                    viewModel.thrombolysis.value?.drugRecords = dlist
                    viewModel.drugs = dlist
                    drugAdapter.submitList(dlist)
                }
                COMPLICATION ->{//并发症
                    val otherIlls = data?.getStringExtra("otherIlls")
                    binding.ohterIll.text = otherIlls
                }
                SELECT_PLACE ->{//溶栓场所
                    val place = data?.getSerializableExtra("SelectOne") as Dictionary
                    viewModel.thrombolysis.value?.thromTreatmentPlaceName = place.itemName
                    viewModel.thrombolysis.value?.throm_Treatment_Place = place.id
                }

                SELECT_DOCTOR ->{//溶栓场所
                    val doctor = data?.getParcelableExtra<EntityIdName>("doctor") //as EntityIdName

                    viewModel.itemForChangeDoctor.value?.staffName = doctor?.name
                    viewModel.itemForChangeDoctor.value?.staffId = doctor?.id
                }

                SELECT_DOCTORS ->{//溶栓场所
                    val doctors = data?.getParcelableArrayListExtra<EntityIdName>("doctors") //as EntityIdName

                    viewModel.thrombolysis.value?.let {
                        it.thromStaffs = doctors?.map {doc-> ThromboStaff(thromId = it.id?:"",staffId =doc.id,staffName = doc.name ) }
                    }
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
                goAdapter.current()?.let {
                    viewModel.thrombolysis.value?.apply {
                        direction_Code=it.id
                    }
                }
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
