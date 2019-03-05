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
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import kotlinx.android.synthetic.main.activity_working.*
import javax.inject.Inject

class WorkingActivity : BaseActivity() {


    companion object {
        const val PATIENT_ID = "PATIENT_ID"
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
                startActivityForResult(intent, EmrFragment.RATING)
            }
        }
    }

}
