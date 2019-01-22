package com.wxsoft.fcare.ui.main.fragment.profile

import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {


    fun loginOut() {
        sharedPreferenceStorage.loginedPassword = ""
        sharedPreferenceStorage.loginedName = ""
        sharedPreferenceStorage.userInfo = ""
    }

}
