package com.wxsoft.fcare.ui.main.fragment.profile

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import javax.inject.Inject

class UserProfileViewModel  @Inject constructor(private val sharedPreferenceStorage: SharedPreferenceStorage): ViewModel() {




    fun loginOut(){
        sharedPreferenceStorage.loginedPassword=""
        sharedPreferenceStorage.loginedName=""
        sharedPreferenceStorage.userInfo=""
    }

}
