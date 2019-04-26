package com.wxsoft.fcare.ui.details.dominating

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.databinding.FragmentTaskStateBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dispatchcar.DispatchCarActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.*
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import com.wxsoft.fcare.utils.ActionCode.Companion.BASE_INFO
import kotlinx.android.synthetic.main.activity_do_mina.*
import kotlinx.android.synthetic.main.layout_task_process_title.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DoMinaActivity : BaseActivity(), OnDateSetListener {
    override fun onDateSet(timePickerView: TimePickerDialog?, millseconds: Long) {

        viewModel.changing(changingStatus,DateTimeUtils.formatter.format(millseconds),millseconds)
        dialog
    }

    private val dictFragment by lazy{
        TaskSheetFragment(::cancel)
    }

    private var dialog: TimePickerDialog?=null

    private var changingStatus=0

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel:DoMinaViewModel

    private lateinit var taskId:String


    private lateinit var adapter: PatientInTaskAdapter

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
        adapter=PatientInTaskAdapter(this,::showPatient)
        DataBindingUtil.setContentView<ActivityDoMinaBinding>(
            this,
            R.layout.activity_do_mina
        ).apply {


            viewModel=this@DoMinaActivity.viewModel
            list.adapter=adapter
            goTo.setOnClickListener {
                val intent = Intent(this@DoMinaActivity, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, viewModel?.taskId)
                }
                startActivityForResult(intent, NEW_PATIENT_REQUEST)

            }
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
            val bin=FragmentTaskStateBinding.inflate(layoutInflater)
            val dialog=AlertDialog.Builder(this,R.style.Theme_FCare_Dialog)
                .setView(
                    bin.apply {
                        Calendar.getInstance().let {
                            date.text="${it.get(Calendar.YEAR)}-${it.get(Calendar.MONTH)+1}-${it.get(Calendar.DAY_OF_MONTH)}"
//                            time.text="${it.get(Calendar.HOUR_OF_DAY)}:${it.get(Calendar.MINUTE)}"
                            memo.text=mess
                            timePicker.setIs24HourView(true)
                            timePicker.currentHour = it.get(Calendar.HOUR_OF_DAY)
                            timePicker.currentMinute = it.get(Calendar.MINUTE)
//                            timePicker.setOnTimeChangedListener { view, hourOfDay, second ->
//
//                            }
                        }

                    }.root
                )
                .setPositiveButton("确定") { _, _ ->
                    val times = bin.date.text.toString() + " " + bin.timePicker.currentHour.toString()+ ":" + bin.timePicker.currentMinute.toString() + ":00"
                    var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val dates = formatter.parse(times)
                    val time = formatter.format(dates)
//                    val time = dates.getTime()
                    viewModel.doAction(it,time)
                }
                .setNegativeButton("取消"){ _, _ -> }

            dialog.show()
        })
        setSupportActionBar(toolbar)
//        viewPager.adapter = TaskStateAdapter(supportFragmentManager)

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
        viewModel.task.observe(this, Observer {
            adapter.patients=it.patients.toList()
        })
        viewModel.cancelResult.observe(this, Observer {
            if(it) {
                dictFragment.dismiss()
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                NEW_PATIENT_REQUEST, BASE_INFO -> {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task_info,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when(item?.itemId){
            R.id.cancel_task->{
                dictFragment.show(supportFragmentManager,TaskSheetFragment.TAG)
                true
            }

            R.id.show_doctor-> {
                val intent = Intent(this, DispatchCarActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, taskId)
                }
                startActivity(intent)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun cancel(dict:Dictionary){

        viewModel.cancel(dict.id)
    }
    private fun showPatient(patient: Patient){

        Intent(this, WorkingActivity::class.java).let {
            it.putExtra(ProfileActivity.PATIENT_ID,patient.id)
            it.putExtra("PRE",true)
            startActivityForResult(it, NEW_PATIENT_REQUEST)
        }
    }

}
//
//class TaskStateAdapter(fm: FragmentManager) :
//    FragmentPagerAdapter(fm) {
//
//    private val statusFragments:List<Fragment> by lazyFast {
//        (0..2).map {
//            when (it) {
//                DoMinaActivity.START_POSITION ->ProcessFragment()
//                DoMinaActivity.PATIENTS_POSITION -> PatientManagerFragment()
//                DoMinaActivity.GIS_POSITION ->GisFragment()
//
//                else -> throw IllegalStateException("Unknown index $it")
//            }
//        }
//    }
//
//    override fun getItem(position: Int): androidx.fragment.app.Fragment {
//
//        return statusFragments[position]
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return when (position) {
//            DoMinaActivity.START_POSITION ->"任务进度"
//            DoMinaActivity.PATIENTS_POSITION -> "病人信息"
//            DoMinaActivity.GIS_POSITION ->"轨迹"
//
//            else -> throw IllegalStateException("Unknown index $position")
//        }
//    }
//
//    override fun getCount(): Int {
//        return statusFragments.size
//    }
//
//
//
//}
