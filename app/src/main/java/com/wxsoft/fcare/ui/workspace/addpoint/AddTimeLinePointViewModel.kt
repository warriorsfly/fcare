package com.wxsoft.fcare.ui.workspace.addpoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TimePoint
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddTimeLinePointViewModel @Inject constructor(private val qualityControlApi: QualityControlApi,
                                                    override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    var patientId:String=""
        set(value) {
            if (value == "") return
            field=value
            loadTimePoints()
        }

    val timepoints: LiveData<List<TimePoint>>
    private val loadTimepointsResult= MediatorLiveData<Response<List<TimePoint>>>()

    init {
        timepoints = loadTimepointsResult.map { it.result?: emptyList() }
    }

    private fun loadTimePoints(){
        disposable.add(qualityControlApi.getAddTimePoints(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doTimePoints,::error))
    }

    private fun doTimePoints(response: Response<List<TimePoint>>){
        loadTimepointsResult.value=response
        timepoints.value?.map { it.editable = true }
    }


    fun clickSelect(item: TimePoint){
        timepoints.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
    }

}