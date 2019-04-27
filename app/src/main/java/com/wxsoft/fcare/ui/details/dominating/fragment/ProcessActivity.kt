package com.wxsoft.fcare.ui.details.dominating.fragment


import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.ui.details.dominating.DoMinaViewModel
import com.wxsoft.fcare.core.utils.lazyFast
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.FragmentTaskProcessBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.patient.ProfileActivity
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class ProcessActivity : BaseActivity() {

    private lateinit var viewModel: DoMinaViewModel

    @Inject
    lateinit var factory: ViewModelFactory

    private val taskId: String by lazyFast {
        intent?.getStringExtra(ProfileActivity.TASK_ID)?:""
    }


    lateinit var binding: FragmentTaskProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<FragmentTaskProcessBinding>(this, R.layout.fragment_task_process).apply {
            list.adapter=ProcessAdapter(this@ProcessActivity)
            lifecycleOwner = this@ProcessActivity
            viewModel=this@ProcessActivity.viewModel
        }

        viewModel.arriving.observe(this, Observer {
            if(it){
                viewModel.arriving.value=false
                val intent = Intent(this, ProfileActivity::class.java).apply {
                    putExtra(ProfileActivity.TASK_ID, taskId)
                }
                startActivityForResult(intent, NEW_PATIENT_REQUEST)
            }
        })

        viewModel.spends.observe(this, Observer {
            ( binding.list.adapter as? ProcessAdapter)?.submitList(it)
        })

        viewModel.taskId=taskId
        setSupportActionBar(toolbar)
        title="时间进度"
    }

}
