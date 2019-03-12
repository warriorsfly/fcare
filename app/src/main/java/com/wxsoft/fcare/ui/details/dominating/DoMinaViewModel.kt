package com.wxsoft.fcare.ui.details.dominating

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.entity.TaskSpend
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.min

/**
 * 考虑到本程序的服务端特殊性（非restful api），不显示状态的查询不使用result.Resource类进行结果封装
 */
class DoMinaViewModel @Inject constructor(private val taskApi: TaskApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    val minute= ObservableField<String>()
    val second=ObservableField<String>()
    val bText=ObservableField<String>()
    private var fireAt=0L
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

    val spends:LiveData<List<TaskSpend>>
    private val loadTaskResult=MediatorLiveData<Response<Task>>()
    private val loadSpendResult=MediatorLiveData<List<TaskSpend>>()

    /**
     * 时间处理
     */

    private var startTimeStamp:Long?=null

    private var arriveTimeStamp:Long?=null
    set(value) {
        field=value
        field?.let {
            task.value?.spends?.let { map ->
                val item = map.firstOrNull { item -> item.task == "到达现场" }
                if (item == null) {
                    map += TaskSpend("到达现场").apply {
                        spends=(it - startTimeStamp!!) / 1000
                    }
                } else {
                    item.spends = (it - startTimeStamp!!) / 1000
                }
            }
        }
    }

    private var firstMetTimeStamp:Long?=null
        set(value) {
            field=value

            field?.let {
                task.value?.spends?.let { map ->
                    val item = map.firstOrNull { item -> item.task == "首次医疗接触" }
                    if (item == null) {
                        map += TaskSpend("首次医疗接触").apply {
                            spends=(it - startTimeStamp!!) / 1000
                        }
                    } else {
                        item.spends = (it - arriveTimeStamp!!) / 1000
                    }
                }
            }
        }

    private var returningTimeStamp:Long?=null
        set(value) {
            field=value

            field?.let {
                task.value?.spends?.let { map ->
                    val item = map.firstOrNull { item -> item.task == "返回医院" }
                    if (item == null) {
                        map += TaskSpend("返回医院").apply {
                            spends=(it - startTimeStamp!!) / 1000
                        }
                    } else {
                        item.spends = (it - firstMetTimeStamp!!) / 1000
                    }
                }
            }
        }
    private var arriveHosTimeStamp:Long?=null
        set(value) {
            field=value

            field?.let {
                task.value?.spends?.let { map ->
                    val item = map.firstOrNull { item -> item.task == "到达医院大门" }
                    if (item == null) {
                        map += TaskSpend("到达医院大门").apply {
                            spends=(it - startTimeStamp!!) / 1000
                        }
                    } else {
                        item.spends = (it - returningTimeStamp!!) / 1000
                    }
                }
            }

        }

    init {

        spends=loadSpendResult.map {
            it
        }

        task=loadTaskResult.map {

            it.result?.apply {
                process = if (status == 5) 8 else 2 * status - 1
                spends= mutableListOf()

            }?: Task("").apply {
                loadSpendResult.value=spends
            }
        }
    }

    private fun refresh(){
        task.value?.startAt?.let { at ->
            DateTimeUtils.formatter.parse(at)?.time?.let { time ->
                startTimeStamp = time

                fireAt=(System.currentTimeMillis()-time)/1000
                disposable.add(
                    Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe {
                            fireAt++
                            minute.set((fireAt/60).toString())
                            second.set((fireAt%60).toString())
                        }
                )
            }
        }

        task.value?.arriveAt?.let { at ->
            DateTimeUtils.formatter.parse(at)?.time?.let { time ->
                arriveTimeStamp = time
            }
        }

        task.value?.firstMet?.let { at ->
            DateTimeUtils.formatter.parse(at)?.time?.let { time ->
                firstMetTimeStamp = time
            }
        }

        task.value?.returnAt?.let { at ->
            DateTimeUtils.formatter.parse(at)?.time?.let { time ->
                returningTimeStamp = time
            }
        }

        task.value?.arriveHosAt?.let { at ->
            DateTimeUtils.formatter.parse(at)?.time?.let { time ->
                arriveHosTimeStamp = time
            }
        }
        getText()
        loadSpendResult.value=task.value?.spends
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
    fun loadTask() {
        disposable.add(taskApi.task(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loadTaskResult.value = it
                    refresh()
                    selectIndex.set(it.result?.status?:0)
                },
                { messageAction.value = Event(it.message ?: "") }
            ))
    }

    /**
     * 供binding调用
     * 和task.status对应
     */
    fun startAction(stat:Int) {

        val status=(task.value?.status?:0)+1
        if (status != 1 && status > (task.value?.status ?: 1)) {

            if(task.value?.status==status-1)
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
                                messageAction.value = Event(it.carId + "到达成功")

                                loadSpendResult.value=it.spends
                                getText()
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

        if (task.value != null) {
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
                                messageAction.value = Event(it.carId + "首次接触")

                                loadSpendResult.value=it.spends
                                getText()
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))
            }
        } else {
            messageAction.value= Event("任务不存在")
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
                                messageAction.value = Event(it.carId + "开始返回医院")
                                loadSpendResult.value=it.spends
                                getText()
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
                                getText()
                                arriveHosTimeStamp = DateTimeUtils.formatter.parse(resp.result)?.time
                                messageAction.value = Event(it.carId + "返回医院大门")
                                loadSpendResult.value=it.spends
                            } else {
                                messageAction.value = Event(resp.msg)
                            }
                        },
                        {error->messageAction.value= Event(error.message?:"") }))
            }
        }
    }

    private fun getText(){
        task.value?.status?.let {
            bText.set(when(it){
                1->"到达现场"
                2->"首次接触"
                3->"返回医院"
                4->"到达医院大门"
                else->throw IllegalArgumentException("error status $it")
            })
        }
    }
}

//TODO clock the process