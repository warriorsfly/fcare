package com.wxsoft.fcare.ui.workspace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentOtherOperationListBinding
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.comingby.ComingByActivity
import com.wxsoft.fcare.ui.details.complaints.ComplaintsActivity
import com.wxsoft.fcare.ui.details.ct.CTActivity
import com.wxsoft.fcare.ui.details.diagnose.record.DiagnoseRecordActivity
import com.wxsoft.fcare.ui.details.ecg.EcgActivity
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
import com.wxsoft.fcare.utils.ActionCode.Companion.BASE_INFO
import com.wxsoft.fcare.utils.ActionCode.Companion.CABG
import com.wxsoft.fcare.utils.ActionCode.Companion.CHECK_BODY
import com.wxsoft.fcare.utils.ActionCode.Companion.COMEBY
import com.wxsoft.fcare.utils.ActionCode.Companion.COMPLAINTS
import com.wxsoft.fcare.utils.ActionCode.Companion.CT_OPERATION
import com.wxsoft.fcare.utils.ActionCode.Companion.Catheter
import com.wxsoft.fcare.utils.ActionCode.Companion.DIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.DRUGRECORD
import com.wxsoft.fcare.utils.ActionCode.Companion.INFORMEDCONSENT
import com.wxsoft.fcare.utils.ActionCode.Companion.MEASURES
import com.wxsoft.fcare.utils.ActionCode.Companion.MEDICAL_HISTORY_CODE
import com.wxsoft.fcare.utils.ActionCode.Companion.OTDIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.OUTCOME
import com.wxsoft.fcare.utils.ActionCode.Companion.RATING
import com.wxsoft.fcare.utils.ActionCode.Companion.STRATEGY
import com.wxsoft.fcare.utils.ActionCode.Companion.THROMBOLYSIS
import com.wxsoft.fcare.utils.ActionCode.Companion.VITAL_SIGNS
import com.wxsoft.fcare.utils.ActionType
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OtherOperationFragment : DaggerFragment() {


    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentOtherOperationListBinding
    private lateinit var viewModel: WorkingViewModel
    var patientId=""
    private lateinit var adapter: OperationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel=activityViewModelProvider(factory)
        adapter= OperationAdapter(this@OtherOperationFragment,::doOperation,true)
        viewModel.otherOperations.observe(this, Observer {
            adapter.submitList(it)
        })
        return  FragmentOtherOperationListBinding
            .inflate(inflater,container, false).apply {
                list.adapter = this@OtherOperationFragment.adapter

                lifecycleOwner = this@OtherOperationFragment
            }.root

    }

    private fun doOperation(operation: WorkOperation){
        when(operation.actionCode){

            ActionType.心电图 ->{
                val intent = Intent(activity, EcgActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                activity?.startActivityForResult(intent, RATING)
            }
            ActionType.GRACE->{
                val intent = Intent(activity, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                activity?.startActivityForResult(intent, RATING)
            }
            ActionType.给药 ->{
                val intent = Intent(activity, DrugRecordsActivity::class.java).apply {
                    putExtra(DrugRecordsActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, DRUGRECORD)
            }
            ActionType.生命体征 -> {
                val intent = Intent(activity, VitalSignsRecordActivity::class.java).apply {
                    putExtra(VitalSignsRecordActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, VITAL_SIGNS)
            }
            ActionType.患者信息录入->{
                val intent = Intent(activity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, BASE_INFO)
            }
            ActionType.主诉及症状 ->{
                val intent = Intent(activity, ComplaintsActivity::class.java).apply {
                    putExtra(ComplaintsActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, COMPLAINTS)
            }
            ActionType.PhysicalExamination->{
                val intent = Intent(activity, CheckBodyActivity::class.java).apply {
                    putExtra(CheckBodyActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, CHECK_BODY)
            }
            ActionType.IllnessHistory->{
                val intent = Intent(activity, MedicalHistoryActivity::class.java).apply {
                    putExtra(MedicalHistoryActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, MEDICAL_HISTORY_CODE)
            }
            ActionType.辅助检查 ->{
                val intent = Intent(activity, AssistantExaminationActivity::class.java).apply {
                    putExtra(AssistantExaminationActivity.PATIENT_ID, patientId)
                }
                startActivity(intent)
            }
            ActionType.诊断 ->{//多条 需要记录界面
                val intent = Intent(activity, DiagnoseRecordActivity::class.java).apply {
                    putExtra(DiagnoseRecordActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, DIAGNOSE)
            }
            ActionType.治疗策略 ->{
                val intent = Intent(activity, StrategyActivity::class.java).apply {
                    putExtra(StrategyActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, STRATEGY)
            }
            ActionType.DispostionMeasures->{
                val intent = Intent(activity, MeasuresActivity::class.java).apply {
                    putExtra(MeasuresActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, MEASURES)
            }
            ActionType.知情同意书 ->{
                val intent = Intent(activity, InformedConsentActivity::class.java).apply {
                    putExtra(InformedConsentActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, INFORMEDCONSENT)
            }
            ActionType.溶栓处置 ->{
                val intent = Intent(activity, ThrombolysisActivity::class.java).apply {
                    putExtra(ThrombolysisActivity.PATIENT_ID, patientId)
                    putExtra(ThrombolysisActivity.COME_FROM, "1")
                }
                activity?.startActivityForResult(intent, THROMBOLYSIS)
            }
            ActionType.Catheter ->{
                val intent = Intent(activity, CatheterActivity ::class.java).apply {
                    putExtra(CatheterActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, Catheter)
            }
            ActionType.CT_OPERATION ->{
                val intent = Intent(activity, CTActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, CT_OPERATION)
            }
            ActionType.CABG ->{
                val intent = Intent(activity, ReperfusionActivity::class.java).apply {
                    putExtra(ReperfusionActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, CABG)
            }
            ActionType.出院诊断 ->{
                val intent = Intent(activity, DisChargeActivity::class.java).apply {
                    putExtra(CTActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, OTDIAGNOSE)
            }
            ActionType.患者转归 ->{
                val intent = Intent(activity, OutComeActivity::class.java).apply {
                    putExtra(OutComeActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, OUTCOME)
            }

            ActionType.来院方式 ->{
                val intent = Intent(activity, ComingByActivity::class.java).apply {
                    putExtra(ComingByActivity.PATIENT_ID, patientId)
                }
                activity?.startActivityForResult(intent, COMEBY)
            }
        }
    }
}
