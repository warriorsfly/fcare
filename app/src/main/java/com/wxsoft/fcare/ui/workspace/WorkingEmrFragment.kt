package com.wxsoft.fcare.ui.workspace

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.FragmentEmrBinding
import com.wxsoft.fcare.databinding.FragmentWorkingEmrBinding
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.emr.EmrAdapter
import com.wxsoft.fcare.ui.emr.EmrViewModel
import com.wxsoft.fcare.ui.workspace.WorkingActivity.Companion.VITAL_SIGNS
import com.wxsoft.fcare.utils.ActionType
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Named

class WorkingEmrFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: FragmentWorkingEmrBinding

    private lateinit var viewModel: EmrViewModel

    private lateinit var adapter: EmrAdapter

    var patientId=""
    set(value) {
        field=value
        viewModel.patientId=field
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel=activityViewModelProvider(factory)
        adapter= EmrAdapter(this@WorkingEmrFragment,::clickItem)
        viewModel.emrs.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.emrItemLoaded.observe(this,EventObserver{
            adapter.notifyItemChanged(it)
        })

        return  FragmentWorkingEmrBinding
            .inflate(inflater,container, false).apply {
                list.adapter=this@WorkingEmrFragment.adapter
            lifecycleOwner=this@WorkingEmrFragment
        }.root

    }

    private fun clickItem(code:String){
        when(code){
            ActionType.生命体征 -> {
                val intent = Intent(activity, VitalSignsRecordActivity::class.java).apply {
                    putExtra(VitalSignsRecordActivity.PATIENT_ID, viewModel.patientId)
                }
                startActivityForResult(intent, VITAL_SIGNS)
            }
        }
    }
}
