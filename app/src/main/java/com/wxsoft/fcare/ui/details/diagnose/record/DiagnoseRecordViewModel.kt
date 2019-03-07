package com.wxsoft.fcare.ui.details.diagnose.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.DiagnoseRecord
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class DiagnoseRecordViewModel @Inject constructor(private val diagnoseApi: DiagnoseApi,
                                                  @Named("ratingTint")
                                                  private val tints:IntArray,
                                                  override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                  override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title="诊断记录"
    override val clickableTitle: String
        get() = "确定"
    override val clickable: LiveData<Boolean>
    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getDiagnoseRecords()
        }

    val diagnoses:LiveData<List<DiagnoseRecord>>
    private val initDiagnoses = MediatorLiveData<List<DiagnoseRecord>>()

    val adddiagnose:LiveData<String>
    private val initAdddiagnose = MediatorLiveData<String>()

    val modifydiagnosis:LiveData<Diagnosis>
    private val initModifydiagnosisl = MediatorLiveData<Diagnosis>()

    init {
        clickable = clickResult.map { it }
        diagnoses = initDiagnoses.map { it?: emptyList() }
        adddiagnose = initAdddiagnose.map { it }
        modifydiagnosis = initModifydiagnosisl.map { it }
    }

    override fun click() {

    }



    fun add(item:DiagnoseRecord){
        initAdddiagnose.value = item.typeId
    }
    fun modify(item:Diagnosis){
        initModifydiagnosisl.value = item
    }

    fun getDiagnoseRecords(){

        disposable.add(diagnoseApi.getDiagnosisList(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doScenceLoadDiagnoseRecord))
    }

    private fun doScenceLoadDiagnoseRecord(response: Response<List<DiagnoseRecord>>){
        initDiagnoses.value=response.result?.apply {
            forEachIndexed { index, result ->
                result.tint = tints[(index + 1) % tints.size]
            }
        }
    }

}