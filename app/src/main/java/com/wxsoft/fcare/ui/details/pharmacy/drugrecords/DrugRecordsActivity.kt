package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDrugRecordsBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.informedconsent.informeddetails.InformedConsentDetailsActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugcar.DrugCarActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DrugRecordsActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val AddDrug = 1
    }

    private lateinit var viewModel: DrugRecordsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDrugRecordsBinding

    private lateinit var drugRecordsAdapter: DrugRecordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDrugRecordsBinding>(this, R.layout.activity_drug_records)
            .apply {
                lifecycleOwner = this@DrugRecordsActivity
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }

        drugRecordsAdapter = DrugRecordsAdapter(this,viewModel)
        binding.drugRecordsList.adapter = drugRecordsAdapter


        viewModel.clikSomething.observe(this, Observer {
            when(it){
                "add"->{//新增
                    val intent = Intent(this, DrugCarActivity::class.java).apply {
                        putExtra(InformedConsentDetailsActivity.PATIENT_ID, patientId)
                    }
                    startActivityForResult(intent,AddDrug)
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when (requestCode) {
                AddDrug->{

                }
            }
        }
    }
}
