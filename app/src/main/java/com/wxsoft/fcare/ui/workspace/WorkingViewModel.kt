package com.wxsoft.fcare.ui.workspace

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class WorkingViewModel @Inject constructor(private val patientApi: PatientApi,
                                           private val pacsApi: PACSApi,
                                           private val interventionApi: InterventionApi,
                                           private val qualityControlApi: QualityControlApi,
                                           @Named("WorkOperations")
                                           private val icons:Array<Pair<String,Int>>,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    val showAttackTime=ObservableBoolean().apply { set(false) }
    val valided=ObservableBoolean().apply { set(true) }
    val timestamp=ObservableLong().apply { set(0) }
    val minute=ObservableLong().apply { set(0) }
    val second=ObservableLong().apply { set(0) }
    var patientId:String=""
        set(value) {
            field=value
            loadPatient(field)
            loadQualities(field)
            loadOperations(field,account.id)
        }

    var pre:Boolean=false
    val courseSeconds=ObservableInt()
    val course=ObservableField<String>().apply {
        set("0")
    }

    val emrFullScreen=ObservableBoolean()
    /**
     * 病人信息
     */
    val patient:LiveData<Patient>
    private val loadPatientResult=MediatorLiveData<Response<Patient>>()

    val checkPicResult:LiveData<String>
    private val loadCheckPicResult=MediatorLiveData<String>()

    init {
        patient=loadPatientResult.map { it.result?.apply {
            if(diagnosis2Name.isNullOrEmpty()) {
                diagnosis2Name = "诊断中"
            }
//            if (!outpatientId.isNullOrEmpty()) lsh = outpatientId
        }?: Patient() }
        checkPicResult = loadCheckPicResult.map { it?:"" }
    }

    /**
     * 质控信息
     */
    val qualities:LiveData<List<TimeQuality>>
    private val loadTimeQualityResult=MediatorLiveData<Response<List<TimeQuality>>>()

    init {
        qualities=loadTimeQualityResult.map {it.result?: emptyList()}
    }

    /**
     * 操作列表
     */
    val operations:LiveData<List<Record<WorkOperation>>>
    val otherOperations:LiveData<List<WorkOperation>>
    private val loadOperationResult=MediatorLiveData<Response<List<Record<WorkOperation>>>>()
    private val loadOtherOperationResult=MediatorLiveData<List<WorkOperation>>()

    init {
        operations=loadOperationResult.map { it.result?: emptyList() }
        otherOperations=loadOtherOperationResult.map { it?: emptyList() }
    }


    private fun loadPatient(id:String){
        disposable.add(patientApi.getOne(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doPatient,::error))
    }

    private fun loadQualities(id:String){
        disposable.add(qualityControlApi.getQualities(id).zipWith(qualityControlApi.validQualities(id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doQualities,::error))
    }

    private fun loadOperations(id:String,userId:String){
        disposable.add(qualityControlApi.getOperationGroups(id,userId,pre)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doOperations,::error))
    }
    fun checkPicNums(){
        disposable.add(qualityControlApi.validPatientData(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::checkResult,::error))
    }
    private fun checkResult(response:Response<String>){
        if (response.success){
            loadCheckPicResult.value = ""
        }else{
            loadCheckPicResult.value = response.result?:""
        }
    }

    private fun doPatient(response:Response<Patient>){
        loadPatientResult.value=response.apply {  result?.let {
             it.timing=true
        } }

        disposable.add(patientApi.getServerDateTime()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::judgeMinete,::error))
    }

    private fun judgeMinete(response: Response<String>){
        val dValue = System.currentTimeMillis() - DateTimeUtils.formatter.parse(response.result).time

        patient.value?.attackTime?.let {
            timestamp.set(DateTimeUtils.formatter.parse(it).time)
            minute.set((System.currentTimeMillis()- timestamp.get() - dValue)/60000)
            second.set(((System.currentTimeMillis()- timestamp.get() - dValue)%60000)/1000)
            courseSeconds.set((System.currentTimeMillis()/60000 - DateTimeUtils.formatter.parse(it).time / 60000 - dValue/60000).toInt())
            if (minute.get() > 300){
                showAttackTime.set(true)
                return
            }
            course.set(courseSeconds.get().toString())
            disposable.add(
                Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe {
                        minute.set((System.currentTimeMillis()- timestamp.get() - dValue)/60000)
                        second.set(((System.currentTimeMillis()- timestamp.get() - dValue)%60000)/1000)
                        courseSeconds.set(courseSeconds.get().plus(1))
                    })
        }?:course.set("")
    }

    private fun doQualities(it: Pair<Response<List<TimeQuality>>,Response<Boolean>>){
        loadTimeQualityResult.value=it.first
        valided.set(it.second.result?:true)
    }
    private fun doOperations(response:Response<List<Record<WorkOperation>>>){
        response.result?.forEach { operation->
            operation.items?.forEach {

                icons.firstOrNull { ic->ic.first == it.actionCode }?.second?.let {
                    sec->it.ico =sec
                }

            }
        }
        val main=response.copy().apply {
            result=response.result?.filter { it.typeId!="232-99" }
        }
        val other=response.result?.firstOrNull { it.typeId=="232-99" }?.items
        loadOperationResult.value=main
        loadOtherOperationResult.value=other
    }

    fun commitNoticePacs(){ //通知启动CT室
        disposable.add(
            pacsApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }

    fun commitNoticeInv(){//通知启动导管室
        disposable.add(
            interventionApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }

    fun nfcInput(code:String?) {
        code?.let {
            disposable.add(
                patientApi.nfcInput(account.id, patientId, it)
                    .flatMap { qualityControlApi.getOperationGroups(patientId, account.id, pre) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        messageAction.value = Event(it.msg)
                    }, ::error)
            )
        }
    }

}
