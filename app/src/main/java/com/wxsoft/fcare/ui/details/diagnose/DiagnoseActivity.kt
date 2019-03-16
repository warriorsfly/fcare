package com.wxsoft.fcare.ui.details.diagnose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDiagnoseBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class DiagnoseActivity : BaseActivity() {

    private lateinit var patientId:String
    private lateinit var id:String
    private lateinit var typeId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ID = "ID"
        const val TYPE_ID = "TYPE_ID"
        const val SELECT_DIAGNOSE_TYPE = 10
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
                model2.setOnClickListener {
                    toSelectSonDiagnose()
                }
                lifecycleOwner = this@DiagnoseActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        id=intent.getStringExtra(ID)?:""
        typeId=intent.getStringExtra(TYPE_ID)?:""
        viewModel.patientId = patientId
        viewModel.id = id
        viewModel.sceneTypeId = typeId
        binding.viewModel = viewModel
        viewModel.activityType = "Diagnose"

        back.setOnClickListener { onBackPressed() }






        val illnessdiagnoseAdapter = DiagnoseSonListAdapter(this,viewModel)
        illnessdiagnoseAdapter.section = 4
        viewModel.illnessItems.observe(this, Observer { illnessdiagnoseAdapter.items = it ?: emptyList() })
        binding.illnessList.adapter = illnessdiagnoseAdapter



        viewModel.getDiagnose()


        viewModel.backToLast.observe(this, Observer {
           if (it.equals("back")){
               Intent().let { intent->
                   setResult(RESULT_OK, intent)
                   finish()
               }
           }
        })

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })


    }


    fun toSelectSonDiagnose(){
        val intent = Intent(this, SelectDiagnoseActivity::class.java).apply {
            putExtra(SelectDiagnoseActivity.PATIENT_ID, patientId)
        }
        startActivityForResult(intent,SELECT_DIAGNOSE_TYPE )
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_DIAGNOSE_TYPE ->{//用药
                    val diagnose = data?.getSerializableExtra("haveSelectedDiagnose") as Diagnosis
                    viewModel.loadSubmitDiagnosis.value = diagnose
                }

            }
        }
    }


}
