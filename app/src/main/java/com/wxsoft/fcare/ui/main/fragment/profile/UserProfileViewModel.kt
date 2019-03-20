package com.wxsoft.fcare.ui.main.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {

    val user:LiveData<Account>

    private val liveAccount=MediatorLiveData<Account>()

    init {
        user=liveAccount.map { it }
        liveAccount.value=account
    }



    fun loginOut() {
        sharedPreferenceStorage.loginedPassword = ""
        sharedPreferenceStorage.loginedName = ""
        sharedPreferenceStorage.userInfo = ""
    }

}
