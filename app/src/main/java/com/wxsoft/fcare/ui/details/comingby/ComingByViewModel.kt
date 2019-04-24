package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.ComingBy
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ComingByApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComingByViewModel @Inject constructor(private val dictApi:DictEnumApi,
                                            private val comingByApi: ComingByApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){


    val comingType:LiveData<List<Dictionary>>
    val comingFrom:LiveData<List<Dictionary>>
    val comingBy:LiveData<ComingBy>

    var patientId:String=""
    set(value) {
        field=value

    }

    /**
     * id:3
     */
    private val loadTypes=MediatorLiveData<List<Dictionary>>()
    /**
     * id:18
     */
    private val loadFroms=MediatorLiveData<List<Dictionary>>()
    private val loadComingBy=MediatorLiveData<Response<ComingBy>>()

    init {

        comingType=loadTypes.map { it?: emptyList() }
        comingFrom=loadFroms.map { it?: emptyList() }
        comingBy=loadComingBy.map { it.result?: ComingBy() }

    }

    private fun doTypes(response: Pair<List<Dictionary>,List<Dictionary>>){
        loadTypes.value=response.first
        loadFroms.value=response.second
    }
    private fun doComingBy(response: Response<ComingBy>){
        loadComingBy.value=response
    }

    fun loadComingType(){
        dictApi.loadDicts("3").zipWith(dictApi.loadDicts("18"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doTypes,::error)
    }

    fun loadComingBy(id:String){
        comingByApi.getOne(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doComingBy,::error)
    }




}