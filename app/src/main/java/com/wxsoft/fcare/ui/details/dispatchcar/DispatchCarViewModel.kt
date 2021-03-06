package com.wxsoft.fcare.ui.details.dispatchcar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.CarApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DispatchCarViewModel @Inject constructor(
    private val  taskApi: TaskApi,
    private val carApi: CarApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson): BaseViewModel(sharedPreferenceStorage,gon), EventActions {



    var taskId=""
    set(value) {
        field=value
        if(field.isNotEmpty()){
            loadTask(field)
        }
    }
    val task: LiveData<Task>
    private val initTask= MediatorLiveData<Task>()

    val doctors: LiveData<List<User>>
    val nurses: LiveData<List<User>>
    val drivers: LiveData<List<User>>
    val cars: LiveData<List<Car>>
    private var selectedCar:Car
    override val account: Account

    var taskSavedId: LiveData<String>
    private val initTaskId= MediatorLiveData<Resource<Response<String>>>()

    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val loadDoctorsResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadNursesResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadDriversResult= MediatorLiveData<Resource<Response<List<User>>>>()
    private val loadCarsResult= MediatorLiveData<Resource<Response<List<Car>>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction

    val haveSelectCar:LiveData<Boolean>
    private val initHaveSelectCar = MediatorLiveData<Boolean>()


    init {
        haveSelectCar = initHaveSelectCar.map { it }
        val s = sharedPreferenceStorage.userInfo!!
        account = gon.fromJson(s, Account::class.java)
        task = initTask.map { it }
        initTask.value = Task("")
        selectedCar = Car("","","",false,"","",false)
        cars = loadCarsResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        doctors = loadDoctorsResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        nurses = loadNursesResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        drivers = loadDriversResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        taskSavedId = initTaskId.map { (it as? Resource.Success)?.data?.result ?: "" }
        getCars()
        getDoctors()
        getNurses()
        getDrivers()

    }

    private fun saveTask(){
        task.value?.createdDate = DateTimeUtils.getCurrentTime()
        task.value?.createdBy = account.userName
        disposable.add(taskApi.save(task.value!!).toResource()
            .subscribe{
                initTaskId.value = it
            }
        )
    }

    private fun loadTask(id:String){
        disposable.add(taskApi.task(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadTaskResult,::error))
    }
    private fun loadTaskResult(response:Response<Task>){
        initTask.value = response.result
        haveData()
    }

    fun haveData(){
        cars.value?.filter { it.id.equals(task.value?.carId) }?.map {
            it.selectStatus = true
            selectedCar = it
        }
        val ids = task.value?.taskStaffs?.map { it.staffId }
        if (ids!!.isNotEmpty()){
            doctors.value?.filter {ids.contains(it.id)}?.map { it.status = true }
            nurses.value?.filter {ids.contains(it.id)}?.map { it.status = true }
            drivers.value?.filter {ids.contains(it.id)}?.map { it.status = true }
        }
    }

    private fun getCars(){
        disposable.add(carApi.cars().toResource()
            .subscribe{
                loadCarsResult.value = it
                haveData()
            }
        )
    }

    private fun getDoctors(){
        disposable.add(taskApi.getDoctors(account.id).toResource()
            .subscribe{
                loadDoctorsResult.value = it
                haveData()
            }
        )
    }

    private fun getNurses(){
        disposable.add(taskApi.getNurses(account.id).toResource()
            .subscribe{
                loadNursesResult.value = it
                haveData()
            }
        )
    }

    private fun getDrivers(){
        disposable.add(taskApi.getDrivers(account.id).toResource()
            .subscribe{
                loadDriversResult.value = it
                haveData()
            }
        )
    }


    override fun onOpen(t: String) {

    }

    fun selectCar(car:Car){
        if (car.status == "203-1"){
            cars.value?.filter { it.selectStatus }?.map { it.selectStatus = false }
            car.selectStatus = true
            selectedCar = car
        }
    }

    fun selectDoctor(doctor:User){
        doctor.status = !doctor.status
    }

    fun selectNurse(nurse:User){
        nurse.status = !nurse.status
    }

    fun selectDriver(driver:User){
        driver.status = !driver.status
    }

    private fun submitBtnClick(){
        val staffs:ArrayList<TaskStaff> = ArrayList()
        val dstaff=doctors.value?.filter { it.status }
            ?.map { TaskStaff("","",it.id,it.trueName,"3")}?: emptyList()
        val nstaff=nurses.value?.filter { it.status }
            ?.map { TaskStaff("","",it.id,it.trueName,"2")}?: emptyList()
        val drtaff=drivers.value?.filter { it.status }
            ?.map { TaskStaff("","",it.id,it.trueName,"7")}?: emptyList()
        staffs.addAll(dstaff)
        staffs.addAll(nstaff)
        staffs.addAll(drtaff)
        task.value?.taskStaffs = staffs.toTypedArray()
        if (selectedCar.id.isEmpty()){
            initHaveSelectCar.value = true
            return
        }else{
            task.value?.carId = selectedCar.id
        }
        saveTask()
    }

    fun click(){
        submitBtnClick()
    }


}