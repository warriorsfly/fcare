package com.wxsoft.fcare.ui.outcome

import androidx.lifecycle.Observer
import android.content.Intent
import androidx.databinding.DataBindingUtil
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
import com.wxsoft.fcare.databinding.ActivityOutcomeChestBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.ui.selecter.SelecterOfOneAdapter
import kotlinx.android.synthetic.main.layout_activity_outcome_chest1.*
import kotlinx.android.synthetic.main.layout_activity_outcome_chest2.*
import kotlinx.android.synthetic.main.layout_activity_outcome_chest3.*
import kotlinx.android.synthetic.main.layout_activity_outcome_chest4.*
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class OutComeActivity : BaseActivity(), OnDateSetListener, View.OnClickListener {

    private var dialog: TimePickerDialog?=null
    override fun onClick(v: View?) {

        (v as? Button)?.let {
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
    private lateinit var viewModel: OutComeViewModel
    @Inject
    lateinit var factory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityOutcomeChestBinding>(this, R.layout.activity_outcome_chest)
            .apply {
                viewModel = this@OutComeActivity. viewModel
                lifecycleOwner = this@OutComeActivity
            }
        patientId=intent.getStringExtra(OutComeActivity.PATIENT_ID)?:""
        viewModel.patientId = patientId

        back.setOnClickListener { onBackPressed() }

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

        start1.setOnClickListener(this)
        start2.setOnClickListener(this)
        start3.setOnClickListener(this)
        start4.setOnClickListener(this)
        pci_start.setOnClickListener(this)
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
            .setCurrentMillseconds(if (time == 0L) System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(16)
            .build()
    }
}
