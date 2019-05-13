package com.wxsoft.fcare.ui.common

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ActivityContainerBinding
import com.wxsoft.fcare.ui.BaseActivity


class ContainerActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityContainerBinding>(this, R.layout.activity_container)
            .apply {
                lifecycleOwner = this@ContainerActivity
            }
    }


}
