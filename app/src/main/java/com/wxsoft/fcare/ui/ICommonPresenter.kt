package com.wxsoft.fcare.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.utils.fromJson

interface ICommonPresenter{
    val title:String
    val clickableTitle:String
    val clickable:LiveData<Boolean>
    fun click()
}