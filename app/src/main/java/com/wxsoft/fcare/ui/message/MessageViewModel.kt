package com.wxsoft.fcare.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.CustomMessage
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.MeasuresApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                          private val measuresApi: MeasuresApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    /**
     * 病人id
     */
    var extra: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val model: CustomMessage by lazy {  gon.fromJson(extra, CustomMessage::class.java)}


    val message:LiveData<String>
    private val _message=MediatorLiveData<String>().apply {
        value="请即刻启动导管室"
    }




    init {
        message=_message.map { it }


    }

    fun reject(){

    }


}