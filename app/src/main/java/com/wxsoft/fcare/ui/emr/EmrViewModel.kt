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
import com.wxsoft.fcare.utils.ActionCode
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
    fun loadEmrs() {
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
        if(pair.first<0)return
        when (pair.second) {
            ActionType.患者信息录入 ->  loadBaseInfo(pair.first)
            ActionType.生命体征-> loadVitals(pair.first)
            ActionType.GRACE-> loadRating(pair.first)
            ActionType.DispostionMeasures-> loadMeasure(pair.first)
            ActionType.给药-> loadDrugList(pair.first)
            ActionType.诊断-> loadDiagnosis(pair.first)
            ActionType.IllnessHistory-> loadMedHis(pair.first)
            ActionType.PhysicalExamination-> loadBodyCheck(pair.first)
            ActionType.知情同意书-> loadTalks(pair.first)
            ActionType.溶栓处置-> loadThrombolysis(pair.first)
            ActionType.出院诊断-> loadOtDiagnosis(pair.first)
            ActionType.治疗策略-> loadStrategy(pair.first)
            ActionType.CT_OPERATION-> loadPACS(pair.first)
            ActionType.主诉及症状-> loadComplaints(pair.first)
            ActionType.Catheter-> loadIntervention(pair.first)
            ActionType.CABG-> loadCABG(pair.first)
            ActionType.患者转归-> loadOT(pair.first)
            ActionType.心电图-> loadEcgs(pair.first)
            ActionType.ACS给药-> loadAcs(pair.first)
            ActionType.来院方式-> loadComingBy(pair.first)
        }
    }

    private fun indexOf(actionType: String):Pair<Int,String>{
        return (emrs.value?.indexOfFirst { it.code==actionType }?:-1).let { Pair(it,actionType) }
    }

    fun refresh(code:Int){

        val actionType=when(code){
            ActionCode.BASE_INFO ->ActionType.患者信息录入
            ActionCode.VITAL_SIGNS -> ActionType.生命体征
            ActionCode.RATING ->ActionType.GRACE
            ActionCode.MEASURES ->ActionType.DispostionMeasures
            ActionCode.DRUGRECORD ->ActionType.给药
            ActionCode.DIAGNOSE ->ActionType.诊断
            ActionCode.MEDICAL_HISTORY_CODE ->ActionType.IllnessHistory
            ActionCode.CHECK_BODY -> ActionType.PhysicalExamination
            ActionCode.INFORMEDCONSENT ->  ActionType.知情同意书
            ActionCode.THROMBOLYSIS -> ActionType.溶栓处置
            ActionCode.OTDIAGNOSE -> ActionType.出院诊断
            ActionCode.STRATEGY -> ActionType.治疗策略
            ActionCode.CT_OPERATION -> ActionType.CT_OPERATION
            ActionCode.COMPLAINTS -> ActionType.主诉及症状
            ActionCode.Catheter -> ActionType.Catheter
            ActionCode.CABG ->ActionType.CABG
            ActionCode.ECG ->ActionType.心电图
            ActionCode.OUTCOME ->ActionType.患者转归
            ActionCode.ACS ->ActionType.ACS给药
            ActionCode.COMEBY ->ActionType.来院方式
            else->null
        }
        actionType?.let(::indexOf)?.let(::loadDetail)
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
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取病评分
     */
    private fun loadRating(index:Int){
        disposable.add(api.getScencelyRatings(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取病史
     */
    private fun loadMedHis(index:Int){
        disposable.add(api.loadMedicalHistory(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }
  /**
     * 获取查体
     */
    private fun loadBodyCheck(index:Int){
        disposable.add(api.getBodyCheck(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取知情谈话
     */
    private fun loadTalks(index:Int){
        disposable.add(api.getTalks(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取病人生命体征
     */
    private fun loadVitals(index:Int){
        disposable.add(api.getVitals(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }/**
     * 获取溶栓处置
     */
    private fun loadThrombolysis(index:Int){
        disposable.add(api.loadThrombolysis(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }



    /**
     * 获取病人接收到的治疗措施
     */
    private fun loadMeasure(index:Int){
        disposable.add(api.loadMeasure(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取病人接收到的治疗措施
     */
    private fun loadDrugList(index:Int){
        disposable.add(api.getDrugRecord(patientId)
            .map { Pair(index,it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    /**
     * 获取病人病情诊断
     */
    private fun loadDiagnosis(index:Int){
        disposable.add(api.getLastDiagnose(patientId)
            .map {
//                val item=it.result?.lastOrNull()?.let {
//                        diagnosis->
//                    Response<Diagnosis>(true ).apply{
//                        result=diagnosis
//                    }
//                }?:it
                Pair(index,it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }
    /**
     * 获取病人出院诊断
     */
    private fun loadOtDiagnosis(index:Int){
        disposable.add(api.getOtDiagnosis(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }/**
     * 获取病人治疗策略
     */
    private fun loadStrategy(index:Int){
        disposable.add(api.getStrategy(patientId)
            .map {
                Pair(index,it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadPACS(index:Int){
        disposable.add(api.getPAC(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadComplaints(index:Int){
        disposable.add(api.getComplaints(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadIntervention(index:Int){
        disposable.add(api.getIntervention(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadCABG(index:Int){
        disposable.add(api.getCABG(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadOT(index:Int){
        disposable.add(api.getOt(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadEcgs(index:Int){
        disposable.add(api.getEcgs(patientId)
            .map {Pair(index,it.apply {
                result?.apply {
                    diagnoseText=diagnoses.joinToString("\n",transform={ (diagnoses.indexOf(it)+1).toString()+"."+it.name})
                }
            })}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadAcs(index:Int){
        disposable.add(api.getACSDrug(patientId)
            .map {Pair(index,it)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setResultAtIndex,::error))
    }

    private fun loadComingBy(index:Int){
        disposable.add(api.getComing(patientId)
                .map {Pair(index,it)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::setResultAtIndex,::error))
    }


}
