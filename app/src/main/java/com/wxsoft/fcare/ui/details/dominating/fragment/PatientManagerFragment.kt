package com.wxsoft.fcare.ui.details.dominating.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.FragmentPatientManagerBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.ui.patient.ProfileActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PatientManagerFragment : DaggerFragment() {


    @Inject lateinit var factory: ViewModelFactory

    lateinit var viewModel:DoMinaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=activityViewModelProvider(factory)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentPatientManagerBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = this@PatientManagerFragment
            viewModel=this@PatientManagerFragment.viewModel
            image.setOnClickListener {
                val intent = Intent(activity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, viewModel?.taskId)
                }
                activity?.startActivityForResult(intent, BaseActivity.NEW_PATIENT_REQUEST)

            }
        }.root
    }

}
