package com.wxsoft.fcare.ui.main.fragment.patients

import android.app.Activity
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
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.PatientEmrActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_patients.*
import javax.inject.Inject

class PatientsFragment : DaggerFragment() , SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return viewModel.showPatients(if(p0.isNullOrEmpty())"" else p0)
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0.isNullOrEmpty()){
            viewModel.showPatients("")
            return true
        }
        return false
    }

    private lateinit var viewModel: PatientsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: PatientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= PatientsAdapter(this,viewModel)
        viewModel.showPatients("")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding= FragmentPatientsBinding.inflate(inflater, container, false).apply {
            list.adapter=adapter
            viewModel=this@PatientsFragment.viewModel

            search.setOnQueryTextListener(this@PatientsFragment)
            floatingActionButton.setOnClickListener {
                val intent = Intent(activity!!, ProfileActivity::class.java)
                startActivityForResult(intent, BaseActivity.NEW_PATIENT_REQUEST)
            }
            lifecycleOwner = this@PatientsFragment

        }
        viewModel.patients.observe(this, Observer { it->
            adapter.submitList(it)
        })
        viewModel.detailAction.observe(this, EventObserver{
                t->

            toDetail(t)
        })

        return binding.root
    }

    private fun toDetail(id:String) {

        Intent(activity!!, PatientEmrActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,id)
            startActivity(it)
        }

    }

    override fun onStop() {
        search.clearFocus()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                BaseActivity.NEW_PATIENT_REQUEST -> {

                    viewModel.showPatients(search.query.toString())
                }
            }
        }
    }
}
