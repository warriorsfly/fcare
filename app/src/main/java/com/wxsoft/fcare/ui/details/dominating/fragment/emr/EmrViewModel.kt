package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Response
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

    private fun loadEms(id:String){

        (if(preHos)emrApi.getPreEmrs(id)else emrApi.getInEmrs(id)) .zipWith(emrApi.getBaseInfo(patientId))
            .subscribeOn(Schedulers.computation())
            .doOnSuccess {zip->
                val list= zip.first.result
                list?.first{it.code==ActionRes.ActionType.患者信息录入}?.result=zip.second.result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ it->

                loadEmrResult.value=it?.first

                //生命体征
                emrApi.getVitals(patientId)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        vital->
                        if(vital.isNullOrEmpty()) return@subscribe

                        loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.生命体征}?.result=vital
                        val index=loadEmrResult.value?.result?.indexOfFirst { emr->emr.code==ActionRes.ActionType.生命体征}
                        index?.let {
                            _loadEmrItemAction.value=Event(Pair(it,ActionRes.ActionType.生命体征))
                        }
                    },{
                        messageAction.value= Event(it.message?:"")
                    })
                //PhysicalExamination
                emrApi.getBodyCheck(patientId)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe( {
                            check->
                        check?.result?: return@subscribe
                        loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.辅助检查}?.result=check
                        val index=loadEmrResult.value?.result?.indexOfFirst { emr->emr.code==ActionRes.ActionType.辅助检查}
                        index?.let {
                            _loadEmrItemAction.value=Event(Pair(it,ActionRes.ActionType.辅助检查))
                        }
                    }, {
                        messageAction.value = Event(it.message ?: "")
                    })

                emrApi.getEcgs(patientId).subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                            check->
//                        check?.result?: return@subscribe
                        loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.心电图}?.result=check?.result?:ElectroCardiogram()
                        val index=loadEmrResult.value?.result?.indexOfFirst { emr->emr.code==ActionRes.ActionType.心电图}
                        index?.let {
                            _loadEmrItemAction.value=Event(Pair(it,ActionRes.ActionType.心电图))
                        }
                    },{
                        messageAction.value= Event(it.message?:"")
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
            patientId=this@EmrViewModel.patientId
        }


        item?.let {
            emrApi.saveEcg(it,files).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread()).subscribe ({

                    emrApi.getEcgs(patientId).subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                                check->

                            loadEmrResult.value?.result?.first { emr->emr.code==ActionRes.ActionType.心电图}?.result=check?.result?:ElectroCardiogram()
                            val index=loadEmrResult.value?.result?.indexOfFirst { emr->emr.code==ActionRes.ActionType.心电图}
                            index?.let { index ->
                                _loadEmrItemAction.value = Event(Pair(index,ActionRes.ActionType.心电图))
                            }
                        },{
                            messageAction.value= Event(it.message?:"")
                        })

                },{
                    throwable->

                    it.savable=true
                    messageAction.value= Event(throwable.message?:"")
                })

        }
    }
}
