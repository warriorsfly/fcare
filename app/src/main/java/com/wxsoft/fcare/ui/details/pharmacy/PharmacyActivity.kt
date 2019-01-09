package com.wxsoft.fcare.ui.details.pharmacy


import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityPharmacyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class PharmacyActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: PharmacyViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityPharmacyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityPharmacyBinding>(this, R.layout.activity_pharmacy)
            .apply {
                setLifecycleOwner(this@PharmacyActivity)
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }
        var  bagAdapter = DrugBagAdapter(this,viewModel)
        binding.drugbagList.adapter = bagAdapter

        var drugsAdapter = DrugsAdapter(this,viewModel)
        binding.drugsList.adapter = drugsAdapter

        viewModel.pharmacy.observe(this, Observer {  })

    }



}
