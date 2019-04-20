package com.wxsoft.fcare.ui.details.strategy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Strategy
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class StrategyViewModel  @Inject constructor(private val api: PACSApi,
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
            getStrategyItems()
        }

    val saveResult: LiveData<String>
    private val loadsaveResult = MediatorLiveData<String>()

    val strategyItems: LiveData<List<Dictionary>>
    private val loadStrategyItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val strategy: LiveData<Strategy>
    private val loadStrategy = MediatorLiveData<Resource<Response<Strategy>>>()

    init {
        strategyItems = loadStrategyItemsResult.map { (it as? Resource.Success)?.data?: emptyList()  }
        strategy = loadStrategy.map { (it as? Resource.Success)?.data?.result?: Strategy("") }
        saveResult = loadsaveResult.map { it }
    }

    fun getStrategyItems(){
        dicEnumApi.loadStrategys(patientId).toResource()
            .subscribe {
                loadStrategyItemsResult.value = it
                getStrategy()
            }
    }

    fun getStrategy(){
        api.getStrategy(patientId).toResource()
            .subscribe {
                result ->
                when (result) {
                    is Resource.Success -> {
                        loadStrategy.value = result
                        haveData()
                    }
                    is Error -> {
                        messageAction.value = Event(result.message ?: "")
                    }
                }
            }
    }


    fun save(){
        api.saveStrategy(strategy.value!!).toResource()
            .subscribe {
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

    fun haveData(){
        strategyItems.value?.filter { it.id.equals(strategy.value?.strategyCode) }?.map { it.checked = true }
    }



    fun clickSelect(item:Dictionary){
        strategyItems.value?.filter{it.checked}?.map { it.checked = false }
        item.checked = true
        strategy.value?.strategyCode = item.id
        click()
    }


    fun click() {
        strategy.value?.patientId = patientId
        save()
    }

}