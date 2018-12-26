package com.wxsoft.fcare.ui.main.fragment.patients

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.databinding.FragmentPatientsBinding
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.result.EventObserver
import com.wxsoft.fcare.ui.detail.PatientDetailActivity
import com.wxsoft.fcare.ui.income.InComeActivity
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_patients.*
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

        binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            setLifecycleOwner (this@PatientsFragment)

        }

        binding.floatingActionButton.setOnClickListener { toDetail("") }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= PatientsAdapter(this,viewModel)
        binding.viewModel=viewModel

        list.adapter=adapter
        viewModel.patients.observe(this, Observer { it->adapter.patients=it?: emptyList() })
        viewModel.navigateToOperationAction.observe(this, EventObserver{ t->

            toDetail(t)
        })
    }


    fun toDetail(id:String) {

        var intent = Intent(activity!!, PatientDetailActivity::class.java)
        intent.putExtra(PatientDetailActivity.PATIENT_ID, id)
        startActivity(intent)
//        if (id.isEmpty()) {
//
//            var intent = Intent(activity!!, InComeActivity::class.java)
//            startActivity(intent)
//        }else{
//
//
//
//        }
    }
}
