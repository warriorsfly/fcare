package com.wxsoft.fcare.ui.details.blood.pressure

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.BloodPressureItem
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BloodPressureViewModel @Inject constructor(private val api: VitalSignApi,
                                               override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadBloodPressures()
        }

    val selectedItem=ObservableField<BloodPressureItem>()

    val items:LiveData<List<BloodPressureItem>>

    private val loadItemsResult=MediatorLiveData<Response<List<BloodPressureItem>>>()

    init {
        items = loadItemsResult.map { it.result?: emptyList() }
    }

    fun loadBloodPressures(){
        disposable.add(
            api.getVitalSignList(patientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doBloodPressures,::error)
        )
    }

    private fun doBloodPressures(response: Response<List<BloodPressureItem>>){
        loadItemsResult.value=response.apply {
            if(result?.isNotEmpty() == true){
                if(selectedItem.get()==null)
                    selectedItem.set(result?.get(0))
                else{
                    selectedItem.set(result?.first { it.strTime==selectedItem.get()?.strTime })
                }
            }
        }
    }

    fun save(){
        selectedItem.get()?.let {

            if(it.sbp.isNullOrEmpty() || it.dbp.isNullOrEmpty() || it.heartRate==0){
                messageAction.value= Event("数据不完整")
                return
            }
            disposable.add(
                api.insert(it.apply {
                    if(it.createrId == null) {
                        it.createrId = account.id
                        it.createrName = account.trueName
                    }

                }).flatMap { api.getVitalSignList(patientId) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doBloodPressures,::error)
            )
            messageAction.value= Event("保存成功")
        }
    }
    fun select(item:BloodPressureItem){
        selectedItem.set(item)
    }
}