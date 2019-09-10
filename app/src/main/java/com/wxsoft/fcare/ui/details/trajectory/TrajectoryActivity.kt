package com.wxsoft.fcare.ui.details.trajectory

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityTrajectoryBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyAdapter
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class TrajectoryActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: TrajectoryViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityTrajectoryBinding

    lateinit var adapter: StrategyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityTrajectoryBinding>(this, R.layout.activity_trajectory)
            .apply {
                lifecycleOwner = this@TrajectoryActivity
            }
        patientId=intent.getStringExtra(MeasuresActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        title="院内节点"
    }
}
