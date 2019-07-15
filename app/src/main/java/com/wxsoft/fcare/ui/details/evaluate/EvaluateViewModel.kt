package com.wxsoft.fcare.ui.details.evaluate

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.EvaluateItem
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EvaluateViewModel @Inject constructor(private val api: VitalSignApi,
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

    val selectedItem=ObservableField<EvaluateItem>()

    val items:LiveData<List<EvaluateItem>>

    private val loadItemsResult=MediatorLiveData<Response<List<EvaluateItem>>>()
    val rating=MediatorLiveData<Triple<String,String,String>>()

    init {
        items = loadItemsResult.map {
            it.result?.apply {
                forEach { item->item.selectColumns() }
            } ?: emptyList()
        }
    }

    fun loadBloodPressures(){
        disposable.add(
            api.getEvaluates(patientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doBloodPressures,::error)
        )
    }

    private fun doBloodPressures(response: Response<List<EvaluateItem>>){
        loadItemsResult.value=response.apply {
            if(result?.isNotEmpty() == true){
                if(selectedItem.get()==null)
                    selectedItem.set(result?.get(0))
                else{
//                    selectedItem.set(result?.first { it.strTime==selectedItem.get()?.strTime })
                }
            }
        }
    }

    fun save(){
        selectedItem.get()?.let {

            if(it.sbp.isNullOrEmpty() || it.dbp.isNullOrEmpty() || it.heart_Rate==0){
                messageAction.value= Event("数据不完整")
                return
            }
            disposable.add(
                api.insert(it.apply {
                    if(it.createrId == null) {
                        it.createrId = account.id
                        it.createrName = account.trueName
                    }

                }).flatMap { api.getEvaluates(patientId) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doBloodPressures,::error)
            )
        }
    }
    fun select(item:EvaluateItem){
        selectedItem.set(item)
    }

    fun openRating(ratId:String,id:String?){
        val name =when(ratId){
            "9"->"NIHSS评分"
            "10"->"mRS评分"
            "24"->"BI评分"
            "25"->"吞咽测试"
            else->""
        }
        rating.value=Triple(ratId,name,id?:"-1")
    }
}