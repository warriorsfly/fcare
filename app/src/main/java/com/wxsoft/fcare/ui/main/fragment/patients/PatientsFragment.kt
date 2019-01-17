package com.wxsoft.emergency.ui.main.fragment.patients

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.FragmentPatientsBinding
import com.wxsoft.fcare.ui.patient.PatientEmrActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.item_get_picture.view.*
import javax.inject.Inject

class PatientsFragment : DaggerFragment() , SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return viewModel.showPatients(p0?:"")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var viewModel: PatientsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: PatientsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(viewModelFactory)
        adapter= PatientsAdapter(this,viewModel)
        val binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            list.adapter=adapter
            viewModel=this@PatientsFragment.viewModel

            search.setOnQueryTextListener(this@PatientsFragment)
            floatingActionButton.setOnClickListener { toDetail("") }
            setLifecycleOwner (this@PatientsFragment)

        }
        viewModel.patients.observe(this, Observer { it->
            adapter.submitList(it)
        })
        viewModel.mesAction.observe(this, EventObserver{
                t->

            toDetail(t)
        })


        return binding.root
    }

    fun toDetail(id:String) {

        Intent(activity!!, PatientEmrActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,"d6bf2a1287a64cc1bad9691c46a31fd5")
            startActivity(it)
        }

//        var intent = Intent(activity!!, PatientDetailActivity::class.java)
//        intent.putExtra(PatientDetailActivity.PATIENT_ID, id)
//        startActivity(intent)
    }
}
