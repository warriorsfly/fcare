package com.wxsoft.emergency.ui.main.fragment.patients

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.FragmentPatientsBinding
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PatientsFragment : DaggerFragment() {

    private lateinit var viewModel: PatientsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: PatientsAdapter

    lateinit var binding: FragmentPatientsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(viewModelFactory)
        adapter= PatientsAdapter(this,viewModel)
        binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            list.adapter=adapter
            viewModel=this@PatientsFragment.viewModel
            floatingActionButton.setOnClickListener { toDetail("") }
            setLifecycleOwner (this@PatientsFragment)

        }
//        viewModel.patients.observe(this, Observer { it->adapter.currentList =it?: emptyList() })
        viewModel.mesAction.observe(this, EventObserver{
                t->

            toDetail(t)
        })
        return binding.root
    }

    fun toDetail(id:String) {

//        var intent = Intent(activity!!, PatientDetailActivity::class.java)
//        intent.putExtra(PatientDetailActivity.PATIENT_ID, id)
//        startActivity(intent)
    }
}
