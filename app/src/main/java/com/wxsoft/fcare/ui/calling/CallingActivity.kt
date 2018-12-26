package com.wxsoft.fcare.ui.calling

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import com.google.gson.Gson
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.NotificationInfo
import com.wxsoft.fcare.di.ViewModelFactory
import com.wxsoft.fcare.ui.BaseActivity
import com.wxsoft.fcare.ui.detail.PatientDetailActivity
import com.wxsoft.fcare.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_calling.*
import javax.inject.Inject
import android.media.RingtoneManager



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CallingActivity : BaseActivity() {

    @Inject lateinit var factory:ViewModelFactory

    @Inject
    lateinit var gson: Gson
    private lateinit var viewModel:CallingViewModel
    private lateinit var media: MediaPlayer


    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calling)


        viewModel = viewModelProvider(factory)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isInteractive) {
            val wl = pm.newWakeLock((PowerManager.ACQUIRE_CAUSES_WAKEUP or FLAG_KEEP_SCREEN_ON), WAKE_UP_TAG)
            wl.acquire()
            wl.release()
        }


        val message=intent.getStringExtra("notify")
//        notify=gson.fromJson(message,NotificationInfo::class.java)
        viewModel.patientId=message
        fullscreen_content.text="胸痛通知"

        submit.setOnClickListener {
            viewModel.answer()
            finish()
        }


        val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        media= MediaPlayer()

        media.setDataSource(this,alert)
        media.prepare()
        media.isLooping = true;

        media.start();
    }

    override fun onDestroy() {
        media?.stop()
        media?.release()
        super.onDestroy()
    }
    fun toDetail(id:String){
        var intent= Intent(this, PatientDetailActivity::class.java)
        intent.putExtra(PatientDetailActivity.PATIENT_ID,id)
        startActivity(intent)
    }


    companion object {

        val WAKE_UP_TAG="bright"

    }
}
