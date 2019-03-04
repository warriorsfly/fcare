package com.wxsoft.fcare.ui.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.NewTimeLine
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class TimeLineViewModel @Inject constructor(
    private val api: QualityControlApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    var patientId=""
        set(value) {
            field=value
            if(!field.isNullOrEmpty())
            loadTimeLine(field)
        }
    val timelines:LiveData<List<NewTimeLine>>

    private val loadTimeLineResult=MediatorLiveData<Response<List<NewTimeLine>>>()


    init {
        timelines=loadTimeLineResult.map {
            it.result?: emptyList()
        }
    }

    private fun loadTimeLine(pId:String){
        disposable.add(api.getTimeLines(pId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::showList,::showError))
    }

    private fun showList(timeLines:Response<List<NewTimeLine>>) {
        if(!timeLines.result.isNullOrEmpty()) {
            val date = timeLines.result?.firstOrNull{ newTimeLine ->  !newTimeLine.excutedAt.isNullOrEmpty()}?.excutedAt?.substring(0, 10)
            val starter = NewTimeLine(starter = true, excutedAt = date, eventName = "")
            val newList = mutableListOf<NewTimeLine>()
            newList.add(starter)
            timeLines.result?.forEach{it.excutedAt=it.excutedAt?.substring(11,16)}
            newList.addAll(timeLines.result!!)
            timeLines.result=newList
            loadTimeLineResult.value = timeLines
        }
    }

    private fun showError(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"")
    }
}