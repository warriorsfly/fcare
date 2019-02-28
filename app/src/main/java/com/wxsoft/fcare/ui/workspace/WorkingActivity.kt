package com.wxsoft.fcare.ui.workspace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityWorkingBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import javax.inject.Inject

class WorkingActivity : BaseActivity() {


    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

    private lateinit var adapter: QualityAdapter
    private lateinit var viewModel: WorkingViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        adapter= QualityAdapter(this@WorkingActivity).apply {
            submitList(listOf(
                TimeQuality("NOT",128,true),
                TimeQuality("DNT",40),
                TimeQuality("COT",89),
                TimeQuality("DRT",0),
                TimeQuality("DOT",0)
            ))
        }

        DataBindingUtil.setContentView<ActivityWorkingBinding>(this,R.layout.activity_working)
            .apply {

                viewModel=this@WorkingActivity.viewModel.apply { patientId=this@WorkingActivity.patientId }

                quality.adapter=this@WorkingActivity.adapter

                lifecycleOwner=this@WorkingActivity
//                viewModel?.qualities?.observe(this@WorkingActivity, Observer {
//                    adapter.submitList(it)
//                })
            }


    }
}
