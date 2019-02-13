package com.wxsoft.fcare.ui.message

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityMessageBinding
import com.wxsoft.fcare.ui.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*
import javax.inject.Inject

class MessageActivity : BaseActivity()  {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val NOTIFY_TYPE = "NOTIFY_TYPE"
    }

    private lateinit var viewModel: MessageViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMessageBinding>(this, R.layout.activity_message)
            .apply {
                lifecycleOwner = this@MessageActivity
            }
        viewModel = viewModelProvider(factory)
        patientId=intent.getStringExtra(MessageActivity.PATIENT_ID)?:""
        viewModel.notifyType = intent.getStringExtra(MessageActivity.NOTIFY_TYPE)?:""
        viewModel.patientId = patientId
        binding.viewModel = viewModel

        back.setOnClickListener { onBackPressed() }
    }

}
