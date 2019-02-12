package com.wxsoft.fcare.ui

import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.utils.lazyFast

class ContainerActivity : BaseActivity() {

    companion object {
        const val FRAGMENT_TYPE="FRAGMENT_TYPE"
        const val TASK_ID = "TASK_ID"
        const val PATIENT_ID = "PATIENT_ID"
    }

    /**
     * fragment类型 enumable
     */
    private val fragmentType: String by lazyFast {
        intent ?.getStringExtra(FRAGMENT_TYPE)?:""

    }

    /**
     * 病人id
     */
    private val patientId: String by lazyFast {
        intent?.getStringExtra(PATIENT_ID)?:""
    }

    /**
     * 任务id
     */
    private val taskId: String by lazyFast {
        intent ?.getStringExtra(TASK_ID)?:""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, ContainerFragment.newInstance())
//                .commitNow()
//        }
    }

}
