package com.wxsoft.fcare.ui.main.fragment.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.Hospital
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
    val hospitals:LiveData<List<Hospital>>
    private val loadHospitals=MediatorLiveData<List<Hospital>>()

    private val liveAccount=MediatorLiveData<Account>()

    val hospitalChanged:LiveData<Boolean>
    private val changeHospitalResult=MediatorLiveData<Boolean>()

    var hospitalName= ObservableField<String>()

    init {
        user=liveAccount.map { it }
        hospitals = loadHospitals.map { it?: emptyList() }
        hospitalName.set(account.hospitalName)
        liveAccount.value=account
        passChanged=changePassworResult.map { it }
        hospitalChanged=changeHospitalResult.map { it }
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

    fun getAllHospital(){
        disposable.add(accountApi.getAllHospital()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.success){
                    loadHospitals.value = it.result
                }else{
                    messageAction.value= Event(it.msg)
                }

            },::error))
    }
    fun updateHospital(hospitalId:String){
        disposable.add(accountApi.updateHospital(account.id,hospitalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.success){
                    changeHospitalResult.value = it.result
                    messageAction.value= Event(it.msg)
                }else{
                    messageAction.value= Event(it.msg)
                }

            },::error))
    }



    fun selectHospital(hospital:Hospital){
        updateHospital(hospital.id)
        user.value?.hospitalName = hospital.name
        user.value?.hospitalId = hospital.id
        account.hospitalId = hospital.id
        account.hospitalName = hospital.name
        hospitalName.set(hospital.name)
    }

}
