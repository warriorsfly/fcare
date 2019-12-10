package com.wxsoft.fcare.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.SignInUser
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val accountApi: AccountApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    val data: LiveData<List<SignInUser>>

    private val mData = MediatorLiveData<List<SignInUser>>()

    init {
        data = mData.map { it }
    }

    fun getSign(){
        fun gs(res: Response<List<SignInUser>>) {
            if (res.success) {
                mData.value=res.result
            }
        }
        accountApi.getSignIns("215-2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::gs,::error)
    }

    fun sign(userId:String,shiftCode:String) {
        fun si(res: Response<String>) {
            if(res.success){
                getSign()
            }
        }
        accountApi.signIn(userId,shiftCode,"215-2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::si,::error)
    }
}
