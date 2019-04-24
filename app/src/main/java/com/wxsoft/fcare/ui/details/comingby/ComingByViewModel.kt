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
import com.wxsoft.fcare.utils.TimingType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ComingByViewModel @Inject constructor(private val dictApi:DictEnumApi,
                                            private val comingByApi: ComingByApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){


    val comingType:LiveData<List<Dictionary>>
    val comingFrom:LiveData<List<Dictionary>>
    val comingBy:LiveData<ComingBy>
    val timeLiveData=MediatorLiveData<Pair<String,String>>()
    var patientId:String=""
    set(value) {
        field=value
        loadData(field)
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
        comingBy=loadComingBy.map { it.result?: ComingBy()}
    }

    private fun doTypes(response: Pair<Pair<List<Dictionary>,List<Dictionary>>,Response<ComingBy>>){
        loadTypes.value=response.first.first
        loadFroms.value=response.first.second
        loadComingBy.value=response.second.apply {
            if(result==null){
                val coming=ComingBy()
                if(response.first.first.isNotEmpty()){
                    coming.comingWayCode=response.first.first.first().id
                }
                result=coming
            }
        }
    }

    private fun loadData(id:String){
        dictApi.loadDicts("3").zipWith(dictApi.loadDicts("18"))
            .zipWith(comingByApi.getOne(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doTypes,::error)
    }

    fun changeTiming(timingType: String){
        comingBy.value?.let {
            val time=when(timingType){
                TimingType.ArriveCcu->it.arrived_Ccu_Date
                TimingType.OutHospitalVisit->it.outhospital_Visit_Time
                TimingType.Transfer->it.transfer_Time
                TimingType.AmbulanceArrived->it.ambulance_Arrived_Time
                TimingType.LeaveOutHospital->it.leave_Outhospital_Time
                TimingType.ArriveHospital->it.arrived_Hospital_Time
                TimingType.InHospitalAdmission->it.inhospital_Admission_Time
                TimingType.LeaveDepartment->it.leave_Department_Time
                TimingType.ArriveScene->it.arrived_Scene_Time
                TimingType.Consultation->it.consultation_Time
                TimingType.LeaveDepartment->it.leave_Department_Time
                else->throw IllegalArgumentException("error timing type $timingType")
            }
            timeLiveData.value=Pair(timingType,time)
        }

    }

    fun changedTiming(pair: Pair<String,String>){
        comingBy.value?.let {
            val time = when (pair.first) {
                TimingType.ArriveCcu -> it.arrived_Ccu_Date=pair.second
                TimingType.OutHospitalVisit -> it.outhospital_Visit_Time=pair.second
                TimingType.Transfer -> it.transfer_Time=pair.second
                TimingType.AmbulanceArrived -> it.ambulance_Arrived_Time=pair.second
                TimingType.LeaveOutHospital -> it.leave_Outhospital_Time=pair.second
                TimingType.ArriveHospital -> it.arrived_Hospital_Time=pair.second
                TimingType.InHospitalAdmission -> it.inhospital_Admission_Time=pair.second
                TimingType.LeaveDepartment -> it.leave_Department_Time=pair.second
                TimingType.ArriveScene->it.arrived_Scene_Time=pair.second
                TimingType.Consultation->it.consultation_Time=pair.second
                else -> throw IllegalArgumentException("error timing type ${pair.first}")
            }
        }
    }


}