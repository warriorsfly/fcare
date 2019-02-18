package com.wxsoft.fcare.ui.details.dominating

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.*
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_do_mina.*
import kotlinx.android.synthetic.main.layout_task_process_title.*
import javax.inject.Inject

class DoMinaActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel:DoMinaViewModel

    private lateinit var taskId:String

    companion object {
        const val TASK_ID = "TASK_ID"
        const val STATE_COUNT = 5
        const val START_POSITION = 0
        const val ARRIVE_POSITION = 1
        const val PATIENT_INFO_POSITION = 2
        const val RETUNING_POSITION = 3
        const val ARRIVE_HOS_POSITION = 4

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        back.setOnClickListener { onBackPressed() }
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

        viewModel.pageAction.observe(this,EventObserver{
            viewPager.setCurrentItem(it,true)
        })
        viewPager.adapter = TaskStateAdapter(supportFragmentManager)

        add_patient.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra(ProfileActivity.TASK_ID, taskId)
                putExtra(ProfileActivity.PATIENT_ID, "")
            }
            startActivityForResult(intent,NEW_PATIENT_REQUEST)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK) {
            when (requestCode) {
                NEW_PATIENT_REQUEST -> {
//                    data?.getStringExtra(NEW_PATIENT_ID)
                    viewModel.loadTask()
                }
            }
        }
    }

}

class TaskStateAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val statusFragments:List<Fragment> by lazyFast {
        (0..4).map {
            when (it) {
                DoMinaActivity.START_POSITION ->ProcessStartFragment()
                DoMinaActivity.ARRIVE_POSITION ->ProcessArriveFragment()
                DoMinaActivity.PATIENT_INFO_POSITION -> PatientManagerFragment()
                DoMinaActivity.RETUNING_POSITION ->ProcessReturningFragment()
                DoMinaActivity.ARRIVE_HOS_POSITION ->ProcessArriveHosFragment()

                else -> throw IllegalStateException("Unknown index $it")
            }
        }
    }

    override fun getItem(position: Int): Fragment {

        return statusFragments[position]
    }

    override fun getCount(): Int {
        return statusFragments.size
    }

}
