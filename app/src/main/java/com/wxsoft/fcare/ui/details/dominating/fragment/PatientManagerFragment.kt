package com.wxsoft.fcare.ui.details.dominating.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentPatientManagerBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PatientManagerFragment : DaggerFragment() {


    @Inject lateinit var factory: ViewModelFactory

    lateinit var viewModel:DoMinaViewModel
    private lateinit var adapter: PatientInTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=activityViewModelProvider(factory)
        adapter=PatientInTaskAdapter(this@PatientManagerFragment,::showPatient)
        viewModel.task.observe(this, Observer {
            adapter.patients=it.patients.toList()
        })
        return FragmentPatientManagerBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = this@PatientManagerFragment
            viewModel=this@PatientManagerFragment.viewModel

            list.adapter=adapter
            goTo.setOnClickListener {
                val intent = Intent(activity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, viewModel?.taskId)
                }
                activity?.startActivityForResult(intent, BaseActivity.NEW_PATIENT_REQUEST)

            }
        }.root
    }

    private fun showPatient(patient: Patient){

        Intent(activity, WorkingActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,patient.id)
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }
    }

}
