package com.wxsoft.fcare.ui.details.thrombolysis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wxsoft.fcare.R

class ThrombolysisActivity : AppCompatActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thrombolysis)
    }
}
