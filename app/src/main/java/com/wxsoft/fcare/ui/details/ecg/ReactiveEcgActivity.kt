package com.wxsoft.fcare.ui.details.ecg

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.wxsoft.fcare.R
import com.wxsoft.fcare.ui.BaseActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.completable.CompletableDisposeOn
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reactive_ecg.*
import java.util.concurrent.TimeUnit

class ReactiveEcgActivity : BaseActivity() {
    private val disposable= CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reactive_ecg)
        close.setOnClickListener { finish() }

        disposable.add(
            Single.fromCallable { doStart() }
                .delay(1, TimeUnit.SECONDS).subscribe(::doLand, ::error)
        )
    }

    fun doLand(st:Boolean){
        if(!st) {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

//    override fun onRestart() {
//
//        super.onRestart()
//        disposable.add(
//            Single.fromCallable { doStart() }
//                .delay(1, TimeUnit.SECONDS).subscribe(::doLand, ::error)
//        )
//        image.visibility=if(requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)View.VISIBLE else View.GONE
//        close.visibility=if(requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)View.VISIBLE else View.GONE
//
//    }
//
//


    private fun doStart()=requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image.visibility=View.VISIBLE
            close.visibility=View.VISIBLE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            image.visibility=View.GONE
            close.visibility=View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }
}
