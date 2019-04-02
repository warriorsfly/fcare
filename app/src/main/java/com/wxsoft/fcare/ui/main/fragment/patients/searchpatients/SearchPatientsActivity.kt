package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySearchPatientsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import javax.inject.Inject

class SearchPatientsActivity : BaseActivity() , SearchView.OnQueryTextListener{

    private lateinit var viewModel: SearchPatientsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySearchPatientsBinding
    lateinit var adapter: SearchPatientsAdapter


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySearchPatientsBinding>(this, R.layout.activity_search_patients)
            .apply {
                search.setOnQueryTextListener(this@SearchPatientsActivity)
                back.setOnClickListener { onBackPressed() }
                lifecycleOwner = this@SearchPatientsActivity
            }
        adapter= SearchPatientsAdapter(this,viewModel)
        binding.list.adapter = adapter

        viewModel.patients.observe(this, Observer {
            adapter.items = it
        })

        viewModel.detailAction.observe(this, EventObserver{
                t->
            toDetail(t)
        })

    }


    private fun toDetail(id:String) {

        Intent(this@SearchPatientsActivity, WorkingActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,id)
            it.putExtra("PRE",false)
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }

    }
}
