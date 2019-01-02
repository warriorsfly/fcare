package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.CarApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.utils.DateTimeUtils
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class DispatchCarViewModel @Inject constructor(gson: Gson,
                                               sharedPreferenceStorage: SharedPreferenceStorage,
                                               val  taskApi: TaskApi,
                                               val carApi: CarApi): ViewModel(), EventActions {

    val task: LiveData<Task>
    val doctors: LiveData<List<User>>
    val nurses: LiveData<List<User>>
    val drivers: LiveData<List<User>>
    val cars: LiveData<List<Car>>
    val account: Account
    var taskId: String = ""

    private val initTask= MediatorLiveData<Task>()
    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val loadDoctorsResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadNursesResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadDriversResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadCarsResult= MediatorLiveData<Resource<Response<List<Car>>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction

    init {
        val s = sharedPreferenceStorage.userInfo!!
        account = gson.fromJson(s, Account::class.java)
        task = initTask.map { it }
        initTask.value = Task("")
        cars = loadCarsResult.map { (it as? Resource.Success)?.data!!.result ?: emptyList() }
        doctors = loadDoctorsResult.map { (it as? Resource.Success)?.data!!.result ?: emptyList() }
        nurses = loadNursesResult.map { (it as? Resource.Success)?.data!!.result ?: emptyList() }
        drivers = loadDriversResult.map { (it as? Resource.Success)?.data!!.result ?: emptyList() }
        getCars()
        getDoctors()
        getNurses()
        getDrivers()
    }

    private fun saveTask(){
        task.value!!.createdDate = DateTimeUtils.getCurrentTime()
        task.value!!.createdBy = account.userName
        taskApi.save(this.task.value!!).toResource()
            .subscribe{
                taskId = (it as? Resource.Success)?.data!!.result ?:""
            }
    }

    private fun getCars(){
        carApi.cars().toResource()
            .subscribe{
                loadCarsResult.value = it
            }
    }

    private fun getDoctors(){
        taskApi.getDoctors(account.id).toResource()
            .subscribe{
                loadDoctorsResult.value = it
            }
    }

    private fun getNurses(){
        taskApi.getNurses(account.id).toResource()
            .subscribe{
                loadNursesResult.value = it
            }
    }

    private fun getDrivers(){
        taskApi.getDrivers(account.id).toResource()
            .subscribe{
                loadDriversResult.value = it
            }
    }


    private fun arrivered(){
        if (taskId.isNotEmpty()) _navigateToOperationAction.value= Event(taskId)
    }

    override fun onOpen(id: String) {

        when(id){
            "1"->saveTask()
//            "2"->getDoctors()
            "2"->getNurses()
//            "2"->getDrivers()
            "3"->arrivered()
        }

    }
}