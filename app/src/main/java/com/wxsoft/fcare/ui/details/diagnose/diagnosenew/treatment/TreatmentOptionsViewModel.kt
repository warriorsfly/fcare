package com.wxsoft.fcare.ui.details.diagnose.diagnosenew.treatment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TreatmentOptionsViewModel @Inject constructor(private val dictEnumApi: DictEnumApi,
                                                    override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    var treatId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val options: LiveData<List<Dictionary>>
    val loadOptions = MediatorLiveData<List<Dictionary>>()

    val submitSuccess: LiveData<Boolean>
    val loadSubmitSuccess = MediatorLiveData<Boolean>()

    init {
        submitSuccess = loadSubmitSuccess.map { it }
        options = loadOptions.map { it?: emptyList()}
    }

    fun loadTreatments(){
        disposable.add(dictEnumApi.loadTreatments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun getData(response: List<Dictionary>){
        loadOptions.value = response
        haveData()
    }

    fun clickOption(item:Dictionary){
        options.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
        loadSubmitSuccess.value = true
    }

    fun haveData(){
        options.value?.filter { it.id.equals(treatId) }?.map { it.checked = true }
    }


}