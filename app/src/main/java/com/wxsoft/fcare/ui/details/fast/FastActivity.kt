package com.wxsoft.fcare.ui.details.fast

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityFastBinding
import com.wxsoft.fcare.databinding.ActivityMessageBinding
import com.wxsoft.fcare.ui.BaseActivity
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

    lateinit var binding: ActivityFastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityFastBinding>(this, R.layout.activity_fast)
            .apply {
                closeTv.setOnClickListener { finish() }
                lifecycleOwner = this@FastActivity
            }




    }

}
