package com.wxsoft.fcare.ui.details.ecg

import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import com.wxsoft.fcare.R
import com.wxsoft.fcare.databinding.ActivityEcgBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_new_title.*

class EcgActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityEcgBinding>(
            this,
            R.layout.activity_ecg
        ).apply{

            lifecycleOwner = this@EcgActivity
        }

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }
}
