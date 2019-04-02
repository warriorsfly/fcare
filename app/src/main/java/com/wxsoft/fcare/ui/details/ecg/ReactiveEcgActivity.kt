package com.wxsoft.fcare.ui.details.ecg

import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_reactive_ecg.*

class ReactiveEcgActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reactive_ecg)
        close.setOnClickListener { finish() }
    }
}
