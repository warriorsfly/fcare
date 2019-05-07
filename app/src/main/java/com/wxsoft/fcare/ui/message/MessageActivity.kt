package com.wxsoft.fcare.ui.message

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.di.ViewModelFactory
import com.wxsoft.fcare.core.utils.viewModelProvider
import com.wxsoft.fcare.databinding.ActivityMessageBinding
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.utils.VirateUtil
import javax.inject.Inject

class MessageActivity : BaseActivity()  {

    private lateinit var title:String
    private lateinit var content:String
    private lateinit var extra:String
    companion object {
        const val TITLE = "TITLE"
        const val CONTENT = "CONTENT"
        const val EXTRA = "EXTRA"
    }

    private lateinit var viewModel: MessageViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    var isVirating:Boolean = false

    lateinit var binding: ActivityMessageBinding
    private lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMessageBinding>(this, R.layout.activity_message)
            .apply {
                lifecycleOwner = this@MessageActivity
            }
        viewModel = viewModelProvider(factory)
        title=intent.getStringExtra(TITLE)?:""
        content=intent.getStringExtra(CONTENT)?:""
        extra=intent.getStringExtra(EXTRA)?:""
        viewModel.extra = extra
        binding.viewModel = viewModel
        binding.title.setText(title)
        binding.title1.setText(content)

        viewModel.message.observe(this, Observer {
            onBackPressed()
            stopRingVibrate()
        })

        viewModel.patient.observe(this, Observer {  })
        startRing()

    }

    fun startRing(){
        playRing()
        if (!isVirating){
            isVirating = true
//            val longArray = longArrayOf(1000, 1000, 1000, 1000)
            VirateUtil.vibrate(this@MessageActivity, 1000)
        }
    }

    fun stopRingVibrate(){
        stopRing()
        //关闭震动
        if (isVirating) {
            isVirating = false;
            VirateUtil.virateCancle(this@MessageActivity);
        }
    }

    //开始播放
    fun playRing() {
        try {
//            val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)//用于获取手机默认铃声的Uri
//            mMediaPlayer = MediaPlayer()
//            mMediaPlayer.setDataSource(this@MessageActivity, alert)
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING)//告诉mediaPlayer播放的是铃声流
//            mMediaPlayer.setLooping(true)
//            mMediaPlayer.prepare()
//            mMediaPlayer.start()
            val uri = "android.resource://" + this.getPackageName() + "/"+R.raw.mysound
            RingtoneManager.getRingtone(this, Uri.parse(uri)).play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //停止播放
    fun stopRing() {
//        if (mMediaPlayer != null) {
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop()
//                mMediaPlayer.release()
//            }
//        }
        val uri = "android.resource://" + this.getPackageName() + "/"+R.raw.mysound;
        RingtoneManager.getRingtone(this, Uri.parse(uri)).stop()
    }

}
