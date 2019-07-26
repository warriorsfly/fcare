package com.wxsoft.fcare.ui.details.comingby

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ComingByApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.TimingType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComingByViewModel @Inject constructor(
                                            private val comingByApi: ComingByApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){

    val comingBy:LiveData<ComingBy>
    val saved:LiveData<Boolean>
    val passing:LiveData<Passing>
    var xtShow= ObservableField<Boolean>()
    val timeLiveData=MediatorLiveData<Pair<String,String>>()
    var patientId:String=""
    set(value) {
        field=value
        loadData(field)
    }

    private val loadComingBy=MediatorLiveData<Response<ComingBy>>()
    private val loadPassing=MediatorLiveData<Response<Passing>>()
    private val savingResult=MediatorLiveData<Response<String?>>()

//    outhospital_Visit_Time,transfer_Time,
//    ambulance_Arrived_Time,leave_Outhospital_Time,arrived_Scene_Time,arrived_Hospital_Time,
//    inhospital_Admission_Time,consultation_Time,leave_Department_Time
    private val array= arrayOf("转出医院入门时间","决定转院时间","转运救护车到达时间","离开转出医院时间","出诊医生到达现场","到达本院大门",
    "院内接诊时间","会诊时间","离开科室时间")
    val selectType=MediatorLiveData<Int>()
    val selectDoctor=MediatorLiveData<Int>()

    var cdoctors:List<User> = emptyList()

    init {
        saved=savingResult.map { it.success }

        comingBy=loadComingBy.map { it.result?: ComingBy().apply {
            emergencyDoctor=User().apply {
//                if(account.)
                id=account.id
                trueName=account.trueName
            }
        }}

        passing=loadPassing.map { it.result?: Passing(patientId=patientId,createrId = account.id)}
    }



    private fun loadData(id:String){
        fun doComing(response: Pair<Response<ComingBy>,Response<Passing>>){
            loadComingBy.value=response.first.apply {
                result?.let {
                    cb->
                    cb.emergencyDoctor=cb.comingWayStaffs.firstOrNull { it.staffType=="1" }?.let {
                        User().apply {
                            this.id=it.staffId
                            trueName = it.staffName
                        }
                    }?:User()

                    cb.emergencyNurse=cb.comingWayStaffs.firstOrNull { it.staffType=="2" }?.let {
                        User().apply {
                            this.id=it.staffId
                            trueName = it.staffName
                        }
                    }?:User()
                    cdoctors=cb.comingWayStaffs.filter { it.staffType=="3" }?.map {
                        User().apply {
                            this.id=it.staffId
                            trueName=it.staffName
                        }
                    }
                    cb.consultantDoctors=cdoctors.
                        joinToString(separator = ","){ it.trueName }
                }
            }
            loadPassing.value=response.second

        }

        disposable.add(comingByApi.getOne(id,account.id).zipWith(comingByApi.getPassing(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doComing,::error))
    }

    fun changeTiming(timingType: String){
        if (timingType==TimingType.PassingArriveCCU
                ||timingType==TimingType.PassingArriveEmergency
                ||timingType==TimingType.PassingLeaveEmergency){
            passing.value?.let {
                val time = when (timingType) {
                    TimingType.PassingLeaveEmergency -> it.passingEmergencyTime
                    TimingType.PassingArriveEmergency -> it.arriveEmergencyTime
                    TimingType.PassingArriveCCU -> it.arriveCCUTime
                    else -> throw IllegalArgumentException("error timing type $timingType")
                }?:""
                timeLiveData.value = Pair(timingType, time)
            }
        }else {
            comingBy.value?.let {
                val time = when (timingType) {
                    TimingType.ArriveCcu -> it.arrived_Ccu_Date
                    TimingType.OutHospitalVisit -> it.outhospital_Visit_Time
                    TimingType.Transfer -> it.transfer_Time
                    TimingType.AmbulanceArrived -> it.ambulance_Arrived_Time
                    TimingType.LeaveOutHospital -> it.leave_Outhospital_Time
                    TimingType.ArriveHospital -> it.arrived_Hospital_Time
                    TimingType.InHospitalAdmission -> it.inhospital_Admission_Time
                    TimingType.LeaveDepartment -> it.leave_Department_Time
                    TimingType.ArriveScene -> it.arrived_Scene_Time
                    TimingType.Consultation -> it.consultation_Time?:""
                    TimingType.LeaveDepartment -> it.leave_Department_Time
                    else -> throw IllegalArgumentException("error timing type $timingType")
                }
                timeLiveData.value = Pair(timingType, time)
            }
        }

    }

    fun changedTiming(pair: Pair<String,String>){

        if (pair.first==TimingType.PassingArriveCCU
                ||pair.first==TimingType.PassingArriveEmergency
                ||pair.first==TimingType.PassingLeaveEmergency){
            passing.value?.let {
                when (pair.first) {
                    TimingType.PassingLeaveEmergency -> it.passingEmergencyTime=pair.second
                    TimingType.PassingArriveEmergency -> it.arriveEmergencyTime=pair.second
                    TimingType.PassingArriveCCU -> it.arriveCCUTime=pair.second
                    else -> throw IllegalArgumentException("error timing type ${pair.second}")
                }
            }
        }else {
            comingBy.value?.let {
                when (pair.first) {
                    TimingType.ArriveCcu -> it.arrived_Ccu_Date = pair.second
                    TimingType.OutHospitalVisit -> it.outhospital_Visit_Time = pair.second
                    TimingType.Transfer -> it.transfer_Time = pair.second
                    TimingType.AmbulanceArrived -> it.ambulance_Arrived_Time = pair.second
                    TimingType.LeaveOutHospital -> it.leave_Outhospital_Time = pair.second
                    TimingType.ArriveHospital -> it.arrived_Hospital_Time = pair.second
                    TimingType.InHospitalAdmission -> it.inhospital_Admission_Time = pair.second
                    TimingType.LeaveDepartment -> it.leave_Department_Time = pair.second
                    TimingType.ArriveScene -> it.arrived_Scene_Time = pair.second
                    TimingType.Consultation -> it.consultation_Time = pair.second
                    else -> throw IllegalArgumentException("error timing type ${pair.first}")
                }
            }
        }
    }

    fun changeType(flag:Int){
        selectType.value=flag
    }

    fun changeDoctor(flag:Int){
        selectDoctor.value=flag
    }

    fun doSaving(result:Response<String?>){
        savingResult.value=result
    }

    fun clearConsultationTime(){
        comingBy.value?.consultation_Time=null
    }


    fun save() {
        if (comingBy.value == null || passing.value == null) return


        comingBy.value?.apply {
            val times = arrayOf( outhospital_Visit_Time,transfer_Time,
                ambulance_Arrived_Time,leave_Outhospital_Time,arrived_Scene_Time,arrived_Hospital_Time,
                inhospital_Admission_Time,consultation_Time,leave_Department_Time)

                .mapIndexedNotNull { index, s -> if(s.isNullOrEmpty()) null else Pair(index,s) }

            if(times.size>1) {
                for (index in 0 until times.size-1) {
                    if (times[index].second > times[index + 1].second){
                        messageAction.value = Event("${array[times[index].first]}需早于${array[times[index+1].first]}")
                        return
                    }
                }
            }

            when{
                consultation_Time != null && !comingWayStaffs?.any { it.staffType == "3" }
                ->{
                    messageAction.value = Event("会诊医生不能为空")
                    return
                }
                comingWayStaffs?.any { it.staffType == "3" } && consultation_Time == null
                ->{
                    messageAction.value = Event("会诊时间不能为空")
                    return
                }
            }

            var d1 = comingWayStaffs.firstOrNull { it.staffType == "1" }

            if (d1 == null) {
                if (emergencyDoctor.id.isNotEmpty())
                    d1 = ComingByStaff(comingWayId = id, staffType = "1")
            }
            if (d1?.staffId != emergencyDoctor.id) {
                d1?.staffId = emergencyDoctor.id
                d1?.staffName = emergencyDoctor.trueName
            }

            var d2 = comingWayStaffs.firstOrNull { it.staffType == "2" }

            if (d2 == null) {
                if (emergencyDoctor.id.isNotEmpty())
                    d2 = ComingByStaff(comingWayId = id, staffType = "2")
            }
            if (d2?.staffId != emergencyNurse.id) {
                d2?.staffId = emergencyNurse.id
                d2?.staffName = emergencyNurse.trueName
            }

            val d3 = this@ComingByViewModel.cdoctors.map {
                ComingByStaff(comingWayId = id, staffType = "3", staffId = it.id, staffName = it.trueName)
            }
            val l = mutableListOf<ComingByStaff>()
            d1?.let(l::add)
            d2?.let(l::add)
            l.addAll(d3)
            comingWayStaffs = l
        }




        comingByApi.save(comingBy.value!!).flatMap {
            comingByApi.savePassing(passing.value!!)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doSaving, ::error)
    }
}