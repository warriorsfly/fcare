package com.wxsoft.fcare.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.result.Event
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel constructor(protected open val sharedPreferenceStorage: SharedPreferenceStorage,
                                         protected open val gon:Gson): ViewModel() {

    protected val disposable= CompositeDisposable()

    protected open val account: Account by lazy {  gon.fromJson(sharedPreferenceStorage.userInfo!!,Account::class.java)}

    /**
     * 结果信息
     */
    protected val messageAction = MutableLiveData<Event<String>>()
    val mesAction: LiveData<Event<String>>
        get() = messageAction
}