package com.wxsoft.fcare.ui.details.dominating

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.dominating.fragment.PatientManagerFragment
import com.wxsoft.fcare.ui.details.dominating.fragment.ProcessFragment
import com.wxsoft.fcare.utils.lazyFast
import com.wxsoft.fcare.utils.viewModelProvider
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
        const val PATIENT_INFO_POSITION = 2

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = DataBindingUtil.setContentView<ActivityDoMinaBinding>(
            this,
            R.layout.activity_do_mina
        ).apply {
            setLifecycleOwner(this@DoMinaActivity)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        taskId=intent.getStringExtra(TASK_ID)?:""
        viewModel=viewModelProvider(factory)
        binding.viewModel=viewModel

        viewModel.task.observe(this, Observer{
            it ?: return@Observer

            viewPager.adapter = TaskStateAdapter(supportFragmentManager, it)
        })
        viewModel.taskId=taskId

        seekBar.setOnTouchListener { v, event ->  true }

        viewModel.mesAction.observe(this, EventObserver {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        viewModel.atAction.observe(this,EventObserver{

            val mess=when(it){
                1->"确认到达现场吗?"
                2->"确认开始首次医疗接触吗?"
                3->"确认开始返回医院吗?"
                4->"确认到达医院大门吗?"
                else->throw IllegalStateException("Unknown type $it")
            }
            val dialog=AlertDialog.Builder(this).setMessage(mess)
                .setPositiveButton("确定") { _, _ -> viewModel.doAction(it) }
                .setNegativeButton("取消"){ _, _ -> }

            dialog.show()
        })


    }

    inner class TaskStateAdapter(fm: FragmentManager, private val task: Task) :
        FragmentPagerAdapter(fm) {

        private val statusFragments:List<Fragment> by lazyFast {
            (0..4).map {
                when (it) {
                    PATIENT_INFO_POSITION -> PatientManagerFragment.newInstance("", "")
                    else -> ProcessFragment.newInstance(it)
                }
            }
        }

        override fun getItem(position: Int): Fragment {

            return statusFragments[position]
        }

        override fun getCount(): Int {
            return STATE_COUNT
        }
    }


}
