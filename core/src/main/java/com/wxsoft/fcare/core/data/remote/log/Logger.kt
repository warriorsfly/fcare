package com.wxsoft.fcare.core.data.remote.log


import android.util.Log

class Logger : LogInterceptor.Logger {

    override fun log(message: String) {
        Log.i("fcare http", message)
    }
}
