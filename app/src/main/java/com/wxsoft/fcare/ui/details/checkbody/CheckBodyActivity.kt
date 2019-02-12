package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityCheckBodyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class CheckBodyActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: CheckBodyViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityCheckBodyBinding

    lateinit var adapter: CheckBodyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityCheckBodyBinding>(this, R.layout.activity_check_body)
            .apply {
                lifecycleOwner = this@CheckBodyActivity
            }
        patientId=intent.getStringExtra(CheckBodyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        viewModel.loadItems()
        viewModel.loadCheckBody()
        back.setOnClickListener { onBackPressed() }

        adapter = CheckBodyAdapter(this,viewModel)
        binding.checkList.adapter = adapter
        viewModel.checkBody.observe(this, Observer {  })

        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent);
                finish();
            }
        })
    }






}
