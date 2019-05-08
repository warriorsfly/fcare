package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ComingByApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.TimingType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ComingByViewModel @Inject constructor(private val dictApi:DictEnumApi,
                                            private val doctorApi:TaskApi,
                                            private val comingByApi: ComingByApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){


    val comingType:LiveData<List<Dictionary>>
    val comingFrom:LiveData<List<Dictionary>>
    val passingKs:LiveData<List<Dictionary>>
    val emergencyDoctors:LiveData<List<User>>
    val emergencyNurses:LiveData<List<User>>
    val consultantDoctors:LiveData<List<User>>
    val comingBy:LiveData<ComingBy>
    val saved:LiveData<Boolean>
    val passing:LiveData<Passing>
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
    private val loadPassingType=MediatorLiveData<List<Dictionary>>()
    private val loadPassing=MediatorLiveData<Response<Passing>>()
    private val savingResult=MediatorLiveData<Response<String?>>()

    private val loadEmergencyDoctors=MediatorLiveData<Response<List<User>>>()
    private val loadEmergencyNurses=MediatorLiveData<Response<List<User>>>()
    private val loadConsultantDoctors=MediatorLiveData<Response<List<User>>>()
    
    val selectType=MediatorLiveData<Int>()
    val selectDoctor=MediatorLiveData<Int>()

    init {
        saved=savingResult.map { it.success }
        passingKs=loadPassingType.map {  it?: emptyList() }
        comingType=loadTypes.map { it?: emptyList() }
        comingFrom=loadFroms.map { it?: emptyList() }
        comingBy=loadComingBy.map { it.result?: ComingBy()}

        emergencyDoctors=loadEmergencyDoctors.map { it.result?: emptyList()}
        emergencyNurses=loadEmergencyNurses.map { it.result?: emptyList()}
        consultantDoctors=loadConsultantDoctors.map { it.result?: emptyList()}
        passing=loadPassing.map { it.result?: Passing(patientId=patientId,createrId = account.id)}
    }



    private fun loadData(id:String){

        fun doDoctor1(response: Response<List<User>>){
         loadEmergencyDoctors.value=response.apply {
             result?.firstOrNull {
                 user->
                 comingBy.value?.
                     comingWayStaffs?.
                     any { user.id==it.staffId && it.staffType=="1" }?:false
             }?.checked=true
         }
        }

        fun doNurse(response: Response<List<User>>){
            loadEmergencyNurses.value=response.apply {
                result?.firstOrNull {
                        user->
                    comingBy.value?.
                        comingWayStaffs?.
                        any { user.id==it.staffId && it.staffType=="2" }?:false
                }?.checked=true
            }
        }

        fun doDoctor2(response: Response<List<User>>){
            loadConsultantDoctors.value=response.apply {
                result?.forEach {
                        user->
                    if(comingBy.value?.comingWayStaffs?.any { user.id==it.staffId && it.staffType=="3" } == true){
                        user.checked=true
                    }

                }
            }
        }

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

                    cb.consultantDoctors=cb.comingWayStaffs.filter { it.staffType=="3" }?.
                        joinToString(separator = ","){ it.staffName }
                }
            }
            loadPassing.value=response.second


            disposable.add(doctorApi.getEmergencyDoctor(account.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doDoctor1,::error))

            disposable.add(doctorApi.getEmergencyNurse(account.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doNurse,::error))

            disposable.add(doctorApi.getConsultantDoctor(account.id,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doDoctor2,::error))
        }

        fun doTypes(response: Pair<List<Dictionary>,List<Dictionary>>){
            loadTypes.value=response.first
            loadFroms.value=response.second
        }

        disposable.add(dictApi.loadDicts("3").zipWith(dictApi.loadDicts("18"))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doTypes,::error))

        disposable.add(dictApi.loadDicts("5")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({loadPassingType.value=it},::error))

        disposable.add(comingByApi.getOne(id).zipWith(comingByApi.getPassing(id))
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
                    TimingType.Consultation -> it.consultation_Time
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


    fun save(){
        if(comingBy.value==null || passing.value==null)return
        comingBy.value?.apply {
            var d1=comingWayStaffs.firstOrNull { it.staffType=="1" }

            if(d1==null){
                if(emergencyDoctor.id.isNotEmpty())
                    d1= ComingByStaff(comingWayId = id,staffType = "1")
            }
            if(d1?.staffId!=emergencyDoctor.id){
                d1?.staffId=emergencyDoctor.id
                d1?.staffName=emergencyDoctor.trueName
            }

            var d2=comingWayStaffs.firstOrNull { it.staffType=="2" }

            if(d2==null){
                if(emergencyDoctor.id.isNotEmpty())
                    d2= ComingByStaff(comingWayId = id,staffType = "2")
            }
            if(d2?.staffId!=emergencyNurse.id){
                d2?.staffId=emergencyNurse.id
                d2?.staffName=emergencyNurse.trueName
            }

            val d3=this@ComingByViewModel.consultantDoctors.value?.filter { it.checked }?.map {
                ComingByStaff(comingWayId = id,staffType = "3",staffId = it.id,staffName = it.trueName)
            }?: emptyList()
            val l= mutableListOf<ComingByStaff>()
            d1?.let(l::add)
            d2?.let(l::add)
            l.addAll(d3)
            comingWayStaffs= l
        }
        comingByApi.save(comingBy.value!!).flatMap {
            comingByApi.savePassing(passing.value!!)
        } .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doSaving,::error)
    }
}