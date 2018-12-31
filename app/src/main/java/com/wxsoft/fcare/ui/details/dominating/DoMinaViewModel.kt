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

    /**
     * 点击到达xxx事件
     */
    private val _atAction = MutableLiveData<Event<Int>>()
    val atAction: LiveData<Event<Int>>
        get() = _atAction

    /**
     * 结果信息
     */
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
     * 供binding调用
     * 和task.status对应
     */
    fun startAction(status:Int){
        _atAction.value=Event(status)
    }

    /**
     * 执行操作
     */
    fun doAction(status: Int){
        when(status){
            1->{arrive()}
            2->{}
        }
    }
    /**
     * 到达现场
     */
    fun arrive(){

        if(task.value==null){
            _taskAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.arrive(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveAt = resp.result
                                loadTaskResult.value?.result?.status = 2
                                _taskAction.value = Event(it.carId + "到达成功")
                            } else {
                                _taskAction.value = Event(resp.msg)
                            }
                        },
                        {error->_taskAction.value= Event(error.message?:"") })

            }
        }
    }

    /**
     * 首次接触
     */
    fun met(){

        if(task.value==null){
            _taskAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.met(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveAt = resp.result
                                loadTaskResult.value?.result?.status = 3
                                _taskAction.value = Event(it.carId + "首次接触")
                            } else {
                                _taskAction.value = Event(resp.msg)
                            }
                        },
                        {error->_taskAction.value= Event(error.message?:"") })
            }
        }
    }

    /**
     * 开始返回医院
     */
    fun returning(){

        if(task.value==null){
            _taskAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.returning(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveAt = resp.result
                                loadTaskResult.value?.result?.status = 4
                                _taskAction.value = Event(it.carId + "开始返回医院")
                            } else {
                                _taskAction.value = Event(resp.msg)
                            }
                        },
                        {error->_taskAction.value= Event(error.message?:"") })
            }
        }
    }

    /**
     * 到达医院大门
     */
    fun arriveHos(){

        if(task.value==null){
            _taskAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.arriveHos(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveAt = resp.result
                                loadTaskResult.value?.result?.status = 5
                                _taskAction.value = Event(it.carId + "返回医院大门")
                            } else {
                                _taskAction.value = Event(resp.msg)
                            }
                        },
                        {error->_taskAction.value= Event(error.message?:"") })
            }
        }
    }
}