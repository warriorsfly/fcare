package com.wxsoft.fcare.ui.details.trajectory

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.result.EventObserver
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityTrajectoryBinding
import com.wxsoft.fcare.service.JPushReceiver
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.details.measures.MeasuresActivity
import com.wxsoft.fcare.ui.details.strategy.StrategyAdapter
import com.wxsoft.fcare.ui.login.LoginViewModel
import com.wxsoft.fcare.utils.VirateUtil
import kotlinx.android.synthetic.main.layout_new_title.*
import javax.inject.Inject

class TrajectoryActivity : BaseActivity() {

    private lateinit var patientId:String
    companion object {
        const val PATIENT_ID = "PATIENT_ID"
        const val DialogsisCode = "Dialogsis"
    }


    private var receiver: RefreshLineBroadcastReceiver? = null

    private lateinit var viewModel: TrajectoryViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var binding: ActivityTrajectoryBinding

    private lateinit var adapter: TrajectoryAdapter

    var isVirating:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(factory)
        binding = DataBindingUtil.setContentView<ActivityTrajectoryBinding>(this, R.layout.activity_trajectory)
            .apply {
                adapter = TrajectoryAdapter(this@TrajectoryActivity)
                list.adapter = this@TrajectoryActivity.adapter
                lifecycleOwner = this@TrajectoryActivity
            }

        patientId=intent.getStringExtra(MeasuresActivity.PATIENT_ID)?:""
        binding.viewModel = viewModel
        viewModel.patientId = patientId


        receiver = RefreshLineBroadcastReceiver(viewModel)
        val intentFilter = IntentFilter()
        // 2. 设置接收广播的类型
        intentFilter.addAction("RefreshRfidTimeline")
        registerReceiver(receiver, intentFilter)



        viewModel.travelTimeLine.observe(this, Observer {
            adapter.submitList(it.triggerRecordLists)
            title="自动采集时间点" + "   " +it.patientInfoDto.name
        })

        viewModel.modefyStr.observe(this, Observer {
            when(it){
                "stop" ->{stopRingVibrate()}
                "RefreshRfidTimeline" ->{startRing()}
            }

        })

        setSupportActionBar(toolbar)

        viewModel.mesAction.observe(this, EventObserver{
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        })

    }



    fun startRing(){
        playRing()
        if (!isVirating){
            isVirating = true
//            val longArray = longArrayOf(1000, 1000, 1000, 1000)
            VirateUtil.vibrate(this@TrajectoryActivity, 1000)
        }
    }

    fun stopRingVibrate(){
        stopRing()
        //关闭震动
        if (isVirating) {
            isVirating = false;
            VirateUtil.virateCancle(this@TrajectoryActivity)
        }
    }

    //开始播放
    fun playRing() {
        try {
            val uri = "android.resource://" + this.getPackageName() + "/"+R.raw.mysound
            RingtoneManager.getRingtone(this, Uri.parse(uri)).play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //停止播放
    fun stopRing() {
        val uri = "android.resource://" + this.getPackageName() + "/"+R.raw.mysound;
        RingtoneManager.getRingtone(this, Uri.parse(uri)).stop()
    }



    class RefreshLineBroadcastReceiver(var vm: TrajectoryViewModel?) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val pid = intent?.getStringExtra("RefreshRfidTimeline")
            pid?.apply {
                vm?.loadModefyStr?.value = "RefreshRfidTimeline"
                vm?.getTreaatment()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver?.let {
            unregisterReceiver(receiver)
            receiver?.vm = null
        }
    }

}
