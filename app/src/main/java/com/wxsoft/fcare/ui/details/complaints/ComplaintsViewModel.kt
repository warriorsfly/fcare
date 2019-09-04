package com.wxsoft.fcare.ui.details.complaints

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Complain
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class ComplaintsViewModel @Inject constructor(private val api: PACSApi,
                                              private val dicEnumApi: DictEnumApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getComplaints()
        }

    val saveResult: LiveData<String>
    private val loadsaveResult = MediatorLiveData<String>()

    val complaintsItems: LiveData<List<Dictionary>>
    private val loadComplaintsItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val complaints: LiveData<List<Complain>>
    private val loadComplaints = MediatorLiveData<Resource<Response<List<Complain>>>>()


    init {
        saveResult = loadsaveResult.map { it }
        complaints = loadComplaints.map { (it as? Resource.Success)?.data?.result?: emptyList() }
        complaintsItems = loadComplaintsItemsResult.map { (it as? Resource.Success)?.data?: emptyList()  }
    }

    fun getComplaints(){
        dicEnumApi.loadComplaints(patientId).toResource()
            .subscribe {
                result ->
                when (result) {
                    is Resource.Success -> {
                        loadComplaintsItemsResult.value = result
                        getCCs()
                    }
                    is Error -> {
                        messageAction.value = Event(result.message ?: "")
                    }
                }
            }
    }

    private val array= arrayOf("221-1","221-2","221-3")

    fun clickSelect(item:Dictionary){
        item.checked = !item.checked
        if (array.contains(item.id)){
            complaintsItems.value?.filter { array.contains(it.id)&&!it.id.equals(item.id) }?.map {
                it.checked = false
            }
        }
    }

    fun click() {
        save()
    }

    fun save(){
        val items = complaintsItems.value?.filter { it.checked }?.map { Complain("").apply {
            this.patientId = this@ComplaintsViewModel.patientId
            ccCode = it.id
            ccCode_Name = it.itemName
            createdDate = DateTimeUtils.getCurrentTime()
        }}?: emptyList()
        api.saveCC(items).toResource()
            .subscribe{
                    result ->
                when (result) {
                    is Resource.Success -> {
                        messageAction.value = Event("保存成功")
                        loadsaveResult.value = "success"
                    }
                    is Error -> {
                        messageAction.value = Event(result.message ?: "")
                    }
                }
            }
    }

    fun getCCs(){
        api.getCCs(patientId).toResource()
            .subscribe {
                    result ->
                when (result) {
                    is Resource.Success -> {
                        loadComplaints.value = result
                        haveData()
                    }
                    is Error -> {
                        messageAction.value = Event(result.message ?: "")
                    }
                }
            }
    }

    fun haveData(){
        val ids = complaints.value?.map {it.ccCode}?: emptyList()
        complaintsItems.value?.filter { ids.contains(it.id) }?.map { it.checked = true }
    }


}