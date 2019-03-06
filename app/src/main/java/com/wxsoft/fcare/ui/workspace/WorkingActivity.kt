package com.wxsoft.fcare.ui.workspace

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.databinding.ActivityWorkingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.complaints.ComplaintsActivity
import com.wxsoft.fcare.ui.details.ct.CTActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.reperfusion.ReperfusionActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.discharge.DisChargeActivity
import com.wxsoft.fcare.ui.outcome.OutComeActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import kotlinx.android.synthetic.main.activity_working.*
import javax.inject.Inject

class WorkingActivity : BaseActivity() {


    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ARG_NEW_ITEM_CODE = 20
        const val MEDICAL_HISTORY_CODE = 21
        const val VITAL_SIGNS = 22
        const val CHECK_BODY = 23
        const val DIAGNOSE = 24
        const val MEASURES = 25
        const val INV = 26
        const val Catheter = 27
        const val CT = 28
        const val DISCHARGE = 29
        const val OUTCOME = 30
        const val INFORMEDCONSENT = 31
        const val THROMBOLYSIS = 32
        const val DRUGRECORD = 33
        const val OTDIAGNOSE = 34
        const val CT_OPERATION = 35
        const val RATING = 36
        const val CABG = 37
        const val BASE_INFO = 38
        const val COMPLAINTS = 39
        const val STRATEGY = 40
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

    private lateinit var viewModel: WorkingViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityWorkingBinding>(this,R.layout.activity_working)
            .apply {
                quality.adapter=QualityAdapter(this@WorkingActivity)
                operationView.adapter=OperationAdapter(this@WorkingActivity,::doOperation)

                operationView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, _ ->

                    bottomSheetBehavior.peekHeight=root.height-bottom
                }
                bottomSheetBehavior=BottomSheetBehavior.from( emr_list.view)
                viewModel=this@WorkingActivity.viewModel.apply { patientId=this@WorkingActivity.patientId }
                lifecycleOwner=this@WorkingActivity
                viewModel?.qualities?.observe(this@WorkingActivity, Observer {
                    (quality.adapter as? QualityAdapter)?.submitList(it)
                })

                viewModel?.operations?.observe(this@WorkingActivity, Observer {
                    (operationView.adapter as? OperationAdapter)?.apply {
                        submitList(it)
                    }

                })
            }


    }


    private fun doOperation(operation:WorkOperation){
        when(operation.actionCode){
            ActionRes.ActionType.GRACE->{
                val intent = Intent(this, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, RATING)
            }
            ActionRes.ActionType.给药 ->{
                val intent = Intent(this@WorkingActivity, DrugRecordsActivity::class.java).apply {
                    putExtra(DrugRecordsActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, DRUGRECORD)
            }
            ActionRes.ActionType.生命体征 -> {
                val intent = Intent(this@WorkingActivity, VitalSignsRecordActivity::class.java).apply {
                    putExtra(VitalSignsRecordActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, VITAL_SIGNS)
            }
            ActionRes.ActionType.患者信息录入->{
                val intent = Intent(this@WorkingActivity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, BASE_INFO)
            }
            ActionRes.ActionType.主诉及症状 ->{
                val intent = Intent(this@WorkingActivity, ComplaintsActivity::class.java).apply {
                    putExtra(ComplaintsActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, COMPLAINTS)
            }
            ActionRes.ActionType.PhysicalExamination->{
                val intent = Intent(this@WorkingActivity, CheckBodyActivity::class.java).apply {
                    putExtra(CheckBodyActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CHECK_BODY)
            }
            ActionRes.ActionType.IllnessHistory->{
                val intent = Intent(this@WorkingActivity, MedicalHistoryActivity::class.java).apply {
                    putExtra(MedicalHistoryActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, MEDICAL_HISTORY_CODE)
            }
            ActionRes.ActionType.辅助检查 ->{
                val intent = Intent(this@WorkingActivity, AssistantExaminationActivity::class.java).apply {
                    putExtra(AssistantExaminationActivity.PATIENT_ID, patientId)
                }
                startActivity(intent)
            }
            ActionRes.ActionType.诊断 ->{//多条 需要记录界面
                val intent = Intent(this@WorkingActivity, DiagnoseActivity::class.java).apply {
                    putExtra(DiagnoseActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, DIAGNOSE)
            }
            ActionRes.ActionType.治疗策略 ->{
                val intent = Intent(this@WorkingActivity, StrategyActivity::class.java).apply {
                    putExtra(StrategyActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, STRATEGY)
            }
            ActionRes.ActionType.DispostionMeasures->{
                val intent = Intent(this@WorkingActivity, MeasuresActivity::class.java).apply {
                    putExtra(MeasuresActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, MEASURES)
            }
            ActionRes.ActionType.知情同意书 ->{
                val intent = Intent(this@WorkingActivity, InformedConsentActivity::class.java).apply {
                    putExtra(InformedConsentActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, INFORMEDCONSENT)
            }
            ActionRes.ActionType.通知启动CT室 ->{
                showDialog("通知启动CT室","CT室")
            }

            ActionRes.ActionType.通知启动导管室 ->{
                showDialog("通知启动导管室","导管室")
            }
            ActionRes.ActionType.溶栓处置 ->{
                val intent = Intent(this@WorkingActivity, ThrombolysisActivity::class.java).apply {
                    putExtra(ThrombolysisActivity.PATIENT_ID, patientId)
                    putExtra(ThrombolysisActivity.COME_FROM, "1")
                }
                startActivityForResult(intent, THROMBOLYSIS)
            }
            ActionRes.ActionType.Catheter ->{
                val intent = Intent(this@WorkingActivity, CatheterActivity ::class.java).apply {
                    putExtra(CatheterActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, Catheter)
            }
            ActionRes.ActionType.CT_OPERATION ->{
                val intent = Intent(this@WorkingActivity, CTActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CT_OPERATION)
            }
            ActionRes.ActionType.CABG ->{
                val intent = Intent(this@WorkingActivity, ReperfusionActivity::class.java).apply {
                    putExtra(ReperfusionActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, CABG)
            }
            ActionRes.ActionType.出院诊断 ->{
                val intent = Intent(this@WorkingActivity, DisChargeActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, OTDIAGNOSE)
            }
            ActionRes.ActionType.患者转归 ->{
                val intent = Intent(this@WorkingActivity, OutComeActivity::class.java).apply {
                    putExtra(OutComeActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, OUTCOME)
            }
        }
    }


    fun showDialog(message:String,type:String){
        AlertDialog.Builder(this@WorkingActivity,R.style.Theme_FCare_Dialog_Text)
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

}
