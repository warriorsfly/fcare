package com.wxsoft.fcare.ui.details.diagnose

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.databinding.ActivityDiagnoseBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DiagnoseActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: DiagnoseViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDiagnoseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDiagnoseBinding>(this, R.layout.activity_diagnose)
            .apply {
                setLifecycleOwner(this@DiagnoseActivity)
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }

        var typeAdapter = DiagnoseTypeAdapter(this,viewModel)
        viewModel.typeItems.observe(this, Observer { it -> typeAdapter.items = it ?: emptyList() })
        binding.diagnoseTypeList.adapter = typeAdapter

        var diagnoseAdapter = DiagnoseAdapter(this,viewModel)
        viewModel.thoracalgiaItems.observe(this, Observer { it -> diagnoseAdapter.items = it ?: emptyList() })
        binding.diagnoseList.adapter = diagnoseAdapter

        var sondiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        sondiagnoseAdapter.section = 3
        viewModel.sonItems.observe(this, Observer { it -> sondiagnoseAdapter.items = it ?: emptyList() })
        binding.diagnoseOtherList.adapter = sondiagnoseAdapter

        var illnessdiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        illnessdiagnoseAdapter.section = 4
        viewModel.illnessItems.observe(this, Observer { it -> illnessdiagnoseAdapter.items = it ?: emptyList() })
        binding.illnessList.adapter = illnessdiagnoseAdapter

        viewModel.startConduitRoom.observe(this, Observer {
            startConduitRoom()
        })

        viewModel.getDiagnose()

        viewModel.diagnosis.observe(this, Observer {  })

        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent);
                finish();
            }
        })

    }




    fun startConduitRoom(){
        AlertDialog.Builder(this)
            .setMessage("通知院内启动导管室？")
            .setTitle("启动导管室")
            .setPositiveButton("确定", DialogInterface.OnClickListener { _, _ ->

            })
            .setNeutralButton("取消", DialogInterface.OnClickListener { _, _ ->

            })
            .create()
            .show()
    }

}
