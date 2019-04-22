package com.wxsoft.fcare.ui.details.vitalsigns.records

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityVitalSignsRecordBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.share.ShareActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class VitalSignsRecordActivity :  BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ADD_VITAL = 10
        const val SHARE = 20

    }

    private lateinit var viewModel: VitalSignsRecordViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityVitalSignsRecordBinding
    lateinit var listAdapter: VitalSignsDetailItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityVitalSignsRecordBinding>(this, R.layout.activity_vital_signs_record)
            .apply {
                lifecycleOwner = this@VitalSignsRecordActivity
            }
        setSupportActionBar(toolbar)
        title="生命体征"
        patientId=intent.getStringExtra(PATIENT_ID)?:""

        binding.viewModel = viewModel
        viewModel.patientId = patientId

        listAdapter = VitalSignsDetailItemAdapter(this,viewModel)
        binding.vitalRecordsList.adapter = listAdapter

        viewModel.vitals.observe(this, Observer {
            listAdapter.items = it
            if (it.size == 0){ toAddVital("230-2")}
        })

        viewModel.addvital.observe(this, Observer {
            if (it.equals("share")){
                toShareVital()
            }else{
                toAddVital(it)
            }


        })

        viewModel.modifyvital.observe(this, Observer { toModifyVital(it) })

    }

    fun toShareVital(){
        val intent = Intent(this, ShareActivity::class.java).apply {
            putExtra(ShareActivity.PATIENT_ID, patientId)
            putExtra(ShareActivity.URL, "230-2")
        }
        startActivityForResult(intent,SHARE )
    }

    fun toAddVital(typeId:String){
        val intent = Intent(this, VitalSignsActivity::class.java).apply {
            putExtra(VitalSignsActivity.PATIENT_ID, patientId)
            putExtra(VitalSignsActivity.TYPE_ID, "")
        }
        startActivityForResult(intent,ADD_VITAL )
    }
    fun toModifyVital(item: VitalSign){
        val intent = Intent(this, VitalSignsActivity::class.java).apply {
            putExtra(VitalSignsActivity.PATIENT_ID, patientId)
            putExtra(VitalSignsActivity.TYPE_ID, item.sceneType)
            putExtra(VitalSignsActivity.ID, item.id)
        }
        startActivityForResult(intent,ADD_VITAL )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            when(requestCode){
                ADD_VITAL ->{//
                   viewModel.getVitalRecords()
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.new_item->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}
