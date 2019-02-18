package com.wxsoft.fcare.ui.details.diagnose

import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDiagnoseBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DiagnoseActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var id:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
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
                lifecycleOwner = this@DiagnoseActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        id=intent.getStringExtra(ID)?:""
        viewModel.patientId = patientId
        viewModel.id = id
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }

        val typeAdapter = DiagnoseTypeAdapter(this,viewModel)
        viewModel.typeItems.observe(this, Observer { typeAdapter.items = it ?: emptyList() })
        binding.diagnoseTypeList.adapter = typeAdapter

        val diagnoseAdapter = DiagnoseAdapter(this,viewModel)
        viewModel.thoracalgiaItems.observe(this, Observer { diagnoseAdapter.items = it ?: emptyList() })
        binding.diagnoseList.adapter = diagnoseAdapter

        val sondiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        sondiagnoseAdapter.section = 3
        viewModel.sonItems.observe(this, Observer { sondiagnoseAdapter.items = it ?: emptyList() })
        binding.diagnoseOtherList.adapter = sondiagnoseAdapter

        val illnessdiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        illnessdiagnoseAdapter.section = 4
        viewModel.illnessItems.observe(this, Observer { illnessdiagnoseAdapter.items = it ?: emptyList() })
        binding.illnessList.adapter = illnessdiagnoseAdapter

        viewModel.startConduitRoom.observe(this, Observer {
            startConduitRoom()
        })

        viewModel.startCT.observe(this, Observer {
            startCT()
        })

        viewModel.getDiagnose()

        viewModel.diagnosis.observe(this, Observer {  })

        viewModel.backToLast.observe(this, Observer {
            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

    }




    private fun startConduitRoom(){
        AlertDialog.Builder(this,R.style.Theme_FCare_Dialog_Text)
            .setMessage("通知院内启动导管室？")
            .setPositiveButton("确定") { _, _ ->
                viewModel.commitNoticeInv()
            }
            .setNegativeButton("取消") { _, _ ->

            }.show()
    }


    private fun startCT(){
        AlertDialog.Builder(this,R.style.Theme_FCare_Dialog_Text)
            .setMessage("通知院内CT室？")
            .setPositiveButton("确定") { _, _ ->
                viewModel.commitNoticePacs()
            }
            .setNegativeButton("取消") { _, _ ->

            }.show()
    }

}
