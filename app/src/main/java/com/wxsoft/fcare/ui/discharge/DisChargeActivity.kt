package com.wxsoft.fcare.ui.discharge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDischargeBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.diagnose.select.SelectDiagnoseActivity
import kotlinx.android.synthetic.main.activity_discharge.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class DisChargeActivity : BaseTimingActivity(), View.OnClickListener {

    override fun onClick(v: View?) {

        (v as? TextView)?.let {
            selectedId = it.id
            val currentTime = it.text.toString().let { text ->
                if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }

    }

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<Button>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
    }

    private var selectedId=0

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: DisChargeViewModel
    private lateinit var binding: ActivityDischargeBinding
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityDischargeBinding>(this, R.layout.activity_discharge)
            .apply {
                model0.setOnClickListener { toSelectDiagnose() }
                diagnose.setOnClickListener { toSelectDiagnose() }
                viewModel = this@DisChargeActivity. viewModel
                lifecycleOwner = this@DisChargeActivity
            }
        patientId=intent.getStringExtra(DisChargeActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        setSupportActionBar(toolbar)
        title="出院"

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })


        viewModel.commitResult .observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->

                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        start.setOnClickListener(this)
    }


    fun toSelectDiagnose(){
        val intent = Intent(this@DisChargeActivity, SelectDiagnoseActivity::class.java).apply {
            putExtra(SelectDiagnoseActivity.PATIENT_ID, patientId)
            putExtra(SelectDiagnoseActivity.COME_FROM, "DisCharge")
        }
        startActivityForResult(intent, 100)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_subject,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return  when(item?.itemId){
            R.id.submit->{
                viewModel.click()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                100 ->{//选择诊断
                    val diagnosis= data?.getSerializableExtra("haveSelectedDiagnose") as Diagnosis
                    binding.diagnose.setText(diagnosis.diagnosisCode2Name + " " + diagnosis.diagnosisCode3Name)
                    viewModel.data.value?.diagnosis = diagnosis
                }

            }
        }
    }
}
