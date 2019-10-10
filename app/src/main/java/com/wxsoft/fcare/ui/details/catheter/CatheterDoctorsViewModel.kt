package com.wxsoft.fcare.ui.details.catheter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CatheterDoctorsViewModel  @Inject constructor(private val doctorApi: InterventionApi,
                                                    private val searchApi: TaskApi,
                                                    override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    val emergencyDoctors: LiveData<List<User>>
    val consultantDoctors: LiveData<List<User>>
    var patientId:String=""

    var type:Int=3
        set(value) {
            field=value
            loadDoctors(field)
        }

    private val loadEmergencyDoctors= MediatorLiveData<Response<List<User>>>()
    private val loadConsultantDoctors= MediatorLiveData<Response<List<User>>>()

    val selectType= MediatorLiveData<Int>()
    val selectDoctor= MediatorLiveData<Int>()

    init {
        emergencyDoctors=loadEmergencyDoctors.map { it.result?: emptyList()}
        consultantDoctors=loadConsultantDoctors.map { it.result?: emptyList()}
    }

    private fun loadDoctors(type:Int){
        when(type){
            1 ->{
                fun doDoctor1(response: Response<List<User>>){
                    loadEmergencyDoctors.value=response
                }
                disposable.add(doctorApi.getInterventionDocs(account.id,patientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doDoctor1,::error))
            }
            3 ->{
                fun doDoctor3(response: Response<List<User>>){
                    loadConsultantDoctors.value=response
                }
                disposable.add(doctorApi.getInterventionDocs(account.id,patientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doDoctor3,::error))
            }
        }

    }

    fun changeDoctor(flag:Int){
        selectDoctor.value=flag
    }

    fun showPatients(name: String): Boolean {
        disposable.add( searchApi.searchUser(name,true,account.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doSearch,::error))
        return true
    }
    private fun doSearch(response: Response<List<User>>){
        when(type){
            1 -> loadEmergencyDoctors.value=response
            3 -> loadConsultantDoctors.value=response
        }
    }
}