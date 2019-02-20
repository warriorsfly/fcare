package com.wxsoft.fcare.ui.details.complaints

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComplaintsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class ComplaintsActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: ComplaintsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityComplaintsBinding

    lateinit var adapter: ComplaintsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityComplaintsBinding>(this, R.layout.activity_complaints)
            .apply {
                lifecycleOwner = this@ComplaintsActivity
            }
        patientId=intent.getStringExtra(MeasuresActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        binding.viewModel = viewModel

        adapter = ComplaintsAdapter(this,viewModel)
//        adapter.items =
        binding.complaintsList.adapter = adapter
        viewModel.complaintsItems.observe(this, Observer {
            adapter.items = it
        })

        back.setOnClickListener { onBackPressed() }

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        viewModel.saveResult.observe(this, Observer {
            when(it){
                "success" ->{
                    Intent().let { intent ->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })
    }
}
