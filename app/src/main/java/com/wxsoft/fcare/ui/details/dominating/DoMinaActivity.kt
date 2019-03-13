package com.wxsoft.fcare.ui.details.dominating

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.*
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment.Companion.BASE_INFO
import kotlinx.android.synthetic.main.activity_do_mina.*
import kotlinx.android.synthetic.main.fragment_task_process.*
import kotlinx.android.synthetic.main.layout_task_process_title.*
import javax.inject.Inject

class DoMinaActivity : BaseActivity(), OnDateSetListener {
    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        viewModel.changing(changingStatus,DateTimeUtils.formatter.format(millseconds),millseconds)
        dialog
    }

    private var dialog: TimePickerDialog?=null

    private var changingStatus=0

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel:DoMinaViewModel

    private lateinit var taskId:String

    companion object {
        const val TASK_ID = "TASK_ID"
        const val STATE_COUNT = 3
        const val START_POSITION = 0
        const val PATIENTS_POSITION = 1
        const val GIS_POSITION = 2

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=viewModelProvider(factory)
        DataBindingUtil.setContentView<ActivityDoMinaBinding>(
            this,
            R.layout.activity_do_mina
        ).apply {


            viewModel=this@DoMinaActivity.viewModel
            lifecycleOwner = this@DoMinaActivity

        }
        taskId=intent.getStringExtra(TASK_ID)?:""

        viewModel.taskId=taskId

        seekBar.setOnTouchListener { _, _ ->  true }

        viewModel.mesAction.observe(this, EventObserver {

            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.atAction.observe(this,EventObserver{

            val mess=when(it){
                2->"确认到达现场吗?"
                3->"确认开始首次医疗接触吗?"
                4->"确认开始返回医院吗?"
                5->"确认到达医院大门吗?"
                else->throw IllegalStateException("Unknown type $it")
            }
            val dialog=AlertDialog.Builder(this,R.style.Theme_FCare_Dialog_Text).setMessage(mess)
                .setPositiveButton("确定") { _, _ -> viewModel.doAction(it) }
                .setNegativeButton("取消"){ _, _ -> }

            dialog.show()
        })
        setSupportActionBar(toolbar)
        viewPager.adapter = TaskStateAdapter(supportFragmentManager)

        viewModel.changeTimeAction.observe(this, EventObserver {
            changingStatus=it
            val time =when(changingStatus){
                1->viewModel.startTimeStamp
                2->viewModel.arriveTimeStamp
                3->viewModel.firstMetTimeStamp
                4->viewModel.returningTimeStamp
                5->viewModel.arriveHosTimeStamp
                else->0
            }?:0

            dialog=createDialog(time)
            dialog?.show(supportFragmentManager,"all")
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                NEW_PATIENT_REQUEST, BASE_INFO -> {
//                    data?.getStringExtra(NEW_PATIENT_ID)
                    viewModel.loadTask()
                    setResult(RESULT_OK)
                }
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
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(12)
            .build()
    }

}

class TaskStateAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0..2).map {
            when (it) {
                DoMinaActivity.START_POSITION ->ProcessFragment()
                DoMinaActivity.PATIENTS_POSITION -> PatientManagerFragment()
                DoMinaActivity.GIS_POSITION ->GisFragment()

                else -> throw IllegalStateException("Unknown index $it")
            }
        }
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {

        return statusFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            DoMinaActivity.START_POSITION ->"任务进度"
            DoMinaActivity.PATIENTS_POSITION -> "病人信息"
            DoMinaActivity.GIS_POSITION ->"轨迹"

            else -> throw IllegalStateException("Unknown index $position")
        }
    }

    override fun getCount(): Int {
        return statusFragments.size
    }

}
