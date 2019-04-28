package com.wxsoft.fcare.ui.details.catheter

import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.databinding.ActivityCatheterBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_catheter.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class CatheterActivity : BaseActivity(), OnDateSetListener{

    private var dialog: TimePickerDialog?=null
    private val selectedIndex= mutableListOf<Int>()


    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<TextView>(selectedId))?.text= DateTimeUtils.formatter.format(millseconds)
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

    private fun selectPlace(){
        val list= viewModel.docs.map { it.trueName }.toTypedArray()

        val selectedItems= viewModel.docs.map { user ->

            viewModel.intervention.value?.interventionMateIds?.contains(user.id)?:false
        }.toBooleanArray()

        AlertDialog.Builder(this).setMultiChoiceItems(list,selectedItems)
        { _, which, isChecked ->
            if(selectedIndex.contains(which) && !isChecked){
                selectedIndex.remove(which)

            }else if(!selectedIndex.contains(which) && isChecked){
                selectedIndex.add(which)
            }

            viewModel.intervention.value?.interventionMateIds=selectedIndex.joinToString {
                viewModel.docs[it].id
            }

            viewModel.intervention.value?.interventionMates=selectedIndex.joinToString {
                viewModel.docs[it].trueName
            }
        }.show()
    }

    private var selectedId=0

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }
    private lateinit var viewModel: CatheterViewModel
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityCatheterBinding>(this, R.layout.activity_catheter)
            .apply {
                viewModel = this@CatheterActivity. viewModel
                lifecycleOwner = this@CatheterActivity
            }
        patientId=intent.getStringExtra(CatheterActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

//        back.setOnClickListener { onBackPressed() }

        setSupportActionBar(toolbar)
        title="导管室操作"

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.commitResult.observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->

                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })

        viewModel.modifySome.observe(this, Observer {
            when(it){
                "0"->selectPlace()
                "1"->showDatePicker(findViewById(R.id.start))
                "2"->showDatePicker(findViewById(R.id.end_thromboly_time))
                "3"->showDatePicker(findViewById(R.id.patient_arrive))
                "4"->showDatePicker(findViewById(R.id.start_puncture))
                "5"->showDatePicker(findViewById(R.id.punctured))
                "6"->showDatePicker(findViewById(R.id.start_angiography))
                "7"->showDatePicker(findViewById(R.id.angiographied))
                "8"->showDatePicker(findViewById(R.id.wire))
                "9"->showDatePicker(findViewById(R.id.end))
                "10"->showDatePicker(findViewById(R.id.leave))
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
                viewModel.saving()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun createDialog(time:Long): TimePickerDialog {

            return TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setHourText("")
                .setMinuteText("")
                .setCyclic(false)
                .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
                .setType(Type.ALL)
                .setWheelItemTextSize(16)
                .setThemeColor(R.color.colorPrimary)
                .build()
    }
}
