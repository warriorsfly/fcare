package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.EmrApi
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EmrViewModel @Inject constructor(private val emrApi: EmrApi,
                                       private val pacsApi: PACSApi,
                                       private val interventionApi: InterventionApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {

    val bitmaps= mutableListOf<String>()
    val emrs:LiveData<List<EmrItem>>
    private val loadEmrResult=MediatorLiveData<Response<List<EmrItem>>>()

    val patient:LiveData<Patient>
    private val loadPatientResult=MediatorLiveData<Response<Patient>>()
    var preHos=true
    init {

        patient=loadPatientResult.map { it.result?: Patient(id = "") }
        emrs=loadEmrResult.map { it.result?: emptyList()}
    }
    var patientId=""
        set(value) {
           field=value
            loadEms()
        }

    /**
     * 结果信息
     */
    private val _loadEmrItemAction = MutableLiveData<Event<Pair<Int,String>>>()
    val emrItemLoaded: LiveData<Event<Pair<Int,String>>>
        get() = _loadEmrItemAction

    private fun loadEms() {


       val dis= emrApi.getEmrs(patientId,account.id,preHos).zipWith(emrApi.getBaseInfo(patientId))
            .subscribeOn(Schedulers.computation())
            .doOnSuccess { zip ->
                val list = zip.first.result
                list?.firstOrNull { it.code == ActionRes.ActionType.患者信息录入 }?.apply {
                    result = zip.second.result
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {it->

                loadEmrResult.value = it?.first
                loadPatientResult.value=it?.second
                disposable.add(emrApi.getEcgs(patientId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ check ->
                        //                        check?.result?: return@subscribe
                         loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.心电图 }
                            ?.result = check?.result ?: ElectroCardiogram()
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                        index?.let { ind ->
                            _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.心电图))
                        }
                    },{
                        messageAction.value= Event(it.message?:"")
                    })
                )

                refreshMedicalHistory()
                refreshVitals()
                refreshRating()
                refreshMeasure()
                refreshComplaints()
                refreshStrategy()
                refreshDiagnose()
                refreshChekBody()
                refreshThrombosis()
                refreshInformedConsent()
                refreshDrugRecords()
                refreshOtDiagnosis()
                refreshCT()
                refreshInv()
                refreshCABG()
                refreshOt()
            }

        disposable.add(dis)
    }

    fun diagnose(string:String) {
        val item = ( loadEmrResult.value?.result?.firstOrNull {
            it.code == ActionRes.ActionType.心电图
        }?.result as? ElectroCardiogram)?.apply {
            diagnoseResult = string
            doctorId = account.id
            doctorName = account.userName
        }
        item?.let {
            disposable.add(emrApi.diagnose(it).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({

                     loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.心电图 }
                        ?.result = it?.result ?: ElectroCardiogram()
                    val index =
                        loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                    index?.let { ind ->
                        _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.心电图))
                    }

                }, { throwable ->

                    it.savable = true
                    messageAction.value = Event(throwable.message ?: "")
                })
            )

        }
    }

    fun saveEcg(){
        val files = bitmaps.map {
            val file = File(it)
            return@map MultipartBody.Part.createFormData(
                "images",
                it.split("/").last(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        }
        val item= ( loadEmrResult.value?.result?.firstOrNull {
            it.code==ActionRes.ActionType.心电图
        }?.result as? ElectroCardiogram )?.apply {
            savable=false
            if(patientId.isEmpty()) {
                patientId = this@EmrViewModel.patientId
            }
        }


        item?.let {
            disposable.add(emrApi.saveEcg(it, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    emrApi.getEcgs(patientId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ check ->
                            check ?: return@subscribe
                             loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.心电图 }
                                ?.let { emr ->
                                    emr.result = check.result
                                    emr.done = true
                                    emr.completedAt = check.result?.time
                                }
                            val index =
                                loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.心电图))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })

                }, { throwable ->

                    it.savable = true
                    messageAction.value = Event(throwable.message ?: "")
                })
            )

        }
    }

    fun refreshDiagnose() {

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.诊断 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getDiagnosisList(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result.isNullOrEmpty()) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.lastOrNull()?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.诊断))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshChekBody(){
        //PhysicalExamination
        disposable.add(emrApi.getBodyCheck(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ check ->
                check?.result ?: return@subscribe
                 loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.PhysicalExamination }?.let {
                        item->
                    item.result= check.result
                    if(!item.done){
                        item.done=true
                        item.completedAt= check.result?.createdDate
                    }
                }
                val index =
                    loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.PhysicalExamination }
                index?.let { ind ->
                    _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.PhysicalExamination))
                }
            }, {
                messageAction.value = Event(it.message ?: "")
            })
        )
    }

    fun refreshMedicalHistory(){
        disposable.add(emrApi.loadMedicalHistory(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    history->
                history?.result ?: return@subscribe
                 loadEmrResult.value?.result?.firstOrNull { emr->emr.code==ActionRes.ActionType.IllnessHistory}?.let {
                    item->
                    item.result=history.result
                    if(!item.done){
                        item.done=true
                        item.completedAt= history.result?.createdDate
                    }
                }
                val index =
                    loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.IllnessHistory }
                index?.let { ind ->
                    _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.IllnessHistory))
                }
            }, {
                messageAction.value = Event(it.message ?: "")
            })
        )
    }

    fun refreshOtDiagnosis(){
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.出院诊断 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getOtDiagnosis(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result==null) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.出院诊断))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshCT(){
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.CT_OPERATION }
            ?.let { emr ->
                disposable.add(
                    emrApi.getPAC(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result==null) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.CT_OPERATION))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshComplaints() {
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.主诉及症状 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getComplaints(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ complaints ->
                            if (complaints.result.isNullOrEmpty()) return@subscribe
                            emr.result = complaints.result
                            if (complaints.result!!.size<=0)return@subscribe
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = complaints.result?.last()?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.主诉及症状))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshStrategy() {
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.治疗策略 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getStrategy(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ strategy ->
                            if (strategy.result==null) return@subscribe
                            emr.result = strategy.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = strategy.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.治疗策略))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }


    fun refreshInv() {
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.Catheter }
            ?.let { emr ->
                disposable.add(
                    emrApi.getIntervention(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result == null) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.Catheter))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshVitals() {
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.生命体征 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getVitals(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result.isNullOrEmpty()) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.lastOrNull()?.createdDate
                            }

                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.生命体征))
                            }
                        },
                            {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshRating(){

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.GRACE }
            ?.let { emr ->
                disposable.add(
                    emrApi.getRecords(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result.isNullOrEmpty()) return@subscribe
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.lastOrNull()?.createdDate
                            }

                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.GRACE))
                            }
                        },
                            {
                                messageAction.value = Event(it.message ?: "")
                            })
                )
            }

    }

    fun refreshDrugRecords(){

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.给药 }
            ?.let { drug ->
                disposable.add(
                    emrApi.getDrugRecord(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ records ->
                            if (records.result.isNullOrEmpty()) return@subscribe
                            drug.result = records.result
                            if (!drug.done) {
                                drug.done = true
                                drug.completedAt = records.result?.lastOrNull()?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.给药 }
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.给药))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshMeasure(){
        disposable.add(emrApi.loadMeasure(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({ measure ->
                measure?.result ?: return@subscribe
                 loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.DispostionMeasures }?.let {
                    item->
                    item.result=measure.result
                    if(!(item.done || !measure.result!!.measureDtos.isNotEmpty())){
                        item.done=true
                        item.completedAt= measure.result?.createDate
                    }
                }
                val index =
                    loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.DispostionMeasures }
                index?.let { ind ->
                    _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.DispostionMeasures))
                }
            },{
                messageAction.value= Event(it.message?:"")
            })
        )
    }

    fun refreshInformedConsent(){
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.知情同意书 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getTalks(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ talks ->
                            if (talks.result.isNullOrEmpty()) return@subscribe
                            talks.result?.map {
                                it.apply {
                                    judgeTime()
                                }
                            }
                            emr.result = talks.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = talks.result?.lastOrNull()?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.知情同意书))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshThrombosis(){

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.溶栓处置 }
            ?.let { emr ->
                disposable.add(
                    emrApi.loadThrombolysis(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ vitals ->
                            if (vitals.result.isNullOrEmpty()) return@subscribe
                            vitals.result!!.map {
                                it.setUpChecked()
                            }
                            emr.result = vitals.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = vitals.result?.lastOrNull()?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.溶栓处置 }
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.溶栓处置))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }


    fun refreshCABG(){
        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.CABG }
            ?.let { emr ->
                disposable.add(
                    emrApi.getCABG(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ cabg ->
                            cabg?.result ?: return@subscribe
                            emr.result = cabg.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = cabg.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.CABG }
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.CABG))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }


    fun commitNoticePacs(){ //通知启动CT室
        disposable.add(
            pacsApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }

    fun commitNoticeInv(){//通知启动导管室
        disposable.add(
            interventionApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }

    fun refreshOt(){

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.患者转归 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getOt(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ records ->
                            records.result ?: return@subscribe
                            emr.result = records.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = records.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.患者转归))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }

    fun refreshBaseInfo(){

        loadEmrResult.value?.result?.firstOrNull { emr -> emr.code == ActionRes.ActionType.患者信息录入 }
            ?.let { emr ->
                disposable.add(
                    emrApi.getBaseInfo(patientId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe({ records ->
                            records.result ?: return@subscribe

                            loadPatientResult.value=records
                            emr.result = records.result
                            if (!emr.done) {
                                emr.done = true
                                emr.completedAt = records.result?.createdDate
                            }
                            val index =
                                loadEmrResult.value?.result?.indexOf(emr)
                            index?.let { ind ->
                                _loadEmrItemAction.value = Event(Pair(ind, ActionRes.ActionType.患者信息录入))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })
                )
            }
    }


}
