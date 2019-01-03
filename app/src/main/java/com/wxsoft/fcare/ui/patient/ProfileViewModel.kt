package com.wxsoft.fcare.ui.patient

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val sharedPreferenceStorage: SharedPreferenceStorage,
    private val gson: Gson
) : BaseViewModel() {

}