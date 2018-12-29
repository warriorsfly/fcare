package com.wxsoft.fcare.ui.details.dominating

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

class DoMinaActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel:DoMinaViewModel

    private lateinit var taskId:String

    companion object {
        const val TASK_ID = "TASK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = DataBindingUtil.setContentView<ActivityDoMinaBinding>(
            this,
            R.layout.activity_do_mina
        ).apply {
            setLifecycleOwner(this@DoMinaActivity)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        taskId=intent.getStringExtra(TASK_ID)?:""
        viewModel=viewModelProvider(factory)
        binding.viewModel=viewModel

        viewModel.taskId=taskId

        viewModel.taskAction.observe(this, EventObserver {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
    }


}
