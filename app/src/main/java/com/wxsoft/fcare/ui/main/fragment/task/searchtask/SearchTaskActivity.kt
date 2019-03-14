package com.wxsoft.fcare.ui.main.fragment.task.searchtask

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySearchTaskBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.ui.main.fragment.task.TaskAdapter
import com.wxsoft.fcare.ui.main.fragment.task.TaskViewModel
import com.wxsoft.fcare.ui.patient.ProfileActivity
import javax.inject.Inject

class SearchTaskActivity : BaseActivity() , SearchView.OnQueryTextListener{

    private lateinit var viewModel: TaskViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySearchTaskBinding
    lateinit var adapter: TaskAdapter


    override fun onQueryTextSubmit(p0: String?): Boolean {
        return viewModel.searchTasks(if(p0.isNullOrEmpty())"" else p0)
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0.isNullOrEmpty()){
            viewModel.searchTasks("")
            return true
        }
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySearchTaskBinding>(this, R.layout.activity_search_task)
            .apply {
                search.setOnQueryTextListener(this@SearchTaskActivity)
                back.setOnClickListener { onBackPressed() }
                lifecycleOwner = this@SearchTaskActivity
            }
        adapter= TaskAdapter(this,viewModel)
        binding.list.adapter = adapter

        viewModel.searchTasks.observe(this, Observer {

            adapter.submitList(it ?: emptyList())
        })

        viewModel.navigateToOperationAction.observe(this, EventObserver {
            toDetail(it)
        })

    }


    private fun toDetail(id:String) {

        Intent(this@SearchTaskActivity, DoMinaActivity::class.java).let {
            it.putExtra(DoMinaActivity.TASK_ID,id)
            startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
        }

    }
}
