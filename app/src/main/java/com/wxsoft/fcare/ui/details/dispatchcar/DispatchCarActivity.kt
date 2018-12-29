package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.DoMinaActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var carAdapter: CarAdapter

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("选择车辆")

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_dispatch_car)
            .apply {
                setLifecycleOwner(this@DispatchCarActivity)
            }
        binding.viewModel = viewModel
        binding.listener = viewModel

//        viewModel.taskAction.observe(this, EventObserver{ t->
//            toDetail(t)
//        })
        carAdapter = CarAdapter(this,viewModel)
        viewModel.doctors.observe(this, Observer { it -> carAdapter.doctors = it ?: emptyList() })

        binding.doctorList.adapter = carAdapter

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun toDetail(id:String) {

        var intent = Intent(this, DoMinaActivity::class.java)
        intent.putExtra(DoMinaActivity.TASK_ID, id)
        startActivity(intent)
    }


}
