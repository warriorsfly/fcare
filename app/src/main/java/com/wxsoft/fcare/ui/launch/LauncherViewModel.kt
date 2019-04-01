package com.wxsoft.fcare.ui.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.version.Version
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.remote.version.VersionApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LauncherViewModel @Inject constructor(
    private val accountApi: AccountApi,
    private val api: VersionApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    var name: String = ""
    var password: String = ""
    private var registrationId: String = ""
    /**
     *获取病人信息
     */
    private val loadAccountResult = MediatorLiveData<Response<Account>>()
    /**
     * 是否登陆成功
     */
    val success: LiveData<Boolean>

    var local:Long=0
        set(value){
            field=value
            loadVersion()
        }

    val version:LiveData<Version>
    private val loadVersionResult=MediatorLiveData<Response<Version>>()

    init {
        success = loadAccountResult.map { it.success }
        version=loadVersionResult.map {
            it.result?: Version()
        }
    }

    private fun startLogin(storageOK:Boolean) {

        if (storageOK) {
            login()
        }else{
            loadAccountResult.value= Response(false)
        }
    }

    private fun loadVersion() {
        disposable.add(api.check(local)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::checkUpdate,::error))
    }

    fun login() {
        val loginInfo = LoginInfo(name, password)
        disposable.add(
            accountApi.login(loginInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    loadAccountResult.value = it
                },::error))
    }

    private fun checkUpdate(response: Response<Version>){
        loadVersionResult.value=response.apply {
            result?.let {
//                if(it.changing) {
                if(!it.changing) {
                    disposable.add(
                        Single.fromCallable { isLoginValid() }.delay(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(::startLogin, ::error)
                    )
                }
            }
        }

    }

    private fun isLoginValid(): Boolean {
        name = sharedPreferenceStorage.loginedName ?: ""
        password = sharedPreferenceStorage.loginedPassword ?: ""
        return name.isNotEmpty() &&  password.isNotEmpty()
    }

    fun setRegistration(id: String) {
        sharedPreferenceStorage.registrationId = id
    }
}