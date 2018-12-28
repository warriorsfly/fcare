package com.wxsoft.fcare.ui.details.dispatchcar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_car)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("选择车辆")

        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_dispatch_car)
            .apply {
                setLifecycleOwner(this@DispatchCarActivity)
            }
        viewModel=viewModelProvider(factory)
        binding.viewModel = viewModel

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
}
