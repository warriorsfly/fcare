package com.wxsoft.fcare.ui.main.fragment.task


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.domain.repository.tasks.ITaskRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class TaskViewModel @Inject constructor(private val taskApi: TaskApi,
                                        private val repository: ITaskRepository,
                                        private val dicEnumApi: DictEnumApi,
                                        override val sharedPreferenceStorage: SharedPreferenceStorage,
                                        override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon), EventActions {

//    val success: LiveData<Boolean>
//    val tasks: LiveData<List<Task>>
    var taskDate: String = DateTimeUtils.getCurrentDate()
    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val _errorToOperationAction = MutableLiveData<Event<String>>()
    private val loadTasksResult = MediatorLiveData<Resource<Response<List<Task>>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction


    val checkCondition:LiveData<PatientsCondition>
    private val checkConditionResult = MediatorLiveData<PatientsCondition>()

    val clickCusDate:LiveData<String>
    private val clickCusDateResult = MediatorLiveData<String>()

    val clickTop:LiveData<String>
    val clickTopResult = MediatorLiveData<String>()

    val typeItems: LiveData<List<Dictionary>>
    private val loadtypeItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val searchTasks: LiveData<List<Task>>
    private val loadSearchTasksResult = MediatorLiveData<Resource<Response<List<Task>>>>()

    init {
//        load()
        searchTasks = loadSearchTasksResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
        clickTop = clickTopResult.map { it }
        clickCusDate = clickCusDateResult.map { it }
        checkCondition = checkConditionResult.map { it?:PatientsCondition(1,10).apply {
            startDate = getTimesmorning()
            endDate = DateTimeUtils.getCurrentTime()
        } }
        checkConditionResult.value = null
//        success = loadTasksResult.map { it == Resource.Loading }
//        tasks = loadTasksResult.map {
//
//            (it as? Resource.Success)?.data?.result ?: emptyList()
//        }

        typeItems = loadtypeItemsResult.map { (
                (it as? Resource.Success)?.data?.apply {
                    var arr = ArrayList<Dictionary>()
                    arr.add(Dictionary("","全部类型").apply { checked = true })
                    arr.addAll(it.data)
                    return@map arr
                })?: emptyList() }
        getMeasuresItems()
    }


    private val patientName = MediatorLiveData<String>()

    private val taskResult = patientName.map{
        repository.getTasks(checkCondition.value?:PatientsCondition(1,10).apply {
            startDate = getTimesmorning()
            endDate = DateTimeUtils.getCurrentTime()
        } )
    }

    val tasks = taskResult.switchMap {
        it.pagedList
    }

    val networkState = taskResult.switchMap {
        it.networkState
    }

    fun showPatients(name: String): Boolean {
        patientName.value = name
        return true
    }

    fun onSwipeRefresh(): Boolean {
        showPatients(patientName.value?:"")
        return true
    }



    fun searchTasks(name: String): Boolean{
        taskApi.searchTasks(name).toResource()
            .subscribe {
                loadSearchTasksResult.value = it
            }

        return true
    }
//    fun onSwipeRefresh() {
//        load()
//    }

//    private fun load() {
//
//        disposable.add(taskApi.tasks(taskDate).toResource()
//            .subscribe {
//                loadTasksResult.value = it
//                if (it is Resource.Error) {
//                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
//                }
//            })
//
//    }




    override fun onOpen(t: String) {
        _navigateToOperationAction.value = Event(t)
    }

    fun selectDate(){
        clickTopResult.value = "DATE"
    }

    fun selectType(){
        clickTopResult.value = "TYPE"
    }

    fun search(){
        clickTopResult.value = "SEARCH"
    }
    //拉取所有大病种类
    private fun getMeasuresItems(){
        dicEnumApi.loadDiagnoseTypeResultItems().toResource()
            .subscribe {
                    items->
                loadtypeItemsResult.value = items
            }
    }
    fun selectIllessType(item:Dictionary){
        typeItems.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
        checkCondition.value?.diagnoseType = item.id
        checkCondition.value?.diagnoseTypeStr = item.itemName
        patientName.value = ""
        clickCusDateResult.value = "选择类型确定"
    }

    fun clickQuckDate(item:String){
        checkCondition.value?.checkedCusDate = true
        checkCondition.value?.startDate = ""
        checkCondition.value?.endDate = ""
        when(item){
            "今天" ->{
                checkCondition.value?.checkedCusDateStr = "今天"
                checkCondition.value?.startDate = getTimesmorning()
                checkCondition.value?.endDate = DateTimeUtils.getCurrentTime()
            }
            "本周" ->{
                checkCondition.value?.checkedCusDateStr = "本周"
                checkCondition.value?.startDate = getTimesWeekmorning()
                checkCondition.value?.endDate = DateTimeUtils.getCurrentTime()
            }
            "本月" ->{
                checkCondition.value?.checkedCusDateStr = "本月"
                checkCondition.value?.startDate = getTimesMonthmorning()
                checkCondition.value?.endDate = DateTimeUtils.getCurrentTime()
            }
            "本季度" ->{
                checkCondition.value?.checkedCusDateStr = "本季度"
                checkCondition.value?.startDate = getCurrentQuarterStartTime().toString()
                checkCondition.value?.endDate = DateTimeUtils.getCurrentTime()
            }
            "本年" ->{
                checkCondition.value?.checkedCusDateStr = "本年"
                checkCondition.value?.startDate = getCurrentYearStartTime()
                checkCondition.value?.endDate = DateTimeUtils.getCurrentTime()
            }
        }
    }

    fun clickCusDate(item:String){
        checkCondition.value?.checkedCusDate = false
        checkCondition.value?.checkedCusDateStr = "自定义时间"
        clickCusDateResult.value = item
    }

    fun sureDate(){
        patientName.value = ""
        clickCusDateResult.value = "选择时间确定"
    }

    // 获得当天0点时间
    fun getTimesmorning(): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime())
    }
    // 获得本周一0点时间
    fun getTimesWeekmorning(): String {
        val cal = Calendar.getInstance();
        cal.setTime(Date());
        // 获得当前日期是一个星期的第几天
        val dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        val day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day)
        val str = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.time)
        return str.substring(0,11) + "00:00:00"
    }

    // 获得本月第一天0点时间
    fun getTimesMonthmorning(): String {
        val cal = Calendar.getInstance()
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.time)
    }
    fun getCurrentQuarterStartTime(): String? {
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val longSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val shortSdf = SimpleDateFormat("yyyy-MM-dd")
        var now: Date? = null
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0)
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3)
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4)
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9)
            c.set(Calendar.DATE, 1)
            now = longSdf.parse(shortSdf.format(c.time) + " 00:00:00")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)
    }
    fun getCurrentYearStartTime(): String {
        val cal = Calendar.getInstance();
        return ""+cal.get(Calendar.YEAR) + "-01-01 00:00:00"
    }


}