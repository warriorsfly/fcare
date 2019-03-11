package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySearchPatientsBinding
import com.wxsoft.fcare.ui.BaseActivity
import javax.inject.Inject

class SearchPatientsActivity : BaseActivity() {

    private lateinit var viewModel: SearchPatientsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySearchPatientsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySearchPatientsBinding>(this, R.layout.activity_search_patients)
            .apply {
                lifecycleOwner = this@SearchPatientsActivity
            }


    }
}
