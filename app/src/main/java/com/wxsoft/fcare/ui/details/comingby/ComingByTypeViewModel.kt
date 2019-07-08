package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComingByTypeViewModel @Inject constructor(private val dictApi:DictEnumApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){


    val comingType:LiveData<List<Dictionary>>
    var patientId:String=""

    var type:Int=3
        set(value) {
            field=value
            loadData(field)
        }

    /**
     * drugId:3
     */
    private val loadTypes=MediatorLiveData<List<Dictionary>>()

    init {
        comingType=loadTypes.map { it?: emptyList() }
    }



    private fun loadData(type:Int) {

        disposable.add(when(type){
            3->dictApi.loadDicts("3")//来院方式
            5->dictApi.loadDicts("5")//科室
            18->dictApi.loadDicts("18")//救护车方式
            else-> throw IllegalArgumentException("error type $type")
        } .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({loadTypes.value=it},::error))

    }

}