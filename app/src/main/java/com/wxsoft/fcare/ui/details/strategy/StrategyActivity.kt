package com.wxsoft.fcare.ui.details.strategy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityStrategyBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity

import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class StrategyActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: StrategyViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityStrategyBinding

    lateinit var adapter: StrategyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityStrategyBinding>(this, R.layout.activity_strategy)
            .apply {
                lifecycleOwner = this@StrategyActivity
            }
        patientId=intent.getStringExtra(MeasuresActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        title="治疗策略"

        adapter = StrategyAdapter(this,viewModel)
//        adapter.items =
        binding.strategyList.adapter = adapter
        viewModel.strategyItems.observe(this, Observer { adapter.items = it })

        viewModel.strategy.observe(this, Observer {  })

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        viewModel.saveResult.observe(this, Observer {
            when(it){
                "success" ->{
                    Intent().let { intent ->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

    }

}
