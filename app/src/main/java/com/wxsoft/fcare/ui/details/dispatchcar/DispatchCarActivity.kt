package com.wxsoft.fcare.ui.details.dispatchcar

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityDispatchCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_dispatch_car.*
import javax.inject.Inject

class DispatchCarActivity : BaseActivity() {

    private lateinit var viewModel: DispatchCarViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: ActivityDispatchCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_car)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("选择车辆")

        binding = DataBindingUtil.setContentView<ActivityDispatchCarBinding>(this, R.layout.activity_main)
            .apply {
                setLifecycleOwner(this@DispatchCarActivity)
            }
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
