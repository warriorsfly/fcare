package com.wxsoft.fcare.ui.details.complication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Complication
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.ThrombolysisApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComplicationViewModel @Inject constructor(private val api: DictEnumApi,
                                                private val thrombolysisApi: ThrombolysisApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title= "并发症"
    override val clickableTitle: String
        get() = "保存"
    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getComplicationItems()
        }

    /**
     * 场景
     */
    var type: String = ""
        set(value) {
            if (value == "") return
            field = value
            getComplication()
        }

    var checkedTypes: ArrayList<String> = ArrayList()


    val items: LiveData<List<Dictionary>>
    private val loadItemsResult = MediatorLiveData<List<Dictionary>>()


    val complications: LiveData<List<Complication>>
    private val loadComplicationsResult = MediatorLiveData<Response<List<Complication>>>()

    init {
        clickable = clickResult.map { it }
        items = loadItemsResult.map { it?: emptyList() }
        complications = loadComplicationsResult.map { it?.result?: emptyList()  }
    }


    fun getComplicationItems(){
        disposable.add(api.loadComplication()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadComplicationItems,::error))
    }

    fun getComplication(){
        disposable.add(thrombolysisApi.getComplication(patientId,type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadComplication,::error))
    }

    private fun save(){
        var arr = ArrayList<Complication>()
        items.value?.filter {it.checked }?.map {
            arr.add(Complication("",patientId,it.id,type))
        }
        disposable.add(thrombolysisApi.saveComplication(arr)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }


    private fun loadComplicationItems(response: List<Dictionary>){
        loadItemsResult.value = response
        haveData()
    }

    private fun loadComplication(response :Response<List<Complication>>){
        response.result?.map { checkedTypes.add(it.complicationCode) }
        loadComplicationsResult.value = response
        haveData()
    }



    private fun saveResult(response: Response<String>){
        messageAction.value= Event(response.msg)
    }

    fun haveData(){
        items.value?.filter { checkedTypes.contains(it.id) }?.map { it.checked = true }
    }

    private fun error(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"未知错误")
    }

    override fun click() {
        save()
    }

    fun clickSelect(item:Dictionary){
        item.checked = !item.checked
    }

}