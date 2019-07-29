package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComingByDoctorsViewModel @Inject constructor(private val doctorApi:TaskApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){

    val emergencyDoctors:LiveData<List<User>>
    val emergencyNurses:LiveData<List<User>>
    val consultantDoctors:LiveData<List<User>>
    var patientId:String=""

    var type:Int=3
    set(value) {
        field=value
        loadDoctors(field)
    }

    private val loadEmergencyDoctors=MediatorLiveData<Response<List<User>>>()
    private val loadEmergencyNurses=MediatorLiveData<Response<List<User>>>()
    private val loadConsultantDoctors=MediatorLiveData<Response<List<User>>>()
    
    val selectType=MediatorLiveData<Int>()
    val selectDoctor=MediatorLiveData<Int>()

    init {
        emergencyDoctors=loadEmergencyDoctors.map { it.result?: emptyList()}
        emergencyNurses=loadEmergencyNurses.map { it.result?: emptyList()}
        consultantDoctors=loadConsultantDoctors.map { it.result?: emptyList()}
    }

    private fun loadDoctors(type:Int){
        when(type){
            1->{
                fun doDoctor1(response: Response<List<User>>){
                    loadEmergencyDoctors.value=response
                }


                disposable.add(doctorApi.getEmergencyDoctor(patientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doDoctor1,::error))
            }
            2->{
                fun doNurse(response: Response<List<User>>){
                    loadEmergencyNurses.value=response
                }

                disposable.add(doctorApi.getEmergencyNurse(patientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doNurse,::error))
            }
            3->{
                fun doDoctor2(response: Response<List<User>>){
                    loadConsultantDoctors.value=response
                }

                disposable.add(doctorApi.getConsultantDoctor(account.id,patientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doDoctor2,::error))
            }
        }
    }

    fun changeDoctor(flag:Int){
        selectDoctor.value=flag
    }

    fun showPatients(name: String): Boolean {
        disposable.add( doctorApi.searchUser(name,type.equals(1)||type.equals(3),account.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doSearch,::error))
        return true
    }
    private fun doSearch(response: Response<List<User>>){
        when(type){
            1 -> loadEmergencyDoctors.value=response
            2 -> loadEmergencyNurses.value=response
            3 -> loadConsultantDoctors.value=response
        }
    }
}