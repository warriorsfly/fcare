package com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityAcsdrugBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.BaseTimingActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ACSDrugActivity : BaseTimingActivity() {

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
//        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
        when(selectedId){
            R.id.start_1 -> viewModel.acsDrug.value?.acs_Delivery_Time = DateTimeUtils.formatter.format(millseconds)
            R.id.start_2 -> viewModel.acsDrug.value?.anticoagulation_Date = DateTimeUtils.formatter.format(millseconds)
        }
    }

    private fun showDatePicker(v: View?){
        (v as? TextView)?.let {
            selectedId=it.id
            val currentTime= it.text.toString().let { txt->
                if(txt.isEmpty()) 0L else DateTimeUtils.formatter.parse(txt).time
            }

            dialog = createDialog(currentTime)
            dialog?.show(supportFragmentManager, "all")
        }
    }

    private var selectedId=0
    private var selectedFragment=0

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: AcsDrugViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityAcsdrugBinding

    private lateinit var changeDrugsFragment1: ChangeDrugFragment
    private lateinit var changeDrugsFragment2: ChangeDrugFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityAcsdrugBinding>(this, R.layout.activity_acsdrug)
            .apply {
                line1.setOnClickListener {
                    showDatePicker(findViewById(R.id.start_1))
                }
                line4.setOnClickListener {
                    showDatePicker(findViewById(R.id.start_2))
                }
                viewModel = this@ACSDrugActivity.viewModel
                lifecycleOwner = this@ACSDrugActivity
            }
        patientId=intent.getStringExtra(ACSDrugActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        changeDrugsFragment1 = ChangeDrugFragment(this,this@ACSDrugActivity.viewModel,"1")
        changeDrugsFragment2 = ChangeDrugFragment(this,this@ACSDrugActivity.viewModel,"2")

        viewModel.clickSomething.observe(this, Observer {
            when(it){
                "1"->{
                    changeDrugsFragment1.show(getSupportFragmentManager(), "Dialog")
                    selectedFragment = 1
                }
                "2"->{
                    changeDrugsFragment2.show(getSupportFragmentManager(), "Dialog")
                    selectedFragment = 2
                }
                "cancel","ok" ->{
                    if (selectedFragment == 1){
                        changeDrugsFragment1.dismiss()
                    }else if(selectedFragment == 2){
                        changeDrugsFragment2.dismiss()
                    }

                }
                "saveResult"->commit()
                else->{}
            }
        })

        setSupportActionBar(toolbar)
        title="ACS给药"

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

    fun commit(){
        Intent().let { intent->
            val bundle = Bundle()
                bundle.putSerializable("SelectedACSDrug",viewModel.acsDrug.value)
            intent.putExtras(bundle)
            setResult(DaggerAppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }
}
