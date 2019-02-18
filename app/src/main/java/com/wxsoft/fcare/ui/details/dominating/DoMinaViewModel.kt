package com.wxsoft.fcare.ui.details.dominating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableInt
import android.databinding.ObservableLong
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * 考虑到本程序的服务端特殊性（非restful  api），不显示状态的查询不使用result.Resource类进行结果封装
 */
class DoMinaViewModel @Inject constructor(private val taskApi: TaskApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    val task:LiveData<Task>
    private val selectIndex = ObservableInt()
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

    /**
     * 时间处理
     */

    private var startTimeStamp:Long?=null

    private var arriveTimeStamp:Long?=null
    set(value) {
        field=value
        field?.let {
            arriveTime.set((it-startTimeStamp!!)/60000)
        }
    }

    private var firstMetTimeStamp:Long?=null
        set(value) {
            field=value
            field?.let {
                firstMetTime.set((it-arriveTimeStamp!!)/60000)
            }
        }

    private var returningTimeStamp:Long?=null
        set(value) {
            field=value
            field?.let {
                returningTime.set((it-firstMetTimeStamp!!)/60000)
            }
        }
    private var arriveHosTimeStamp:Long?=null
        set(value) {
            field=value
            field?.let {
                arriveHosTime.set((it-returningTimeStamp!!)/60000)
            }
        }

    /**
     * 到达现场所需要的时间
     */
    val arriveTime=ObservableLong()
    /**
     * 首次医疗接触所需要的时间
     */
    private val firstMetTime=ObservableLong()
    /**
     * 返回医院所需要的时间
     */
    val returningTime=ObservableLong()
    /**
     * 到达医院大门所需要的时间
     */
    val arriveHosTime=ObservableLong()

    init {
        task=loadTaskResult.map {

            val theTask=it.result?: Task("")
            startTimeStamp = if(theTask.startAt==null) null else DateTimeUtils.formatter.parse(theTask.startAt)?.time
            arriveTimeStamp = if(theTask.arriveAt==null) null else DateTimeUtils.formatter.parse(theTask.arriveAt)?.time
            firstMetTimeStamp = if(theTask.firstMet==null) null else DateTimeUtils.formatter.parse(theTask.firstMet)?.time
            returningTimeStamp = if(theTask.returnAt==null) null else DateTimeUtils.formatter.parse(theTask.returnAt)?.time
            arriveHosTimeStamp = if(theTask.arriveHosAt==null) null else DateTimeUtils.formatter.parse(theTask.arriveHosAt)?.time


            _pageAction.value=Event(it.result!!.status-1)
            return@map theTask
        }
    }

    /**
     * 点击到达xxx事件
     */
    private val _atAction = MutableLiveData<Event<Int>>()
    val atAction: LiveData<Event<Int>>
        get() = _atAction

    private val _pageAction = MutableLiveData<Event<Int>>()
    val pageAction: LiveData<Event<Int>>
        get() = _pageAction
    /**
     * 拉取任务
     */
    fun loadTask() {
        disposable.add(taskApi.task(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(DisposableSingleObserver<Response<Task>>())
            .subscribe(
                {
                    loadTaskResult.value = it
                    selectIndex.set(it.result?.status?:0)
                },
                { messageAction.value = Event(it.message ?: "") }
            ))
    }

    /**
     * 供binding调用
     * 和task.status对应
     */
    fun startAction(status:Int) {
         if (status==1   || status <= (task.value?.status ?: 1)) {

            _pageAction.value = Event(status-1)
        } else {

            _atAction.value = Event(status)
        }
    }

    /**
     * 执行操作
     */
    fun doAction(status: Int){

        when (status) {
            2 -> {
                arrive()
            }
            3 -> {
                met()
            }
            4 -> {
                returning()
            }
            5 -> {
                arriveHos()
            }
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
               disposable.add(taskApi.arrive(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveAt = resp.result
                                loadTaskResult.value?.result?.status = 2
                                arriveTimeStamp =  DateTimeUtils.formatter.parse(resp.result)?.time

                                selectIndex.set(2)
                                _pageAction.value = Event(1)
                                messageAction.value = Event(it.carId + "到达成功")

                                arriveTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))

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
                disposable.add(taskApi.met(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.firstMet = resp.result
                                loadTaskResult.value?.result?.status = 3
                                selectIndex.set(3)
                                firstMetTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                                _pageAction.value = Event(2)
                                messageAction.value = Event(it.carId + "首次接触")

                                firstMetTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))
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
                disposable.add(taskApi.returning(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.returnAt = resp.result
                                loadTaskResult.value?.result?.status = 4
                                selectIndex.set(4)
                                returningTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                                _pageAction.value = Event(3)
                                messageAction.value = Event(it.carId + "开始返回医院")


                                returningTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))
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
                disposable.add( taskApi.arriveHos(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { resp ->
                            if (resp.success) {
                                loadTaskResult.value?.result?.arriveHosAt = resp.result
                                loadTaskResult.value?.result?.status = 5
                                selectIndex.set(5)
                                _pageAction.value = Event(4)
                                arriveHosTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                                messageAction.value = Event(it.carId + "返回医院大门")

                                arriveHosTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))
            }
        }
    }


}

//TODO clock the process