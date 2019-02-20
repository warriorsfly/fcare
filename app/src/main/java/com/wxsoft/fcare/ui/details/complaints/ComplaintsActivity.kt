package com.wxsoft.fcare.ui.details.complaints

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wxsoft.fcare.R

class ComplaintsActivity : AppCompatActivity() {

    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints)
    }
}
