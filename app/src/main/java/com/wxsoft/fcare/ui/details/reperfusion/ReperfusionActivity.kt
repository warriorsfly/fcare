package com.wxsoft.fcare.ui.details.reperfusion

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityReperfusionBinding
import com.wxsoft.fcare.ui.BaseTimingActivity
import com.wxsoft.fcare.ui.details.pharmacy.PharmacyActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ReperfusionActivity : BaseTimingActivity() {

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.decide_cabg_time -> viewModel.cabg.value?.decisionOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.start_cabg_time -> viewModel.cabg.value?.startOperationTime = DateTimeUtils.formatter.format(millseconds)
            R.id.end_cabg_time -> viewModel.cabg.value?.endOperationTime = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { text->
                if(text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private var selectedId=0


    private lateinit var patientId:String

    companion object {
        const val PATIENT_ID = "PATIENT_ID"

    }

    private lateinit var viewModel: ReperfusionViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityReperfusionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityReperfusionBinding>(this, R.layout.activity_reperfusion)
            .apply {
                lifecycleOwner = this@ReperfusionActivity
            }
        patientId=intent.getStringExtra(PharmacyActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        title="CABG"

        viewModel.getCABG()

        viewModel.mSelectTime.observe(this, Observer {
            when(it){
                "1"->{showDatePicker(findViewById(R.id.decide_cabg_time))}
                "2"->{showDatePicker(findViewById(R.id.start_cabg_time))}
                "3"->{showDatePicker(findViewById(R.id.end_cabg_time))}
            }
        })


        viewModel.backToLast.observe(this, Observer {

            Intent().let { intent->
                setResult(RESULT_OK, intent)
                finish()
            }
        })


        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this@ReperfusionActivity,it, Toast.LENGTH_SHORT).show()
        })
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
}
