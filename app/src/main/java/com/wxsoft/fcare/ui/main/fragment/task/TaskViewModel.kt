package com.wxsoft.fcare.ui.main.fragment.task


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject


class TaskViewModel @Inject constructor(private val taskApi: TaskApi,
                                        override val sharedPreferenceStorage: SharedPreferenceStorage,
                                        override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon), EventActions {

    val isLoading: LiveData<Boolean>
    val tasks: LiveData<List<Task>>
    var taskDate: String = DateTimeUtils.getCurrentDate()
    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val _errorToOperationAction = MutableLiveData<Event<String>>()
    private val loadTasksResult = MediatorLiveData<Resource<Response<List<Task>>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction

    init {
        load()
        isLoading = loadTasksResult.map { it == Resource.Loading }
        tasks = loadTasksResult.map {

            (it as? Resource.Success)?.data?.result ?: emptyList()
        }
    }

    fun onSwipeRefresh() {
        load()
    }

    private fun load() {

        disposable.add(taskApi.tasks(taskDate).toResource()
            .subscribe {
                loadTasksResult.value = it
                if (it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            })

    }


    override fun onOpen(t: String) {
        _navigateToOperationAction.value = Event(t)
    }

    fun selectDate(){
//        clickTopResult.value = "DATE"
    }

    fun selectType(){
//        clickTopResult.value = "TYPE"
    }

    fun search(){
//        clickTopResult.value = "SEARCH"
    }




}