package com.wxsoft.fcare.ui.details.diagnose.record

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDiagnoseRecordBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.diagnose.DiagnoseActivity

import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DiagnoseRecordActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ADD = 10

    }

    private lateinit var viewModel: DiagnoseRecordViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityDiagnoseRecordBinding
    lateinit var listAdapter: DiagnoseRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDiagnoseRecordBinding>(this, R.layout.activity_diagnose_record)
            .apply {
                lifecycleOwner = this@DiagnoseRecordActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        listAdapter = DiagnoseRecordAdapter(this,viewModel)
        binding.diagnoseRecordsList.adapter = listAdapter

        viewModel.diagnoses.observe(this, Observer { listAdapter.items = it?: emptyList() })

        viewModel.adddiagnose.observe(this, Observer { toAddDiagnose(it) })
        viewModel.modifydiagnosis.observe(this, Observer { toModifyVital(it) })

        setSupportActionBar(toolbar)
        title="诊断记录"

    }



    fun toAddDiagnose(typeId:String){
        val intent = Intent(this, DiagnoseActivity::class.java).apply {
            putExtra(DiagnoseActivity.PATIENT_ID, patientId)
            putExtra(DiagnoseActivity.TYPE_ID, typeId)
        }
        startActivityForResult(intent,ADD )
    }

    fun toModifyVital(item: Diagnosis){
        val intent = Intent(this, DiagnoseActivity::class.java).apply {
            putExtra(DiagnoseActivity.PATIENT_ID, patientId)
            putExtra(DiagnoseActivity.TYPE_ID, item.sceneType)
            putExtra(DiagnoseActivity.ID, item.id)
        }
        startActivityForResult(intent,ADD )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                ADD ->{//
                    viewModel.getDiagnoseRecords()
                }

            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_subject,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        return  when(item?.itemId){
//            R.drugId.submit->{
//                viewModel.click()
//                true
//            }
//            else->super.onOptionsItemSelected(item)
//        }
//    }
}
