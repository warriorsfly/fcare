package com.wxsoft.fcare.ui.workspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TimePointViewModel @Inject constructor(private val qualityControlApi: QualityControlApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    var patientId:String=""
        set(value) {
            field=value
            loadTimePoints(field)
        }
    /**
     * 病人信息
     */
    val liveData:LiveData<List<TimePoint>>
    private val loadTimepointsResult=MediatorLiveData<Response<List<TimePoint>>>()

    val indexData:LiveData<Int>
    private val saveTimepointsResult=MediatorLiveData<Int>()

    init {
        liveData=loadTimepointsResult.map { it.result?: emptyList() }
        indexData=saveTimepointsResult.map { it }
    }

    private fun loadTimePoints(id:String){
        disposable.add(qualityControlApi.getTimePoints(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doTimePoints,::error))
    }

    private fun saveTimePoint(point: TimePoint){
        disposable.add(qualityControlApi.saveTimePoints(point)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveTimePoints,::error))
    }

    private fun doTimePoints(response:Response<List<TimePoint>>){
        loadTimepointsResult.value=response
    }

    private fun saveTimePoints(response:Response<String>){
        if(response.success) {
            liveData.value?.apply {
                val firstOne=first { it.id == currentPoint?.id }?.apply {
                    excutedAt = currentPoint?.excutedAt
                    currentPoint = null
                }
                saveTimepointsResult.value=indexOf(firstOne)
            }

            messageAction.value = Event(if (response.success) "保存成功" else response.msg)
        }else{
            messageAction.value = Event(response.msg)
        }
    }

    private fun error(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"未知错误")
    }

    private var currentPoint:TimePoint?=null

    fun setCurrentPoint(point: TimePoint){
        currentPoint=point
    }

    fun newTime(time:String){

        currentPoint?.also {
            it.excutedAt=time

            saveTimePoint(it)
        }

    }
}
