package com.wxsoft.fcare.data.remote.log;


import android.util.Log;

public class Logger implements LogInterceptor.Logger {

    @Override
    public void log(String message) {
        Log.i("fcare http", message);
    }
}
