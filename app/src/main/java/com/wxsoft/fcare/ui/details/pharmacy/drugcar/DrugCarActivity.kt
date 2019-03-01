package com.wxsoft.fcare.ui.details.pharmacy.drugcar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDrugCarBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.selectdrugs.SelectDrugsActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DrugCarActivity : BaseActivity() , View.OnClickListener {


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val AddDrugs = 1
    }

    private lateinit var viewModel: DrugCarViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDrugCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDrugCarBinding>(this, R.layout.activity_drug_car)
            .apply {
                lifecycleOwner = this@DrugCarActivity
            }
        patientId=intent.getStringExtra(DrugCarActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }


        viewModel.clikSomething.observe(this, Observer {
            when(it){

            }
        })

        binding.selectDrugbags.setOnClickListener(this)
        binding.selectDrugs.setOnClickListener(this)
        binding.submitContainer.setOnClickListener(this)
        binding.submitAllDrugs.setOnClickListener(this)



    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.select_drugbags -> clickSelectDrugBags()
            R.id.select_drugs -> clickSelectDrugs()
            R.id.submit_container -> clicSubmit()
            R.id.submit_all_drugs -> clicSubmit()
        }
    }

    fun clickSelectDrugBags(){//快速选择药包

    }

    fun clickSelectDrugs(){//药品列表选择药品
        val intent = Intent(this, SelectDrugsActivity::class.java).apply {
            putExtra(SelectDrugsActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent,AddDrugs)
    }

    fun clicSubmit(){//提交

    }

}
