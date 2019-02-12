package com.wxsoft.fcare.ui.message

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.MeasuresApi
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                          private val measuresApi: MeasuresApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter{
    override var title: String=""
        get() = "准备通知"
    override val clickableTitle: String
        get() = ""
    override val clickable: LiveData<Boolean>
        get() = MediatorLiveData<Boolean>().apply {
            value=false
        }

    override fun click(){}

    val message:LiveData<String>
    private val _message=MediatorLiveData<String>().apply {
        value="请即刻启动导管室"
    }

    init{
        message=_message.map { it }
    }

    val patientType:LiveData<String>
    private val _patientType=MediatorLiveData<String>().apply {
        value="胸痛病人"
    }

    init{
        patientType=_patientType.map { it }
    }


}