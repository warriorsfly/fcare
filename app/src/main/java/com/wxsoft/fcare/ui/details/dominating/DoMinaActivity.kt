package com.wxsoft.fcare.ui.details.dominating

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.databinding.ActivityDoMinaBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.layout_task_process.*
import javax.inject.Inject

class DoMinaActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel:DoMinaViewModel

    private lateinit var taskId:String

    companion object {
        const val TASK_ID = "TASK_ID"
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

        viewModel.taskId=taskId

        seekBar.setOnTouchListener { v, event ->  true }

        viewModel.taskAction.observe(this, EventObserver {
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


}
