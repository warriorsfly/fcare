package com.wxsoft.fcare.ui.details.dominating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * 考虑到本程序的服务端特殊性（非restful  api），不显示状态的查询不使用result.Resource类进行结果封装
 */
class DoMinaViewModel @Inject constructor(private val taskApi: TaskApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gson: Gson
) : BaseViewModel(sharedPreferenceStorage,gson) {

    val task:LiveData<Task>
    val selectedItemPosition:LiveData<Int>
    val selectIndex = MediatorLiveData<Int>()

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
        task=loadTaskResult.map {it.result?: Task("")}

        selectedItemPosition=selectIndex.map { it }
    }

    /**
     * 点击到达xxx事件
     */
    private val _atAction = MutableLiveData<Event<Int>>()
    val atAction: LiveData<Event<Int>>
        get() = _atAction

    /**
     * 拉取任务
     */
    private fun loadTask() {
        taskApi.task(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {  loadTaskResult.value = it
                    selectIndex.value=if(it.result?.status?:0==0) 0 else it.result?.status
                },
                { messageAction.value = Event(it.message ?: "") }
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
            2->{met()}
            3->{returning()}
            4->{arriveHos()}
        }
    }
    /**
     * 到达现场
     */
    private fun arrive(){

        if(task.value==null){
            messageAction.value= Event("任务不存在")
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
                                selectIndex.value=2
                                messageAction.value = Event(it.carId + "到达成功")
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") })

            }
        }
    }

    /**
     * 首次接触
     */
    private fun met(){

        if(task.value==null){
            messageAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.met(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.firstMet = resp.result
                                loadTaskResult.value?.result?.status = 3
                                selectIndex.value=3
                                messageAction.value = Event(it.carId + "首次接触")
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") })
            }
        }
    }

    /**
     * 开始返回医院
     */
    private fun returning(){

        if(task.value==null){
            messageAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.returning(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.returnAt = resp.result
                                loadTaskResult.value?.result?.status = 4
                                selectIndex.value=4
                                messageAction.value = Event(it.carId + "开始返回医院")
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") })
            }
        }
    }

    /**
     * 到达医院大门
     */
    private fun arriveHos(){

        if(task.value==null){
            messageAction.value= Event("任务不存在")
        }else {
            task.value?.let {
                taskApi.arriveHos(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveHosAt = resp.result
                                loadTaskResult.value?.result?.status = 5
                                selectIndex.value=5
                                messageAction.value = Event(it.carId + "返回医院大门")
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") })
            }
        }
    }
}

//TODO 修改各个时间点