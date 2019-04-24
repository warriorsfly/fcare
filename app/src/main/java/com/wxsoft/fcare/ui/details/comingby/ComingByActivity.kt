package com.wxsoft.fcare.ui.details.comingby

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityComingByBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ComingByActivity : BaseActivity()  {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: ComingByViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityComingByBinding

    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityComingByBinding>(this, R.layout.activity_coming_by)
            .apply {
                viewModel=this@ComingByActivity.viewModel
                lifecycleOwner = this@ComingByActivity
            }
        setSupportActionBar(toolbar)
        title="来院方式"
        viewModel.patientId=patientId

    }

}
