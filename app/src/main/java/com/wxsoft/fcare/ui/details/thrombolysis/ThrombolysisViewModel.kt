package com.wxsoft.fcare.ui.details.thrombolysis

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.ThrombolysisApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class ThrombolysisViewModel @Inject constructor(private val thrombolysisApi: ThrombolysisApi,
                                                private val dictEnumApi: DictEnumApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "溶栓"
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
        }

    val thrombolysis:LiveData<Thrombolysis>
    private val loadThrombolysis = MediatorLiveData<Resource<Response<Thrombolysis>>>()

    init {
        clickable = clickResult.map { it }
        thrombolysis = loadThrombolysis.map { (it as? Resource.Success)?.data?.result ?: Thrombolysis("")  }
    }

    fun loadThrombolysis(isPre:Boolean){
        thrombolysisApi.loadThrombolysis(patientId,isPre).toResource()
            .subscribe {
                loadThrombolysis.value = it
            }
    }



    override fun click() {

    }

}