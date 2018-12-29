package com.wxsoft.fcare.ui.main.fragment.assignment


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.ObservableField
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.map
import javax.inject.Inject


class AssignmentViewModel @Inject constructor(val taskApi: TaskApi) : ViewModel(), EventActions {

    val isLoading: LiveData<Boolean>
    val tasks: LiveData<List<Task>>
//    val t:ObservableField<String>
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
            var ps = (it as? Resource.Success)?.data!!.result ?: emptyList()
//            if(ps.isNotEmpty()){
//                for(p in ps){
//                }
//            }
            return@map ps
        }
    }

    fun onSwipeRefresh() {
        load()
    }

    private fun load() {
        taskApi.tasks(taskDate).toResource()
            .subscribe {
                loadTasksResult.value = it
                if (it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }

    }

    override fun onOpen(id: String) {
        _navigateToOperationAction.value = Event(id)
    }


}