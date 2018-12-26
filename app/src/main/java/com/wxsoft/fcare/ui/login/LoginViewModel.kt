package com.wxsoft.fcare.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.LoginInfo
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.AccountApi
import com.wxsoft.fcare.data.toWxResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val sharedPreferenceStorage: SharedPreferenceStorage,
                                         private val accountApi: AccountApi,
                                         private val gson: Gson):ViewModel() {


    var name:String = ""
    var password:String = ""
    var registrationId:String =""
    /**
     *获取病人信息
     */
    private val loadAccountResult = MediatorLiveData<Resource<Account>>()
    /**
     * 是否正在夹在数据
     */
    val isLoading: LiveData<Boolean>

    val account:LiveData<Account?>
    val logined:LiveData<Boolean>

    init {
        attemptLogin()
        account=loadAccountResult.map {
            var a=(it as? Resource.Success)?.data

            if(a==null){

            }else{
                sharedPreferenceStorage.loginedName=name
                sharedPreferenceStorage.loginedPassword=password
                sharedPreferenceStorage.userInfo=gson.toJson(a)
            }
            return@map a
        }
        logined=loadAccountResult.map {
            it is Resource.Success
        }
        isLoading = loadAccountResult.map { it == Resource.Loading }
    }

    fun attemptLogin(){

        name=sharedPreferenceStorage.loginedName?:""
        password=sharedPreferenceStorage.loginedPassword?:""


        if(isNameValid(name) && isPasswordValid(password))
            login()
    }

    fun login(){
        registrationId=sharedPreferenceStorage.registrationId?:""
        if(isNameValid(name)&& isPasswordValid(password)) {
            val loginInfo = LoginInfo(name, password, registrationId)
            accountApi.login(loginInfo).toWxResource()

                .subscribe { result ->
                    loadAccountResult.value = result
                }
        }
    }


    private fun isNameValid(email: String): Boolean {
        return email.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    fun setRegistration(id:String){
        sharedPreferenceStorage.registrationId=id
    }
}