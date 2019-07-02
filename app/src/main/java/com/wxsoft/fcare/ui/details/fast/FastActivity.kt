package com.wxsoft.fcare.ui.details.fast

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityFastBinding
import com.wxsoft.fcare.databinding.ActivityMessageBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.strategy.FastViewModel
import javax.inject.Inject

class FastActivity : BaseActivity()  {

    private lateinit var title:String
    private lateinit var content:String
    private lateinit var extra:String
    companion object {
        const val TITLE = "TITLE"
        const val CONTENT = "CONTENT"
        const val EXTRA = "EXTRA"
    }
    private lateinit var viewModel: FastViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var binding: ActivityFastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityFastBinding>(this, R.layout.activity_fast)
            .apply {
                viewModel=this@FastActivity.viewModel
                closeTv.setOnClickListener { finish() }
                lifecycleOwner = this@FastActivity
            }
        viewModel.result.observe(this, Observer {
            if(!it) return@Observer

            Intent().let { intent->

                (viewModel.savingResult.value as? Resource.Success)?.data?.result?.let{id->
                    intent.putExtra("id",id)
                }

                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }

}
