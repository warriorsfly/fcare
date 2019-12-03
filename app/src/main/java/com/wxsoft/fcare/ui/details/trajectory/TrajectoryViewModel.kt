package com.wxsoft.fcare.ui.details.trajectory

import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TravelTimeLine
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrajectoryViewModel @Inject constructor(private val api: QualityControlApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    val pointNum = ObservableInt().apply { set(0) }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getTreaatment()
        }
    val travelTimeLine: LiveData<TravelTimeLine>
    private val loadTravelTimeLine = MediatorLiveData<TravelTimeLine>()

    val modefyStr: LiveData<String>
    val loadModefyStr = MediatorLiveData<String>()

    init {
        travelTimeLine = loadTravelTimeLine.map { it?: TravelTimeLine("") }
        modefyStr = loadModefyStr.map { it?:"" }
        disposable.add(
            Observable.interval(2, TimeUnit.SECONDS)
                .subscribe {
                    refreshTreaatment()
                })
    }

    fun getTreaatment(){
        disposable.add(api.getPatientTravelTimeLine(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun getData(response: Response<TravelTimeLine>){
        loadTravelTimeLine.value = response.result
        travelTimeLine.value?.timeStr()
        messageAction.value= Event("刷新时间点成功")
    }

    fun refreshTreaatment(){
        disposable.add(api.getPatientTravelTimeLine(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData1,::error))
    }

    private fun getData1(response: Response<TravelTimeLine>){
        val num = response.result?.triggerRecordLists?.filter { !it.inTime.isNullOrEmpty() }!!.size
        if (num > pointNum.get()){
            pointNum.set(num)
            loadModefyStr?.value = "RefreshRfidTimeline"
            loadTravelTimeLine.value = response.result
            travelTimeLine.value?.timeStr()
            messageAction.value= Event("刷新时间点成功")
        }else{
            loadModefyStr?.value = "stop"
        }

    }

}