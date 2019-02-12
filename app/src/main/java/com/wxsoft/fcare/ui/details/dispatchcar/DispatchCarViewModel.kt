package com.wxsoft.fcare.ui.details.dispatchcar

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
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
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class DispatchCarViewModel @Inject constructor(
    private val  taskApi: TaskApi,
    private val carApi: CarApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson): BaseViewModel(sharedPreferenceStorage,gon), EventActions ,
    ICommonPresenter {

    override var title: String=""
        get() = "发车"
    override val clickableTitle: String
        get() = "立即发车"
    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    val task: LiveData<Task>
    private val initTask= MediatorLiveData<Task>()

    val doctors: LiveData<List<User>>
    val nurses: LiveData<List<User>>
    val drivers: LiveData<List<User>>
    val cars: LiveData<List<Car>>
    var selectedCar:Car
    override val account: Account

    var taskId: LiveData<String>
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
        clickable = clickResult.map { it }
        val s = sharedPreferenceStorage.userInfo!!
        account = gon.fromJson(s, Account::class.java)
        task = initTask.map { it }
        initTask.value = Task("")
        selectedCar = Car("","","",false,"","",false)
        cars = loadCarsResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        doctors = loadDoctorsResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        nurses = loadNursesResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        drivers = loadDriversResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        taskId = initTaskId.map { (it as? Resource.Success)?.data?.result ?: "" }
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

    private fun getCars(){
        disposable.add(carApi.cars().toResource()
            .subscribe{
                loadCarsResult.value = it

            }
        )
    }

    private fun getDoctors(){
        disposable.add(taskApi.getDoctors(account.id).toResource()
            .subscribe{
                loadDoctorsResult.value = it
            }
        )
    }

    private fun getNurses(){
        disposable.add(taskApi.getNurses(account.id).toResource()
            .subscribe{
                loadNursesResult.value = it
            }
        )
    }

    private fun getDrivers(){
        disposable.add(taskApi.getDrivers(account.id).toResource()
            .subscribe{
                loadDriversResult.value = it
            }
        )
    }


    override fun onOpen(id: String) {

    }
    fun selectCar(car:Car){
        if (car.status.equals("203-1")){
            if (!car.id.equals(selectedCar.id)){
                selectedCar.selectStatus = !selectedCar.selectStatus
                car.selectStatus = !car.selectStatus
                selectedCar = car
            }
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

    fun submitBtnClick(){
        var staffs:ArrayList<TaskStaff> = ArrayList()
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

    override fun click(){
        submitBtnClick()
    }


}