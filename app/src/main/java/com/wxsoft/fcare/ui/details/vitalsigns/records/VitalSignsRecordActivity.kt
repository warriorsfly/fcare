package com.wxsoft.fcare.ui.details.vitalsigns.records

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityVitalSignsRecordBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class VitalSignsRecordActivity :  BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ADD_VITAL = 10

    }

    private lateinit var viewModel: VitalSignsRecordViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityVitalSignsRecordBinding
    lateinit var listAdapter: VitalRecordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityVitalSignsRecordBinding>(this, R.layout.activity_vital_signs_record)
            .apply {
                lifecycleOwner = this@VitalSignsRecordActivity
            }
        back.setOnClickListener { onBackPressed() }
        patientId=intent.getStringExtra(PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        listAdapter = VitalRecordListAdapter(this,viewModel)
        binding.vitalRecordsList.adapter = listAdapter

        viewModel.vitals.observe(this, Observer { listAdapter.items = it })


        viewModel.addvital.observe(this, Observer { toAddVital(it) })


    }



    fun toAddVital(typeId:String){
        val intent = Intent(this, VitalSignsActivity::class.java).apply {
            putExtra(VitalSignsActivity.PATIENT_ID, patientId)
            putExtra(VitalSignsActivity.TYPE_ID, typeId)
        }
        startActivityForResult(intent,ADD_VITAL )
    }
}
