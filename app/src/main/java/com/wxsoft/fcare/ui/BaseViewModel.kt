package com.wxsoft.fcare.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.result.Event

abstract class BaseViewModel: ViewModel() {

    /**
     * 结果信息
     */
    protected val messageAction = MutableLiveData<Event<String>>()
    val mesAction: LiveData<Event<String>>
        get() = messageAction
}