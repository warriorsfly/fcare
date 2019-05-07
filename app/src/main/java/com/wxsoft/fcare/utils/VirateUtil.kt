package com.wxsoft.fcare.utils

import android.app.Service
import android.os.Vibrator
import com.wxsoft.fcare.ui.BaseActivity

class VirateUtil {

    companion object {
        //震动milliseconds毫秒
        @JvmStatic
        fun vibrate(activity: BaseActivity, milliseconds: Long) {
            val vib = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(milliseconds)
        }

        //以pattern[]方式震动
        fun vibrate(activity: BaseActivity, pattern: LongArray, repeat: Int) {
            val vib = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(pattern, repeat)
        }

        //取消震动
        fun virateCancle(activity: BaseActivity) {
            val vib = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vib.cancel()
        }
    }

}
