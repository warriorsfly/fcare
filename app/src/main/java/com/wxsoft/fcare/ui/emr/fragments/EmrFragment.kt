package com.wxsoft.fcare.ui.emr.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.databinding.ActivityWorkingEmrBinding
import com.wxsoft.fcare.databinding.FragmentWorkingEmrBinding
import com.wxsoft.fcare.ui.BaseFragment
import com.wxsoft.fcare.ui.details.assistant.AssistantExaminationActivity
import com.wxsoft.fcare.ui.details.assistant.troponin.JGDBActivity
import com.wxsoft.fcare.ui.details.catheter.CatheterActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import com.wxsoft.fcare.ui.details.comingby.ComingByActivity
import com.wxsoft.fcare.ui.details.complaints.ComplaintsActivity
import com.wxsoft.fcare.ui.details.ct.CTActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity
import com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug.ACSDrugActivity
import com.wxsoft.fcare.ui.details.ecg.EcgActivity
import com.wxsoft.fcare.ui.details.informedconsent.InformedConsentActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.medicalhistory.MedicalHistoryActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.reperfusion.ReperfusionActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyActivity
import com.wxsoft.fcare.ui.details.thrombolysis.ThrombolysisActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.discharge.DisChargeActivity
import com.wxsoft.fcare.ui.emr.EmrAdapter
import com.wxsoft.fcare.ui.emr.EmrViewModel
import com.wxsoft.fcare.ui.outcome.OutComeActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.ui.workspace.notify.OneTouchCallingActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.BASE_INFO
import com.wxsoft.fcare.utils.ActionCode.Companion.CABG
import com.wxsoft.fcare.utils.ActionCode.Companion.CHECK_BODY
import com.wxsoft.fcare.utils.ActionCode.Companion.COMEBY
import com.wxsoft.fcare.utils.ActionCode.Companion.COMPLAINTS
import com.wxsoft.fcare.utils.ActionCode.Companion.CT_OPERATION
import com.wxsoft.fcare.utils.ActionCode.Companion.Catheter
import com.wxsoft.fcare.utils.ActionCode.Companion.DIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.DRUGRECORD
import com.wxsoft.fcare.utils.ActionCode.Companion.ECG
import com.wxsoft.fcare.utils.ActionCode.Companion.INFORMEDCONSENT
import com.wxsoft.fcare.utils.ActionCode.Companion.MEASURES
import com.wxsoft.fcare.utils.ActionCode.Companion.MEDICAL_HISTORY_CODE
import com.wxsoft.fcare.utils.ActionCode.Companion.ONETOUCH
import com.wxsoft.fcare.utils.ActionCode.Companion.OTDIAGNOSE
import com.wxsoft.fcare.utils.ActionCode.Companion.OUTCOME
import com.wxsoft.fcare.utils.ActionCode.Companion.RATING
import com.wxsoft.fcare.utils.ActionCode.Companion.RIS_LIS
import com.wxsoft.fcare.utils.ActionCode.Companion.STRATEGY
import com.wxsoft.fcare.utils.ActionCode.Companion.THROMBOLYSIS
import com.wxsoft.fcare.utils.ActionCode.Companion.VITAL_SIGNS
import com.wxsoft.fcare.utils.ActionType
import javax.inject.Inject

class EmrFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        emrViewModel=activityViewModelProvider(factory)
        emrViewModel.patientId=patientId
        adapter= EmrAdapter(this,::clickItem)
        emrViewModel.emrs.observe(this, Observer {
            adapter.submitList(it)
        })

        emrViewModel.emrItemLoaded.observe(this,EventObserver{
            adapter.notifyItemChanged(it)
        })
        return ActivityWorkingEmrBinding.inflate(inflater,container, false).apply {
            list.adapter=this@EmrFragment.adapter
        }.root

    }


    fun refresh(){
        adapter.notifyDataSetChanged()
    }

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentWorkingEmrBinding
    private lateinit var emrViewModel: EmrViewModel
    private lateinit var adapter: EmrAdapter
//    private lateinit var headerAdapter: EmrAdapter

    private val patientId: String by lazyFast {
        activity?.intent?.getStringExtra("PATIENT_ID")?:""
    }


    private fun clickItem(code:String){

        val act =when(code) {
            ActionType.患者信息录入 -> Pair(ProfileActivity::class.java, BASE_INFO)
            ActionType.GRACE -> Pair(RatingActivity::class.java, RATING)
            ActionType.生命体征 -> Pair(VitalSignsActivity::class.java, VITAL_SIGNS)
            ActionType.PhysicalExamination -> Pair(CheckBodyActivity::class.java, CHECK_BODY)
            ActionType.IllnessHistory -> Pair(MedicalHistoryActivity::class.java, MEDICAL_HISTORY_CODE)
            ActionType.DispostionMeasures -> Pair(MeasuresActivity::class.java, MEASURES)
            ActionType.给药 -> Pair(DrugRecordsActivity::class.java, DRUGRECORD)
            ActionType.ACS给药 -> Pair(ACSDrugActivity::class.java, DRUGRECORD)
            ActionType.知情同意书 -> Pair(InformedConsentActivity::class.java, INFORMEDCONSENT)
            ActionType.诊断 -> Pair(DiagnoseActivity::class.java, DIAGNOSE)
            ActionType.溶栓处置 -> Pair(ThrombolysisActivity::class.java, THROMBOLYSIS)
            ActionType.Catheter -> Pair(CatheterActivity::class.java, Catheter)
            ActionType.出院诊断 -> Pair(DisChargeActivity::class.java, OTDIAGNOSE)
            ActionType.辅助检查 -> Pair(AssistantExaminationActivity::class.java,RIS_LIS)
            ActionType.患者转归 -> Pair(OutComeActivity::class.java, OUTCOME)
            ActionType.主诉及症状 -> Pair(ComplaintsActivity::class.java, COMPLAINTS)
            ActionType.CT_OPERATION -> Pair(CTActivity::class.java, CT_OPERATION)
            ActionType.CABG -> Pair(ReperfusionActivity::class.java, CABG)
            ActionType.治疗策略 -> Pair(StrategyActivity::class.java, STRATEGY)
            ActionType.心电图 -> Pair(EcgActivity::class.java, ECG)
            ActionType.一键通知 ->Pair(OneTouchCallingActivity::class.java, ONETOUCH)
            ActionType.来院方式 ->Pair(ComingByActivity::class.java, COMEBY)
            ActionType.肌钙蛋白 ->Pair(JGDBActivity::class.java, ONETOUCH)
            else -> throw IllegalArgumentException("unknown code $code")
        }
        val intent = Intent(activity, act.first).apply {
            putExtra("PATIENT_ID", emrViewModel.patientId)
            when(code){
                ActionType.溶栓处置->putExtra(ThrombolysisActivity.COME_FROM, "1")
            }
        }
        startActivityForResult(intent, act.second)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            emrViewModel.refresh(requestCode)
        }
    }
}
