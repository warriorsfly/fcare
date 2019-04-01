package com.wxsoft.fcare.ui.workspace

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.core.data.entity.WorkOperation
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
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class WorkingViewModel @Inject constructor(private val patientApi: PatientApi,
                                           private val pacsApi: PACSApi,
                                           private val interventionApi: InterventionApi,
                                           private val qualityControlApi: QualityControlApi,
                                           @Named("WorkOperationIcon")
                                           private val icons:Array<Int>,
                                           @Named("WorkOperationTint")
                                           private val tints:IntArray,
                                           @Named("WorkOperationKey")
                                           private val keys:Array<String>,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

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

    init {
        patient=loadPatientResult.map { it.result?: Patient() }
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
    val operations:LiveData<List<WorkOperation>>
    private val loadOperationResult=MediatorLiveData<Response<List<WorkOperation>>>()

    init {
        operations=loadOperationResult.map { it.result?: emptyList() }
    }


    private fun loadPatient(id:String){
        disposable.add(patientApi.getOne(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doPatient,::error))
    }

    private fun loadQualities(id:String){
        disposable.add(qualityControlApi.getQualities(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doQualities,::error))
    }

    private fun loadOperations(id:String,userId:String){
        disposable.add(qualityControlApi.getOperations(id,userId,pre)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doOperations,::error))
    }

    private fun doPatient(response:Response<Patient>){
        loadPatientResult.value=response.apply {  result?.let {
            it.timing=it.currentScene<"223-4"
        } }

        response.result?.attackTime?.let {
            courseSeconds.set((System.currentTimeMillis()/60000 - DateTimeUtils.formatter.parse(it).time / 60000).toInt())

            course.set(courseSeconds.get().toString())
            disposable.add(
                Observable.interval(1, TimeUnit.MINUTES)
                    .subscribe {
                        courseSeconds.set(courseSeconds.get().plus(1))
                    })
        }?:course.set("")
    }
    private fun doQualities(it: Response<List<TimeQuality>>){
        loadTimeQualityResult.value=it
    }
    private fun doOperations(response:Response<List<WorkOperation>>){
        response.result?.forEach { operation->
            operation.apply {
                val index=keys.indexOf(actionCode)
                if(index>=0){
                    tint=tints[index]
                    ico=icons[index]
                }
            }
        }
        loadOperationResult.value=response
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

}
