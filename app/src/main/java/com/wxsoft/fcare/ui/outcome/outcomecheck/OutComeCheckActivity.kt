package com.wxsoft.fcare.ui.outcome.outcomecheck

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityOutComeCheckBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.discharge.DisChargeSonActivity
import kotlinx.android.synthetic.main.activity_out_come_check.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class OutComeCheckActivity : BaseActivity() {


    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: OutComeCheckViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityOutComeCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityOutComeCheckBinding>(this, R.layout.activity_out_come_check)
            .apply {
                viewModel = this@OutComeCheckActivity. viewModel
                lifecycleOwner = this@OutComeCheckActivity
            }
        patientId=intent.getStringExtra(DisChargeSonActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        setSupportActionBar(toolbar)
        title="出院检查"


        radio_1.setOnCheckedChangeListener({ group, checkedId ->
            when(checkedId){
                R.id.radio_btn_1 ->{viewModel.data?.value?.troponin_72h_type=1}
                R.id.radio_btn_2 ->{viewModel.data?.value?.troponin_72h_type=2}
            }
        })
        radio_2.setOnCheckedChangeListener({ group, checkedId ->
            when(checkedId){
                R.id.radio2_btn_1 ->{viewModel.data?.value?.bnP_TYPE=1}
                R.id.radio2_btn_2 ->{viewModel.data?.value?.bnP_TYPE=2}
            }
        })

        viewModel.saveResult.observe(this, Observer {
            Intent().let { intent ->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.save()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }



}
