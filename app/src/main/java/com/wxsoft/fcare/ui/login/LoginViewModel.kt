package com.wxsoft.fcare.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.EndPoint
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val sharedPreferenceStorage: SharedPreferenceStorage,
    private val accountApi: AccountApi,
    private val gson: Gson,
    val endpoints:List<EndPoint>
) : ViewModel() {

    private val disposable= CompositeDisposable()
    var name: String = ""
    var password: String = ""
    private var registrationId: String = ""
    /**
     *获取病人信息
     */
    private val loadAccountResult = MediatorLiveData<Resource<Response<Account>>>()
    /**
     * 是否正在夹在数据
     */
    val isLoading: LiveData<Boolean>

    val account: LiveData<Response<Account>>
    val logined: LiveData<Boolean>
    val hosName=ObservableField<String>()
    var endpoint=0
    set(value){
        field=value
        hosName.set(endpoints[field].hospital)
        if(field!=sharedPreferenceStorage.endPointIndex) {
            sharedPreferenceStorage.endPointIndex = field
            sharedPreferenceStorage.endPointChanged = true
        }
    }

    /**
     * 需要传递出去的toast消息
     */
    val messageAction = MutableLiveData<Event<String>>()
    val mesAction: LiveData<Event<String>>
        get() = messageAction


    init {
        endpoint=if(sharedPreferenceStorage.endPointIndex<0)1 else sharedPreferenceStorage.endPointIndex
        attemptLogin()
        account = loadAccountResult.map {

            val acc = (it as? Resource.Success)?.data ?: Response(false)
            when (it) {
                is Resource.Error -> {
                    messageAction.value=Event(it.throwable.message?:"")
                }
                is Resource.Success -> {
                    if (it.data.success) {
                        sharedPreferenceStorage.loginedName = name
                        sharedPreferenceStorage.loginedPassword = password
                        sharedPreferenceStorage.userInfo = gson.toJson(it.data.result)
                    }else{
                        messageAction.value=Event(it.data.msg)
                    }
                }
            }
            return@map acc
        }
        logined = loadAccountResult.map {
            it is Resource.Success && it.data.success
        }
        isLoading = loadAccountResult.map {
            it == Resource.Loading
        }
    }

    private fun attemptLogin() {

        name = sharedPreferenceStorage.loginedName ?: ""
        password = sharedPreferenceStorage.loginedPassword ?: ""


        if (isNameValid(name) && isPasswordValid(password))
            login()
    }

    fun login() {
        registrationId = sharedPreferenceStorage.registrationId ?: ""
        if (isNameValid(name) && isPasswordValid(password)) {
            val loginInfo = LoginInfo(name, password)
            disposable.add(accountApi.login(loginInfo).toResource().subscribe {
                loadAccountResult.value = it
            })
        }
    }

    private fun isNameValid(email: String): Boolean {
        return email.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    fun setRegistration(id: String) {
        sharedPreferenceStorage.registrationId = id
    }
}