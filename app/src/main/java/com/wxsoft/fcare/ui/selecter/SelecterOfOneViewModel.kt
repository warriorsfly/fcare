package com.wxsoft.fcare.ui.selecter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.NotifyType
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelecterOfOneViewModel @Inject constructor(private val enumApi: DictEnumApi,
                                                 override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                 override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    var haveSelectedId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    var haveSelectedIds: List<String> = emptyList()
        set(value) {
            field = value
        }


    var typeId: String = ""
        set(value) {
            if (value == "") return
            field = value
            when(value){
                "Vital" -> {
                    loadVital()
                    clickAlone = true
                }
                "MedicalHistoryProvider" -> {
                    loadMedicalHistoryProvider()
                    clickAlone = true
                }
                "MedicalHistoryAnamnesis" -> {
                    loadMedicalHistoryAnamnesis()
                    clickAlone = false
                }
                "Notify" ->{
                    loadNotifyTypes()
                    clickAlone = true
                }
                "ThromSelectPlace" ->{
                    loadThromPlace()
                    clickAlone = true
                }
                "Treatment" ->{
                    loadTreatments()
                    clickAlone = true
                }
                "Adress" ->{
                    loadAdress()
                    clickAlone = true
                }
                "selectHandway" ->{
                    loadHandway()
                    clickAlone = true
                }
                "selectPatientOutcom" ->{
                    loadPatientOutcom()
                    clickAlone = true
                }
            }
        }

    var clickAlone:Boolean = false

    val des: LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()

    val notifyTypes: LiveData<List<NotifyType>>
    private val loadNotifyTypes = MediatorLiveData<List<NotifyType>>()

    val submit: LiveData<String>
    private val loadSubmit = MediatorLiveData<String>()

    init {
        des = loadDesResult.map { it ?: emptyList()  }
        notifyTypes = loadNotifyTypes.map { it?: emptyList() }
        submit = loadSubmit.map { it }
    }

    private fun loadPatientOutcom(){
        disposable.add(enumApi.loadPatientOutcom()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadHandway(){
        disposable.add(enumApi.loadHandway()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadAdress(){
        disposable.add(enumApi.loadloadAdress()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadVital(){
        disposable.add(enumApi.loadConsciousness()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadMedicalHistoryProvider(){
        disposable.add(enumApi.loadMedicalHistoryProviderItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadMedicalHistoryAnamnesis(){
        disposable.add(enumApi.loadMedicalHistoryItems(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun loadNotifyTypes(){
        disposable.add(enumApi.loadNotifyTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getNotify,::error))
    }
    private fun loadThromPlace(){
        disposable.add(enumApi.loadThromPlaces()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }
    private fun loadTreatments(){
        disposable.add(enumApi.loadNoRefushionResons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun getData(response: List<Dictionary>){
        loadDesResult.value = response
        haveData()
    }

    private fun getNotify(response: Response<List<NotifyType>>){
        loadNotifyTypes.value = response.result
    }
    fun clickSelect(item: Dictionary){
        if (clickAlone){
            des.value?.filter { it.checked }?.map { it.checked = false }
            item.checked = true
            loadSubmit.value = "success"
        }else{
            item.checked = !item.checked
        }
    }
    fun clickSelectNotify(item: NotifyType){
        notifyTypes.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
        loadSubmit.value = "success"
    }

    private fun haveData(){
        des.value?.filter { it.id.equals(haveSelectedId)}?.map { it.checked = true }
        des.value?.filter { haveSelectedIds.contains(it.id)}?.map { it.checked = true }
    }

}