package com.wxsoft.fcare.ui.main.fragment.patients

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class PatientsViewModel @Inject constructor(private val repository: IPatientRepository,
                                            private val dicEnumApi: DictEnumApi,
                                            private val accountApi: AccountApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon),EventActions {

    private val _detailAction= MutableLiveData<Event<String>>()
    val detailAction: LiveData<Event<String>>
        get() = _detailAction
    override fun onOpen(t: String) {
        _detailAction.value=Event(t)
    }

    var noPatientsShow= ObservableField<Boolean>()


    val clickTop:LiveData<String>
    val clickTopResult = MediatorLiveData<String>()

    val clickCusDate:LiveData<String>
    private val clickCusDateResult = MediatorLiveData<String>()


    val checkCondition:LiveData<PatientsCondition>
    private val checkConditionResult = MediatorLiveData<PatientsCondition>()

    val typeItems: LiveData<List<Dictionary>>
    private val loadtypeItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {
        clickTop = clickTopResult.map { it }
        checkCondition = checkConditionResult.map { it ?: PatientsCondition(1, 10).apply {
            startDate = getTimesmorning()
            endDate = DateTimeUtils.getCurrentTime()
            currUserId = account.id
        } }
        clickCusDate = clickCusDateResult.map { it }
        checkConditionResult.value = null
        typeItems = loadtypeItemsResult.map {
            (
                    (it as? Resource.Success)?.data?.apply {
                        var arr = ArrayList<Dictionary>()
                        arr.add(Dictionary("", "全部类型").apply { checked = true })
                        arr.addAll(it.data)
                        return@map arr
                    }) ?: emptyList()
        }
        getMeasuresItems()

        /**
         * 在主页进行jpush相关
         */
        sharedPreferenceStorage.registrationId?.let {
            disposable.add(accountApi.jPush(account.id, it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},::error))
        }
    }

    private val patientName = MediatorLiveData<String>()

    private val patientResult = patientName.map{
        repository.getPatients(checkCondition.value?:PatientsCondition(1,10).apply {
            startDate = getTimesmorning()
            endDate = DateTimeUtils.getCurrentTime()
            currUserId = account.id
        })
    }

    val patients = patientResult.switchMap {
        it.pagedList
    }

    val networkState = patientResult.switchMap {
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
    private fun getTimesmorning(): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime())
    }
    // 获得本周一0点时间
    private fun getTimesWeekmorning(): String {
        val cal = Calendar.getInstance();
        cal.time = Date();
        // 获得当前日期是一个星期的第几天
        val dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.firstDayOfWeek = Calendar.MONDAY;
        // 获得当前日期是一个星期的第几天
        val day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.firstDayOfWeek - day)
        val str =DateTimeUtils.formatter.format(cal.time)
        return str.substring(0,11) + "00:00:00"
    }

    // 获得本月第一天0点时间
    private fun getTimesMonthmorning(): String {
        val cal = Calendar.getInstance().apply {
            set(get(Calendar.YEAR), get(Calendar.MONTH), getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0)
        }

        return DateTimeUtils.formatter.format(cal.time)
    }
    private fun getCurrentQuarterStartTime(): String? {
        val c = Calendar.getInstance().apply {
            when ( get(Calendar.MONTH) + 1) {
                in 1..3 -> set(Calendar.MONTH, 0)
                in 4..6 -> set(Calendar.MONTH, 3)
                in 7..9 -> set(Calendar.MONTH, 4)
                in 10..12 -> set(Calendar.MONTH, 9)
            }
            set(Calendar.DATE,1)
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }

        return DateTimeUtils.formatter.format(c.time)
    }
    private fun getCurrentYearStartTime(): String {
        val cal = Calendar.getInstance();
        return ""+cal.get(Calendar.YEAR) + "-01-01 00:00:00"
    }
}