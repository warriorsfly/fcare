package com.wxsoft.fcare.ui.detail.dialog.notify

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Department
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.NotifyApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.data.toWxResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.result.succeeded
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class NotifyViewModel @Inject constructor(private val notifyApi: NotifyApi,
                                          private val gson: Gson,
                                          private val sharedPreferenceStorage: SharedPreferenceStorage): ViewModel() {

    private lateinit var account:Account
    var patientId:String=""
    set(value) {
        field=value
        loadDepartments()
    }
    val departments:LiveData<List<Department>>

    var showNotify:LiveData<Boolean>
    val notifyed:LiveData<Response<String>>

    private val loadDepartmentsResult=MediatorLiveData<Resource<Response<List<Department>>>>()
    val notifyResult=MediatorLiveData<Resource<Response<String>>>()

    init {
        departments=loadDepartmentsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
        notifyed=notifyResult.map { (it as? Resource.Success)?.data?: Response(false) }
        showNotify=departments.map {  it!=null && it.isNotEmpty() && it.any { department ->  department.selected}}

        val userInfo=sharedPreferenceStorage.userInfo!!
        account=gson.fromJson(userInfo,Account::class.java)

    }

    private fun loadDepartments(){
        notifyApi.getNotifyWho().toResource()
            .subscribe{

                if(it.succeeded){
                    var des=(it as Resource.Success).data
                    for(de in des.result?: emptyList()){
                        de.selected=true
                    }
                }
                loadDepartmentsResult.value=it
            }
    }

    fun notifyHospital(){
        var ids=(departments.value?: emptyList()).filter { it.selected }.map { it.id }
        if(patientId.isNotEmpty() && showNotify.value == true && ids.isNotEmpty()){
            notifyApi.notify(account.id,patientId,ids).toResource().subscribe{
                notifyResult.value=it
            }
        }
    }

//    fun checkDepartment(){
//        showNotify=departments.map {  it!=null && it.isNotEmpty() && it.any { it.selected }}
//
//        ids=departments.map {
//            it?.filter { it -> it.selected }.map { it -> it.id }
//        }
//    }
}