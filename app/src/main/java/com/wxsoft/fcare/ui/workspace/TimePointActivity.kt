package com.wxsoft.fcare.ui.workspace

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.TimePoint
import com.wxsoft.fcare.core.data.entity.TimePointHead
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityTimePointBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsActivity
import com.wxsoft.fcare.ui.details.vitalsigns.records.VitalSignsRecordActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.rating.RatingActivity
import com.wxsoft.fcare.utils.ActionType
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class TimePointActivity : BaseActivity(), OnDateSetListener  {
    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {
        dialog?.onDestroy()
        dialog=null
       viewModel.newTime(DateTimeUtils.formatter.format(millseconds))
    }

    private var dialog: TimePickerDialog?=null
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val ARG_NEW_ITEM_CODE = 20
        const val MEDICAL_HISTORY_CODE = 21
        const val VITAL_SIGNS = 22
        const val CHECK_BODY = 23
        const val DIAGNOSE = 24
        const val MEASURES = 25
        const val INV = 26
        const val Catheter = 27
        const val CT = 28
        const val DISCHARGE = 29
        const val OUTCOME = 30
        const val INFORMEDCONSENT = 31
        const val THROMBOLYSIS = 32
        const val DRUGRECORD = 33
        const val OTDIAGNOSE = 34
        const val CT_OPERATION = 35
        const val RATING = 36
        const val CABG = 37
        const val BASE_INFO = 38
        const val COMPLAINTS = 39
        const val STRATEGY = 40
    }

    private val patientId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.PATIENT_ID)?:""
    }

    private lateinit var viewModel: TimePointViewModel
    private lateinit var adapter: TimePointAdapter
    @Inject
    lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        adapter= TimePointAdapter(this,::doTimeSelect)
        DataBindingUtil.setContentView<ActivityTimePointBinding>(this,R.layout.activity_time_point)
            .apply {
                if (list.adapter == null)
                    list.adapter = this@TimePointActivity.adapter
                viewModel = this@TimePointActivity.viewModel.apply { patientId = this@TimePointActivity.patientId }
                title="急救时间轴"
                lifecycleOwner = this@TimePointActivity

            }

        viewModel.liveData.observe(this, Observer {
            adapter.points=it
        })

        viewModel.indexData.observe(this, Observer {
            if(it==0){
                (adapter.differ.currentList[0] as? TimePointHead)?.excutedAt=adapter.points[0] .excutedAt?.substring(0,10)
                adapter.notifyItemChanged(it)
            }
            adapter.notifyItemChanged(it+1)
//            adapter.points=adapter.points
        })

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        setSupportActionBar(toolbar)
    }

    private fun doTimeSelect(point:TimePoint){
        viewModel.setCurrentPoint(point)
        val currentTime =point.excutedAt.let { text ->
            if (text==null) 0L else DateTimeUtils.formatter.parse(text).time
        }

        dialog = createDialog(currentTime)
        dialog?.show(supportFragmentManager, "all")
    }


    private fun doOperation(operation:WorkOperation){
        when(operation.actionCode){
            ActionType.GRACE->{
                val intent = Intent(this, RatingActivity::class.java)
                    .apply {
                        putExtra(ProfileActivity.PATIENT_ID, patientId)
                    }
                startActivityForResult(intent, RATING)
            }
            ActionType.给药 ->{
                val intent = Intent(this@TimePointActivity, DrugRecordsActivity::class.java).apply {
                    putExtra(DrugRecordsActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, DRUGRECORD)
            }
            ActionType.生命体征 -> {
                val intent = Intent(this@TimePointActivity, VitalSignsRecordActivity::class.java).apply {
                    putExtra(VitalSignsRecordActivity.PATIENT_ID, patientId)
                }
                startActivityForResult(intent, VITAL_SIGNS)
            }
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
            .setCurrentMillseconds(if (time == 0L) System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(16)
            .build()
    }

}
