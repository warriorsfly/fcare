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
    }

    fun doLand(st:Boolean){
        if(st) {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        }
    }

    fun showImage(st:Boolean){
        image.visibility=if(st)View.VISIBLE else View.GONE
    }

    override fun onRestart() {

        super.onRestart()
        disposable.add(
            Single.fromCallable { doStart() }
                .delay(1, TimeUnit.SECONDS).subscribe(::doLand, ::error)
        )
        image.visibility=if(requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)View.VISIBLE else View.GONE
        close.visibility=if(requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)View.VISIBLE else View.GONE

    }


    fun doStart()=requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }
}
