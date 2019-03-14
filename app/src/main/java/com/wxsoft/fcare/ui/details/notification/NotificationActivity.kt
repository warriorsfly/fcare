package com.wxsoft.fcare.ui.details.notification

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityNotificationBinding
import com.wxsoft.fcare.ui.BaseActivity
import javax.inject.Inject

class NotificationActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
    }

    private lateinit var viewModel: NotificationViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var checkedAdapter: NotificationCheckedAdapter
    lateinit var userAdapter: NotiGroupAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        checkedAdapter = NotificationCheckedAdapter(this,viewModel)
        DataBindingUtil.setContentView<ActivityNotificationBinding>(this, R.layout.activity_notification)
            .apply {
                viewModel = this@NotificationActivity.viewModel
                checkedList.adapter = checkedAdapter
                userAdapter = NotiGroupAdapter(this@NotificationActivity, this@NotificationActivity.viewModel)
                doctorsList.adapter = userAdapter
                back.setOnClickListener { onBackPressed() }
                lifecycleOwner = this@NotificationActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel.patientId = patientId

        viewModel.userItems.observe(this, Observer {
            userAdapter.items = it
        })

        viewModel.checkedUsers.observe(this, Observer {
            checkedAdapter.submitList(it)})

    }
}
