package com.wxsoft.fcare.ui.main.fragment.patients

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.activityViewModelProvider
import com.wxsoft.fcare.databinding.LayoutListBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.main.fragment.message.MessageAdapter
import com.wxsoft.fcare.ui.patient.ProfileActivity
import com.wxsoft.fcare.ui.workspace.WorkingActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MessageFragment : DaggerFragment() {


    //定义一个回调接口
    interface CallBackValue {
        fun setMemuCount(count: Int)
    }

    private lateinit var viewModel: MessageViewModel

    //接口
    var callBackValue: CallBackValue? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var adapter: MessageAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = activity as CallBackValue?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activityViewModelProvider(viewModelFactory)

        adapter= MessageAdapter(this,::ignoreMessage)
        return LayoutListBinding.inflate(inflater, container, false).apply {
            viewModel = this@MessageFragment.viewModel
            lifecycleOwner = this@MessageFragment

            list.adapter=this@MessageFragment.adapter
            list.layoutManager=LinearLayoutManager(this@MessageFragment.context)
            val dividerItemDecoration = DividerItemDecoration(
                list.context,
                RecyclerView.VERTICAL
            )
            list.addItemDecoration(dividerItemDecoration)
            this@MessageFragment.viewModel.messages.observe(viewLifecycleOwner, Observer {
                this@MessageFragment.adapter.submitList(it)
            })

            this@MessageFragment.viewModel.totolCount.observe(viewLifecycleOwner, Observer {
                callBackValue?.setMemuCount(it)
                setHuaweiBadge(it, this@MessageFragment.context!!)
            })

        }.root
    }

    private fun ignoreMessage(message:Message,type:Int){
        if (type==1){
            val dialog = AlertDialog.Builder(this@MessageFragment.context, R.style.Theme_FCare_Dialog)
            dialog.setTitle("确认忽略后不再接收该患者此类消息提醒,是否确认忽略?")
                .setPositiveButton("确认") { _, _ -> viewModel.ignoreMessage(message.messageId) }
                .setNegativeButton("取消") { _, _ -> }
                .create().show()
        }
        if (type==2){
            Intent(activity!!, WorkingActivity::class.java).let {
                it.putExtra(ProfileActivity.PATIENT_ID,message.patientId)
                it.putExtra("PRE",false)
                startActivityForResult(it, BaseActivity.NEW_PATIENT_REQUEST)
            }

        }
    }


    private fun setHuaweiBadge(count: Int, context: Context): Boolean {
        return try {
            val launchClassName: String = getLauncherClassName(context)
            if (TextUtils.isEmpty(launchClassName)) {
                return false
            }
            val bundle = Bundle()
            bundle.putString("package", context.packageName)
            bundle.putString("class", launchClassName)
            bundle.putInt("badgenumber", count)
            context.contentResolver.call(
                Uri.parse(
                    "content://com.huawei.android.launcher" +
                            ".settings/badge/"
                ), "change_badge", null, bundle
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private fun getLauncherClassName(context: Context): String {
        val launchComponent = getLauncherComponentName(context)
        return launchComponent?.className ?: ""
    }

    private fun getLauncherComponentName(context: Context): ComponentName? {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(
            context
                .packageName
        )
        return launchIntent?.component
    }



}
