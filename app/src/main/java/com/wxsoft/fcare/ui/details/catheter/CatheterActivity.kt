package com.wxsoft.fcare.ui.details.catheter

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_catheter.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class CatheterActivity : BaseActivity(), OnDateSetListener, View.OnClickListener {

    private var dialog: TimePickerDialog?=null
    private val selectedIndex= mutableListOf<Int>()
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.thromboly_place -> {

                val list=viewModel.docs.map { it.trueName }?.toTypedArray()

                val selectedItems=(viewModel.docs.map {  user ->

                    viewModel.intervention.value?.interventionMateIds?.contains(user.id)?:false
                }?: emptyList()).toBooleanArray()

                val dialog = AlertDialog.Builder(this).setMultiChoiceItems(list,selectedItems
                ) { _, which, isChecked ->
                    if(selectedIndex.contains(which) && !isChecked){
                        selectedIndex.remove(which)

                    }else if(!selectedIndex.contains(which) && isChecked){
                        selectedIndex.add(which)
                    }

                    viewModel.intervention.value?.interventionMateIds=selectedIndex.joinToString {
                        viewModel.docs.get(it)?.id?:""
                    }

                    viewModel.intervention.value?.interventionMates=selectedIndex.joinToString {
                        viewModel.docs.get(it)?.trueName?:""
                    }
                }.show()


            }
            else ->{

                (v as? Button)?.let {
                    selectedId = it.id
                    val currentTime = it.text.toString()?.let { text ->
                        return@let if (text.isEmpty()) 0L else DateTimeUtils.formatter.parse(text).time
                    }

                    dialog = createDialog(currentTime)
                    dialog?.show(supportFragmentManager, "all");
                }
            }
        }
    }

    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        dialog?.onDestroy()
        dialog=null
        (findViewById<Button>(selectedId))?.text=DateTimeUtils.formatter.format(millseconds)
    }

    private var selectedId=0;

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
                setLifecycleOwner(this@CatheterActivity)
            }
        patientId=intent.getStringExtra(CatheterActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        back.setOnClickListener { onBackPressed() }

        start.setOnClickListener  (this)
        end_thromboly_time.setOnClickListener  (this)
        patient_arrive.setOnClickListener  (this)
        start_puncture.setOnClickListener  (this)
        punctured.setOnClickListener  (this)
        start_angiography.setOnClickListener  (this)
        angiographied.setOnClickListener  (this)
        wire.setOnClickListener  (this)
        end.setOnClickListener  (this)
        leave.setOnClickListener  (this)
        thromboly_place.setOnClickListener  (this)

        viewModel.mesAction.observe(this,EventObserver{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.commitResult .observe(this, Observer {
            when(it) {
                is Resource.Success -> {
                    Intent().let { intent ->

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        })
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
                .build()
    }
}
