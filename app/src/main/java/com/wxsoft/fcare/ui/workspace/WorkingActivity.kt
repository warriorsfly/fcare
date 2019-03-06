package com.wxsoft.fcare.ui.workspace

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
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
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

                timeLine.setOnClickListener {
                    val intent = Intent(this@WorkingActivity, TimePointActivity::class.java)
                        .apply {
                            putExtra(TimePointActivity.PATIENT_ID, patientId)
                        }
                    startActivityForResult(intent, TimePointActivity.BASE_INFO)
                }
            }


    }


    private fun doOperation(operation:WorkOperation){
        when(operation.actionCode){
            ActionRes.ActionType.GRACE->{
                val intent = Intent(this, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, EmrFragment.RATING)
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
        }
    }

}
