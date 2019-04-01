package com.wxsoft.fcare.ui.details.notification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.NotifyType
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityNotificationBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.vitalsigns.VitalSignsActivity
import com.wxsoft.fcare.ui.selecter.SelecterOfOneModelActivity
import javax.inject.Inject

class NotificationActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val SELECT_NOTIFY_TYPE = 100
    }

    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding: ActivityNotificationBinding
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var checkedAdapter: NotificationCheckedAdapter
    lateinit var userAdapter: NotiGroupAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityNotificationBinding>(this, R.layout.activity_notification)
            .apply {
                viewModel = this@NotificationActivity.viewModel
                checkedAdapter = NotificationCheckedAdapter(this@NotificationActivity,this@NotificationActivity.viewModel)
                checkedList.adapter = checkedAdapter
                userAdapter = NotiGroupAdapter(this@NotificationActivity, this@NotificationActivity.viewModel)
                doctorsList.adapter = userAdapter
                back.setOnClickListener { onBackPressed() }
                notifyType.setOnClickListener{
                    toSelectNotifyType()
                }
                lifecycleOwner = this@NotificationActivity
            }
        patientId=intent.getStringExtra(PATIENT_ID)?:""
        viewModel.patientId = patientId

        viewModel.userItems.observe(this, Observer {
            userAdapter.items = it
        })

        viewModel.checkedUsers.observe(this, Observer {
            checkedAdapter.submitList(it)})

        viewModel.notify.observe(this, Observer {})

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

        viewModel.backToLast.observe(this, Observer {
            onBackPressed()
        })

    }




    fun toSelectNotifyType(){
        val intent = Intent(this, SelecterOfOneModelActivity::class.java).apply {
            putExtra(SelecterOfOneModelActivity.PATIENT_ID, patientId)
            putExtra(SelecterOfOneModelActivity.COME_FROM, "Notify")
        }
        startActivityForResult(intent, SELECT_NOTIFY_TYPE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK) {
            when(requestCode){
                SELECT_NOTIFY_TYPE ->{//
                    val notyfiType= data?.getSerializableExtra("SelectNotify") as NotifyType
                    binding.notifyType.setText(notyfiType.descrition)
                    viewModel.notify.value?.messageTemplateName = notyfiType.tag
                }

            }
        }
    }

}
