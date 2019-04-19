package com.wxsoft.fcare.ui.details.pharmacy.selectdrugs

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelectDrugsBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_select_drugs.*
import javax.inject.Inject

class SelectDrugsActivity : BaseActivity() {


    private lateinit var patientId:String
    private lateinit var comeFrom:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val COME_FROM = "COME_FROM"
    }

    private lateinit var viewModel: SelectDrugsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySelectDrugsBinding

    private lateinit var typeAdapter: DrugTypesListAdapter
    private lateinit var drugAdapter: DrugsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySelectDrugsBinding>(this, R.layout.activity_select_drugs)
            .apply {
                lifecycleOwner = this@SelectDrugsActivity
            }
        patientId=intent.getStringExtra(SelectDrugsActivity.PATIENT_ID)?:""
        comeFrom=intent.getStringExtra(SelectDrugsActivity.COME_FROM)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }
        typeAdapter = DrugTypesListAdapter(this,viewModel)
        binding.drugTypesList.adapter = typeAdapter

        drugAdapter = DrugsListAdapter(this,viewModel)
        binding.drugsList.adapter = drugAdapter

        viewModel.drugs.observe(this, Observer {
            typeAdapter.items = it
        })

        viewModel.selectTypeDrugs.observe(this, Observer {
            drugAdapter.items = it
        })

        viewModel.sumit.observe(this, Observer {
            Intent().let { intent->
                val bundle = Bundle()
                bundle.putSerializable("selectedDrugs",viewModel.selectedDrugs)
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }
}
