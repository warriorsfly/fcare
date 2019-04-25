package com.wxsoft.fcare.ui.hardwaredata

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityHardwareDataBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.checkbody.CheckBodyActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class HardwareDataActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: HardwareDataViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var binding: ActivityHardwareDataBinding
    lateinit var adapter: HardwareAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityHardwareDataBinding>(this, R.layout.activity_hardware_data)
            .apply {
                viewModel = this@HardwareDataActivity.viewModel
                lifecycleOwner = this@HardwareDataActivity
            }
        patientId=intent.getStringExtra(CheckBodyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        adapter = HardwareAdapter(this,viewModel)
        binding.hardwareRv.adapter = adapter

        setSupportActionBar(toolbar)
        title="选择设备"

        viewModel.mindrayDevices.observe(this, Observer {
            adapter.submitList(it)
        })

    }
}
