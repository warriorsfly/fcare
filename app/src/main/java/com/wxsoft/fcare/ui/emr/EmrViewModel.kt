package com.wxsoft.fcare.ui.emr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.EmrApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.ActionType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EmrViewModel @Inject constructor(private val api: EmrApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {
    /**
     * 病历数据集
     */
    val emrs: LiveData<List<EmrItem>>
    /**
     * 获取病历结果集
     */
    private val loadEmrResult = MediatorLiveData<Response<List<EmrItem>>>()

    init {
        emrs = loadEmrResult.map { it.result ?: emptyList() }
    }

    private val _loadDetailAction = MutableLiveData<Event<Int>>()
    val emrItemLoaded: LiveData<Event<Int>>
        get() = _loadDetailAction

    /**
     * 是否院前阶段
     */
    var preHos = true
    /**
     * 病人ID
     */
    var patientId = ""
        set(value) {
           field=value
            loadEmrs()
        }



    /**
     * 获取的详情放入emr列表
     */
    private fun setResultAtIndex(pair: Pair<Int,Any>){
        emrs.value?.get(pair.first)?.result=(pair.second as? Response<*>)?.result
        _loadDetailAction.value= Event(pair.first)
    }
    /**
     * 获取emr列表
     */
    private fun loadEmrs() {
       disposable.add( api.getEmrs(patientId, account.id, preHos)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::loadEmrDetails, ::error))
    }

    /**
     * 获取emr详细信息
     * @param pair ,first emr在列表中的序号,second emr类型
     */
    private fun loadDetail(pair:Pair<Int,String>) {
        when (pair.second) {
            ActionType.患者信息录入 ->  loadBaseInfo(pair.first)
            ActionType.生命体征-> loadVitals(pair.first)
            ActionType.GRACE-> loadRating(pair.first)
            ActionType.DispostionMeasures-> loadMeasure(pair.first)
            ActionType.给药-> loadDrugList(pair.first)
        }
    }

    /**
     * 根据获取的emr列表获取每个emr的详细信息
     */
    private fun loadEmrDetails(response: Response<List<EmrItem>>) {
        loadEmrResult.value=response
        response.result?.run {

            val source1 = Observable.range(0, size)
            val source2 = toObservable().map { it.code }

            disposable.add(source1.zipWith(source2).subscribe (::loadDetail,::error))

        }
    }

    /**
     * 获取病人详细信息
     */
    private fun loadBaseInfo(index:Int){
        disposable.add(api.getBaseInfo(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResultAtIndex(it)},::error))
    }

    /**
     * 获取病人生命体征
     */
    private fun loadVitals(index:Int){
        disposable.add(api.getVitals(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResultAtIndex(it)},::error))
    }

    /**
     * 获取病人生命体征
     */
    private fun loadRating(index:Int){
        disposable.add(api.getScencelyRatings(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResultAtIndex(it)},::error))
    }

    /**
     * 获取病人接收到的治疗措施
     */
    private fun loadMeasure(index:Int){
        disposable.add(api.loadMeasure(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResultAtIndex(it)},::error))
    }

    /**
     * 获取病人接收到的治疗措施
     */
    private fun loadDrugList(index:Int){
        disposable.add(api.getDrugRecord(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setResultAtIndex(it)},::error))
    }


}
