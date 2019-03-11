package com.wxsoft.fcare.ui.details.complication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import javax.inject.Inject

class ComplicationViewModel @Inject constructor(private val api: DictEnumApi,
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
            getComplications()
        }


    val items: LiveData<List<Dictionary>>
    private val loadItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {
        clickable = clickResult.map { it }
        items = loadItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
    }


    fun getComplications(){
        api.loadComplication().toResource()
            .subscribe {
                loadItemsResult.value = it
            }
    }


    override fun click() {

    }

    fun clickSelect(item:Dictionary){
        item.checked = !item.checked
    }

}