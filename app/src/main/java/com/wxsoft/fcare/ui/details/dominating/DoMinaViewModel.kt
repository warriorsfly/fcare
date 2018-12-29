package com.wxsoft.fcare.ui.details.dominating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * 考虑到本程序的服务端特殊性（非restful  api），不显示状态的查询不使用result.Resource类进行结果封装
 */
class DoMinaViewModel @Inject constructor(private val taskApi: TaskApi) : ViewModel() {

    val task:LiveData<Task>

    var taskId:String=""
        set(value) {
            if(value!=field){
                field=value
                if(field.isNotEmpty()){
                    loadTask()
                }
            }
        }
    private val loadTaskResult=MediatorLiveData<Response<Task>>()
    init {
        task=loadTaskResult.map { it.result?: Task("") }
    }

    private val _taskAction = MutableLiveData<Event<String>>()
    val taskAction: LiveData<Event<String>>
        get() = _taskAction

    /**
     * 拉取任务
     */
    private fun loadTask() {
        taskApi.task(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {  loadTaskResult.value = it },
                { _taskAction.value = Event(it.message ?: "") }
            )
    }
    /**
     * 到达现场
     */
    fun arrive(){
        task.value?.let {
            taskApi.arrive(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->
                    if (resp.success) {
                        loadTaskResult.value?.result?.arriveAt = resp.result
                        _taskAction.value = Event(it.carId + "到达成功")
                    } else {
                        _taskAction.value = Event(resp.msg)
                    }
                }.doOnError {  _taskAction.value= Event(it.message?:"") }

        }.also { _taskAction.value= Event("任务不存在") }
    }

    /**
     * 首次接触
     */
    fun met(){
        task.value?.let {
            taskApi.met(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->

                    if (resp.success) {
                        loadTaskResult.value?.result?.arriveAt = resp.result
                        _taskAction.value = Event(it.carId + "首次接触")
                    } else {
                        _taskAction.value = Event(resp.msg)
                    }

                }.doOnError {  _taskAction.value= Event(it.message?:"") }
        }.also { _taskAction.value= Event("任务不存在") }
    }

    /**
     * 开始返回医院
     */
    fun returning(){
        task.value?.let {
            taskApi.returning(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->

                    if (resp.success) {
                        loadTaskResult.value?.result?.arriveAt = resp.result
                        _taskAction.value = Event(it.carId + "开始返回医院")
                    } else {
                        _taskAction.value = Event(resp.msg)
                    }

                }.doOnError {  _taskAction.value= Event(it.message?:"") }
        }.also { _taskAction.value= Event("任务不存在") }
    }

    /**
     * 到达医院大门
     */
    fun arriveHos(){
        task.value?.let {
            taskApi.arriveHos(it.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { resp ->

                    if (resp.success) {
                        loadTaskResult.value?.result?.arriveAt = resp.result
                        _taskAction.value = Event(it.carId + "到达医院大门")
                    } else {
                        _taskAction.value = Event(resp.msg)
                    }

                }.doOnError {  _taskAction.value= Event(it.message?:"") }
        }.also { _taskAction.value= Event("任务不存在") }
    }
}