package com.wxsoft.fcare.ui.details.complication

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComplicationBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class ComplicationActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: ComplicationViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var adapter: ComplicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complication)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityComplicationBinding>(this, R.layout.activity_complication)
            .apply {
                viewModel = this@ComplicationActivity.viewModel
                adapter = ComplicationAdapter(this@ComplicationActivity,this@ComplicationActivity.viewModel)
                list.adapter = adapter
                lifecycleOwner = this@ComplicationActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel.patientId = patientId

        viewModel.items.observe(this, Observer {
            adapter.items = it
        })


        back.setOnClickListener { onBackPressed() }
    }
}
