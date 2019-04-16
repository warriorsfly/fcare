package com.wxsoft.fcare.ui.workspace

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityWorkingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.complaints.ComplaintsActivity
import com.wxsoft.fcare.ui.details.ct.CTActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.DiagnoseNewActivity
import com.wxsoft.fcare.ui.details.ecg.EcgActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.notification.NotificationActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.reperfusion.ReperfusionActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity.Companion.SHARE
import com.wxsoft.fcare.ui.discharge.DisChargeActivity
import com.wxsoft.fcare.ui.emr.EmrActivity
import com.wxsoft.fcare.ui.emr.EmrViewModel
import com.wxsoft.fcare.ui.outcome.OutComeActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.ui.workspace.notify.NotifyFragment
import com.wxsoft.fcare.ui.rating.RatingSubjectActivity
import com.wxsoft.fcare.ui.rating.RatingsSheetFragment
import com.wxsoft.fcare.ui.share.ShareItemListActivity
import com.wxsoft.fcare.utils.ActionCode
import com.wxsoft.fcare.utils.ActionCode.Companion.BASE_INFO
import com.wxsoft.fcare.utils.ActionCode.Companion.CABG
import com.wxsoft.fcare.utils.ActionCode.Companion.CHECK_BODY
import com.wxsoft.fcare.utils.ActionCode.Companion.COMPLAINTS
import com.wxsoft.fcare.utils.ActionCode.Companion.CT_OPERATION
import com.wxsoft.fcare.utils.ActionCode.Companion.Catheter
import com.wxsoft.fcare.utils.ActionCode.Companion.DIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.DRUGRECORD
import com.wxsoft.fcare.utils.ActionCode.Companion.EMR
import com.wxsoft.fcare.utils.ActionCode.Companion.INFORMEDCONSENT
import com.wxsoft.fcare.utils.ActionCode.Companion.MEASURES
import com.wxsoft.fcare.utils.ActionCode.Companion.MEDICAL_HISTORY_CODE
import com.wxsoft.fcare.utils.ActionCode.Companion.NOTIFICATION
import com.wxsoft.fcare.utils.ActionCode.Companion.OTDIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.OUTCOME
import com.wxsoft.fcare.utils.ActionCode.Companion.RATING
import com.wxsoft.fcare.utils.ActionCode.Companion.STRATEGY
import com.wxsoft.fcare.utils.ActionCode.Companion.THROMBOLYSIS
import com.wxsoft.fcare.utils.ActionCode.Companion.VITAL_SIGNS
import com.wxsoft.fcare.utils.ActionType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_working.*
import kotlinx.android.synthetic.main.item_diagnose_record_details_item.*
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class WorkingActivity : BaseActivity() {

//    private val ratingFragment by lazy{
//        RatingsSheetFragment(::newRating).apply {
//            this.patientId=this@WorkingActivity.patientId
//        }
//    }
//
//    private fun newRating(rating: Rating){
//        ratingFragment.dismiss()
//        val intent = Intent(this, RatingSubjectActivity::class.java).apply {
//            putExtra(RatingSubjectActivity.PATIENT_ID, patientId)
//            putExtra(RatingSubjectActivity.RATING_NAME, rating.name)
//            putExtra(RatingSubjectActivity.RATING_ID, rating.id)
//        }
//        startActivityForResult(intent, ActionCode.ARG_NEW_ITEM_CODE)
//    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(bottomSheetBehavior.state){
            BottomSheetBehavior.STATE_EXPANDED->bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        }
        when (item.itemId) {
            R.id.share -> {
                val intent = Intent(this, ShareItemListActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, SHARE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.rating -> {
                val intent = Intent(this, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, RATING)
                return@OnNavigationItemSelectedListener true
            }
            R.id.emr -> {
                val intent = Intent(this, EmrActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, EMR)
                return@OnNavigationItemSelectedListener true
            }
            R.id.more -> {
                when(bottomSheetBehavior.state){

                    BottomSheetBehavior.STATE_COLLAPSED,BottomSheetBehavior.STATE_HIDDEN->bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
                }
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

    private val pre: Boolean by lazyFast {
        intent?.getBooleanExtra("PRE",false)?:false
    }

    @Inject
    @field:Named("optionViewPool")
    lateinit var optionViewPool: RecyclerView.RecycledViewPool

    lateinit var viewModel: WorkingViewModel
    private lateinit var emrViewModel: EmrViewModel
//    lateinit var transition: TransitionDrawable
    @Inject
    lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        emrViewModel=viewModelProvider(factory)
        emrViewModel.patientId=patientId
        emrViewModel.preHos=pre
        DataBindingUtil.setContentView<ActivityWorkingBinding>(this,R.layout.activity_working)
            .apply {

                operationMenu.apply {
                    itemIconTintList=null
                    setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
                }
//                quality.adapter=QualityAdapter(this@WorkingActivity)
                operationView.addItemDecoration(DividerItemDecoration(this@WorkingActivity,DividerItemDecoration.VERTICAL))
                operationView.adapter=OperationGroupAdapter(this@WorkingActivity,optionViewPool,::doOperation)
                bottomSheetBehavior=BottomSheetBehavior.from( other_list.view)

                viewModel=this@WorkingActivity.viewModel.apply {
                    pre = this@WorkingActivity.pre
                    patientId=this@WorkingActivity.patientId
                }
                lifecycleOwner=this@WorkingActivity
//                viewModel?.qualities?.observe(this@WorkingActivity, Observer {
//                    (quality.adapter as? QualityAdapter)?.submitList(it)
//                })

                viewModel?.operations?.observe(this@WorkingActivity, Observer {
                    (operationView.adapter as? OperationGroupAdapter)?.apply {
                        submitList(it)
                    }

                })

                backAway.setOnClickListener {
                   onBackPressed()
                }

                timeLine.setOnClickListener {
                    val intent = Intent(this@WorkingActivity, TimePointActivity::class.java)
                        .apply {
                            putExtra(TimePointActivity.PATIENT_ID, patientId)
                        }
                    startActivityForResult(intent, TimePointActivity.BASE_INFO)
                }
                notification.setOnClickListener {
                    val intent = Intent(this@WorkingActivity, NotificationActivity::class.java).apply {
                        putExtra(NotificationActivity.PATIENT_ID, patientId)
                    }
                    startActivityForResult(intent, NOTIFICATION)
                }
            }

    }


    private fun doOperation(operation:WorkOperation){
        when(operation.actionCode){

            ActionType.心电图 ->{
                val intent = Intent(this, EcgActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, RATING)
            }
            ActionType.GRACE->{
                val intent = Intent(this, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, RATING)
            }
            ActionType.给药 ->{
                val intent = Intent(this@WorkingActivity, DrugRecordsActivity::class.java).apply {
                    putExtra(DrugRecordsActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, DRUGRECORD)
            }
            ActionType.生命体征 -> {
                val intent = Intent(this@WorkingActivity, VitalSignsRecordActivity::class.java).apply {
                    putExtra(VitalSignsRecordActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, VITAL_SIGNS)
            }
            ActionType.患者信息录入->{
                val intent = Intent(this@WorkingActivity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, BASE_INFO)
            }
            ActionType.主诉及症状 ->{
                val intent = Intent(this@WorkingActivity, ComplaintsActivity::class.java).apply {
                    putExtra(ComplaintsActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, COMPLAINTS)
            }
            ActionType.PhysicalExamination->{
                val intent = Intent(this@WorkingActivity, CheckBodyActivity::class.java).apply {
                    putExtra(CheckBodyActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CHECK_BODY)
            }
            ActionType.IllnessHistory->{
                val intent = Intent(this@WorkingActivity, MedicalHistoryActivity::class.java).apply {
                    putExtra(MedicalHistoryActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, MEDICAL_HISTORY_CODE)
            }
            ActionType.辅助检查 ->{
                val intent = Intent(this@WorkingActivity, AssistantExaminationActivity::class.java).apply {
                    putExtra(AssistantExaminationActivity.PATIENT_ID, patientId)
                }
                startActivity(intent)
            }
            ActionType.诊断 ->{
                val intent = Intent(this@WorkingActivity, DiagnoseNewActivity::class.java).apply {
                    putExtra(DiagnoseNewActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, DIAGNOSE)
            }
            ActionType.治疗策略 ->{
                val intent = Intent(this@WorkingActivity, StrategyActivity::class.java).apply {
                    putExtra(StrategyActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, STRATEGY)
            }
            ActionType.DispostionMeasures->{
                val intent = Intent(this@WorkingActivity, MeasuresActivity::class.java).apply {
                    putExtra(MeasuresActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, MEASURES)
            }
            ActionType.知情同意书 ->{
                val intent = Intent(this@WorkingActivity, InformedConsentActivity::class.java).apply {
                    putExtra(InformedConsentActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, INFORMEDCONSENT)
            }
            ActionType.溶栓处置 ->{
                val intent = Intent(this@WorkingActivity, ThrombolysisActivity::class.java).apply {
                    putExtra(ThrombolysisActivity.PATIENT_ID, patientId)
                    putExtra(ThrombolysisActivity.COME_FROM, "1")
                }
                startActivityForResult(intent, THROMBOLYSIS)
            }
            ActionType.Catheter ->{
                val intent = Intent(this@WorkingActivity, CatheterActivity ::class.java).apply {
                    putExtra(CatheterActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, Catheter)
            }
            ActionType.CT_OPERATION ->{
                val intent = Intent(this@WorkingActivity, CTActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CT_OPERATION)
            }
            ActionType.CABG ->{
                val intent = Intent(this@WorkingActivity, ReperfusionActivity::class.java).apply {
                    putExtra(ReperfusionActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CABG)
            }
            ActionType.出院诊断 ->{
                val intent = Intent(this@WorkingActivity, DisChargeActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, OTDIAGNOSE)
            }
            ActionType.患者转归 ->{
                val intent = Intent(this@WorkingActivity, OutComeActivity::class.java).apply {
                    putExtra(OutComeActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, OUTCOME)
            }
            ActionType.一键通知 ->{
                val dialog= NotifyFragment()
                dialog.show(supportFragmentManager, NotifyFragment.TAG)
            }
        }
    }


    class CallBack( context: WorkingActivity):BottomSheetBehavior.BottomSheetCallback() {
        private val activity = WeakReference(context)
        private var translated:Boolean=false
        private var lastState: Int = BottomSheetBehavior.STATE_COLLAPSED
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if(!translated){

                when(lastState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        translated=true
//                        activity.get()?.transition?.startTransition(500)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        translated=true
//                        activity.get()?.transition?.reverseTransition(500)
                    }
                }
            }
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {

            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    if(lastState==newState){
//                        activity.get()?.transition?.reverseTransition(250)
                    }else {
                        lastState = newState
                    }
                    translated=false
                    activity.get()?.viewModel?.emrFullScreen?.set(false)
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    if(lastState==newState){
//                        activity.get()?.transition?.startTransition(250)
                    }else {
                        lastState = newState
                    }
                    translated=false
                    activity.get()?.viewModel?.emrFullScreen?.set(true)
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {

            emrViewModel.refresh(requestCode)

            viewModel.patientId=patientId

        }
    }

    fun showDialog(message:String,type:String){
        AlertDialog.Builder(this@WorkingActivity,R.style.Theme_FCare_Dialog)
            .setMessage(message)
            .setPositiveButton("确定") { _, _ ->
                when(type){
                    "导管室" -> viewModel.commitNoticeInv()
                    "CT室" -> viewModel.commitNoticePacs()
                }
            }
            .setNegativeButton("取消") { _, _ ->

            }.show()
    }
//
//    override fun onBackPressed() {
//
//        if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_EXPANDED){
//            bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
//        }else {
//            super.onBackPressed()
//        }
//    }

}
