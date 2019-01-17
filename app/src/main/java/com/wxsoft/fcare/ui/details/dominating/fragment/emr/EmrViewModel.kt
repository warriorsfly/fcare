package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.EmrApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EmrViewModel @Inject constructor(private val emrApi: EmrApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {

    val bitmaps= mutableListOf<String>()
    val emrs:LiveData<List<EmrItem>>
    private val loadEmrResult=MediatorLiveData<Response<List<EmrItem>>>()
    var preHos=true
    init {

        emrs=loadEmrResult.map { it.result?: emptyList()}
    }
    var patientId=""
        set(value) {
           field=value
            loadEms(field)
        }

    /**
     * 结果信息
     */
    private val _loadEmrItemAction = MutableLiveData<Event<Pair<Int,String>>>()
    val emrItemLoaded: LiveData<Event<Pair<Int,String>>>
        get() = _loadEmrItemAction

    private fun loadEms(id:String) {

        (if (preHos) emrApi.getPreEmrs(id) else emrApi.getInEmrs(id)).zipWith(emrApi.getBaseInfo(patientId))
            .subscribeOn(Schedulers.computation())
            .doOnSuccess { zip ->
                val list = zip.first.result
                list?.first { it.code == ActionRes.ActionType.患者信息录入 }?.apply {
                    result = zip.second.result
                    done = true
                    completedAt = zip.second.result?.createdDate
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->

                loadEmrResult.value = it?.first

                //生命体征
                emrApi.getVitals(patientId)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ vital ->
                        if (vital.isNullOrEmpty()) return@subscribe

                        loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.生命体征 }
                            ?.let {
                                if (!vital.isNullOrEmpty()) {
                                    it.result = vital
                                    it.done = true
                                    it.completedAt = vital[0].createdDate
                                }
                            }
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                        index?.let { index ->
                            _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.心电图))
                        }

                    }, {
                        messageAction.value = Event(it.message ?: "")
                    })
                //PhysicalExamination
                emrApi.getBodyCheck(patientId)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ check ->
                        check?.result ?: return@subscribe
                        loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.PhysicalExamination }?.let {
                            item->
                            item.result=check?.result
                            if(!item.done){
                                item.done=true
                                item.completedAt=check?.result?.createdDate
                            }
                        }
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.PhysicalExamination }
                        index?.let { index ->
                            _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.PhysicalExamination))
                        }
                    }, {
                        messageAction.value = Event(it.message ?: "")
                    })

                emrApi.getEcgs(patientId).subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ check ->
                        //                        check?.result?: return@subscribe
                        loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.心电图 }
                            ?.result = check?.result ?: ElectroCardiogram()
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                        index?.let { index ->
                            _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.心电图))
                        }
                    },{
                        messageAction.value= Event(it.message?:"")
                    })
                emrApi.loadMedicalHistory(patientId).subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                            history->
                        loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.IllnessHistory}?.result=history?.result?: MedicalHistory("")
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.IllnessHistory }
                        index?.let { index ->
                            _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.IllnessHistory))
                        }
                    }, {
                        messageAction.value = Event(it.message ?: "")
                    })
                emrApi.getDiagnosis(patientId,1).subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                            diagnose->
                        loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.院前诊断}?.result=diagnose?.result?: Diagnosis("")
                        val index =
                            loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.院前诊断 }
                        index?.let { index ->
                            _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.院前诊断))
                        }
                    }, {
                        messageAction.value = Event(it.message ?: "")
                    })



                refreshRating()

                refreshMeasure()
            }
    }

    fun diagnose(string:String) {
        val item = (loadEmrResult.value?.result?.first {
            it.code == ActionRes.ActionType.心电图
        }?.result as? ElectroCardiogram)?.apply {
            diagnoseResult = string
            doctorId = account.id
            doctorName = account.userName
        }
        item?.let {
            emrApi.diagnose(it).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({

                    loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.心电图 }
                        ?.result = it?.result ?: ElectroCardiogram()
                    val index =
                        loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                    index?.let { index ->
                        _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.心电图))
                    }

                }, { throwable ->

                    it.savable = true
                    messageAction.value = Event(throwable.message ?: "")
                })

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
        val item= (loadEmrResult.value?.result?.first {
            it.code==ActionRes.ActionType.心电图
        }?.result as? ElectroCardiogram )?.apply {
            savable=false
            if(patientId.isNullOrEmpty()) {
                patientId = this@EmrViewModel.patientId
            }
        }


        item?.let {
            emrApi.saveEcg(it, files)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    emrApi.getEcgs(patientId)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ check ->
                            check ?: return@subscribe
                            loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.心电图 }
                                ?.let { emr ->
                                    emr.result = check?.result
                                    emr.done = true
                                    emr.completedAt = check?.result?.time
                                }
                            val index =
                                loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.心电图 }
                            index?.let { index ->
                                _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.心电图))
                            }
                        }, {
                            messageAction.value = Event(it.message ?: "")
                        })

                }, { throwable ->

                    it.savable = true
                    messageAction.value = Event(throwable.message ?: "")
                })

        }
    }

    fun refreshRating(){
        emrApi.getRecords(patientId).subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({ rating ->
                rating?.result ?: return@subscribe
                loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.GRACE }?.result =
                        rating?.result
                val index =
                    loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.GRACE }
                index?.let { index ->
                    _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.GRACE))
                }
            },{
                messageAction.value= Event(it.message?:"")
            })
    }

    fun refreshMeasure(){
        emrApi.loadMeasure(patientId).subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread()).subscribe ({ rating ->
                rating?.result ?: return@subscribe
                loadEmrResult.value?.result?.first { emr -> emr.code == ActionRes.ActionType.DispostionMeasures }?.result =
                        rating?.result
                val index =
                    loadEmrResult.value?.result?.indexOfFirst { emr -> emr.code == ActionRes.ActionType.DispostionMeasures }
                index?.let { index ->
                    _loadEmrItemAction.value = Event(Pair(index, ActionRes.ActionType.DispostionMeasures))
                }
            },{
                messageAction.value= Event(it.message?:"")
            })
    }
}
