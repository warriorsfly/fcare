package com.wxsoft.fcare.ui.details.diagnose.select

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivitySelectDiagnoseBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseAdapter
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseSonListAdapter
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseViewModel

import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class SelectDiagnoseActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var comefrom:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val COME_FROM = "COME_FROM"
    }

    private lateinit var viewModel: DiagnoseViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivitySelectDiagnoseBinding
    lateinit var diagnoseAdapter: DiagnoseAdapter
    lateinit var sondiagnoseAdapter: DiagnoseSonListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivitySelectDiagnoseBinding>(this, R.layout.activity_select_diagnose)
            .apply {
                viewModel = this@SelectDiagnoseActivity.viewModel
                lifecycleOwner = this@SelectDiagnoseActivity
            }
        patientId=intent.getStringExtra(SelectDiagnoseActivity.PATIENT_ID)?:""
        comefrom=intent.getStringExtra(SelectDiagnoseActivity.COME_FROM)?:""
        viewModel.patientId = patientId
        viewModel.activityType = "SelectDiagnose"
        setSupportActionBar(toolbar)
        title="诊断"
//
        diagnoseAdapter = DiagnoseAdapter(this,viewModel)
        binding.firstTypesList.adapter = diagnoseAdapter
        viewModel.thoracalgiaItems.observe(this, Observer { diagnoseAdapter.items = it ?: emptyList() })

        sondiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        sondiagnoseAdapter.section = 3
        viewModel.sonItems.observe(this, Observer { sondiagnoseAdapter.items = it ?: emptyList() })
        binding.secondList.adapter = sondiagnoseAdapter

        viewModel.loadThoracalgias()
        viewModel.getDiagnose()


        viewModel.diagnosis.observe(this, Observer {  })

        viewModel.backToLast.observe(this, Observer {
            if (it.equals("haveSelected")){
                val bundle = Bundle()
                bundle.putSerializable("haveSelectedDiagnose",viewModel.diagnosis.value!!)
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                finish()
            }
        })



    }

}
