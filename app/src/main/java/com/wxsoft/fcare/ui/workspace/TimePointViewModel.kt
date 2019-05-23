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
    val liveData:LiveData<List<Any>>
    private val loadTimepointsResult=MediatorLiveData<List<Any>>()

    val indexData:LiveData<Int>
    private val saveTimepointsResult=MediatorLiveData<Int>()

    init {
        liveData=loadTimepointsResult.map { it }
        indexData=saveTimepointsResult.map { it }
    }

    private fun loadTimePoints(id:String){
        disposable.add(qualityControlApi.getTimePoints(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doTimePoints,::error))
    }

    fun loadTimePoints(){
        loadTimePoints(patientId)
    }

    private fun saveTimePoint(point: TimePoint){
        disposable.add(qualityControlApi.saveTimePoints(TimePointChange(patientId,point.excutedAt,point.tableName,point.fieldName))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveTimePoints,::error))
    }

    private fun doTimePoints(response:Response<List<List<TimePoint>>>){

        val merged = mutableListOf<Any>()
        var emptyList:List<TimePoint>?=null
        for(list in response.result?: emptyList()){

            if(emptyList.isNullOrEmpty() && list.isNotEmpty() && list[0].excutedAt.isNullOrEmpty()){
                emptyList=list
            }else {
                val title = list.firstOrNull { it.excutedAt?.isNotEmpty() ?: false }?.excutedAt?.substring(0, 10) ?: ""
                if (title.isNotEmpty()) {
                    merged += TimePointHead(title)
                }
                merged.addAll(list)
            }
        }
        emptyList?.let(merged::addAll)
        loadTimepointsResult.value=merged
    }

    private fun saveTimePoints(response:Response<String>) = if(response.success) {
        loadTimePoints(patientId)
//        liveData.value?.apply {
//            val firstOne=
//
//                (firstOrNull { it is TimePoint && it.id == currentPoint?.id } as TimePoint).apply {
//                excutedAt = currentPoint?.excutedAt
//                currentPoint = null
//            }
//
//            saveTimepointsResult.value=this.indexOf(firstOne)
//        }

        messageAction.value = Event(if (response.success) "保存成功" else response.msg)
    }else{
        messageAction.value = Event(response.msg)
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
