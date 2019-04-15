package com.wxsoft.fcare.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ShareApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareItemListViewModel  @Inject constructor(private val api: ShareApi,
                                                  override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                  override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }


    val items:LiveData<List<Dictionary>>
    private val loadItemResult = MediatorLiveData<List<Dictionary>>()

    init {

        items = loadItemResult.map { it?: emptyList() }
    }

    fun loadItems(){
        api.getShareItems().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doSubscribe,::error)
    }

    fun doSubscribe(items:List<Dictionary>){
        loadItemResult.value=items
    }

}