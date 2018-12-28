package com.wxsoft.fcare.ui.details.dominating

import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.ui.BaseActivity

class DoMinaActivity : BaseActivity() {

    companion object {
        const val TASK_ID = "TASK_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_mina)
        setTitle("详情页")
    }



}
