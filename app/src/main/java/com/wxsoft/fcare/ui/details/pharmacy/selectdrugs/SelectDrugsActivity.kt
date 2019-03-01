package com.wxsoft.fcare.ui.details.pharmacy.selectdrugs

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelectDrugsBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_select_drugs.*
import javax.inject.Inject

class SelectDrugsActivity : BaseActivity() {


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: SelectDrugsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySelectDrugsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySelectDrugsBinding>(this, R.layout.activity_select_drugs)
            .apply {
                lifecycleOwner = this@SelectDrugsActivity
            }
        patientId=intent.getStringExtra(SelectDrugsActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }
    }
}
