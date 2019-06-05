package com.wxsoft.fcare.ui.main.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(private val accountApi: AccountApi,
                                               override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {

    val passChanged:LiveData<Boolean>
    private val changePassworResult=MediatorLiveData<Boolean>()
    val user:LiveData<Account>

    private val liveAccount=MediatorLiveData<Account>()

    init {
        user=liveAccount.map { it }
        liveAccount.value=account
        passChanged=changePassworResult.map { it }
    }



    fun loginOut() {
        sharedPreferenceStorage.loginedPassword = ""
        sharedPreferenceStorage.loginedName = ""
        sharedPreferenceStorage.userInfo = ""
    }

    fun changePassword(pass:String,newPass:String){
        disposable.add(accountApi.changePassWord(account.id,pass,newPass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.success){
                    sharedPreferenceStorage.loginedPassword=""
                    changePassworResult.value=true
                }else{
                    messageAction.value= Event(it.msg)
                }

            },::error))

    }
    fun checkOldPassWord(pass:String)=sharedPreferenceStorage.loginedPassword==pass


}
