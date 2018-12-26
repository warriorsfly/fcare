package com.wxsoft.fcare.ui.login

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
    private val loadAccountResult = MediatorLiveData<Resource<Response<Account>>>()
    /**
     * 是否正在夹在数据
     */
    val isLoading: LiveData<Boolean>

    val account:LiveData<Response<Account>>
    val logined:LiveData<Boolean>

    init {
        attemptLogin()
        account=loadAccountResult.map {

            val acc=(it as? Resource.Success)?.data?: Response(false)
            when(it){
                is Resource.Error->{
//                    TODO 显示错误
                }
                is Resource.Success->{
                    if(it.data.success){
                        sharedPreferenceStorage.loginedName=name
                        sharedPreferenceStorage.loginedPassword=password
                        sharedPreferenceStorage.userInfo=gson.toJson(it.data.result)
                    }
                }
            }
            return@map  acc
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
            accountApi.login(loginInfo).toResource().subscribe {
                loadAccountResult.value=it
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