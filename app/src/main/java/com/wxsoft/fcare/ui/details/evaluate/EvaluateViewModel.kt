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
    val clickIndex=ObservableField<Int>()

    val items:LiveData<List<EvaluateItem>>

    var choseItem = EvaluateItem("")

    private val loadItemsResult=MediatorLiveData<Response<List<EvaluateItem>>>()
    val rating=MediatorLiveData<Triple<String,String,String>>()

    init {
        clickIndex.set(0)
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
                if(selectedItem.get()==null) {
                    selectedItem.set(result?.get(0))
                    choseItem.let {
                        it.id =  selectedItem.get()!!.id
                        it.name =  selectedItem.get()!!.name
                        it.consciousness_Type_Name =  selectedItem.get()!!.consciousness_Type_Name
                        it.consciousness_Type =  selectedItem.get()!!.consciousness_Type
                        it.dbp =  selectedItem.get()!!.dbp
                        it.sbp =  selectedItem.get()!!.sbp
                        it.heart_Rate =  selectedItem.get()!!.heart_Rate
                        it.respiration_Rate =  selectedItem.get()!!.respiration_Rate
                        it.spO2 =  selectedItem.get()!!.spO2
                        it.weight =  selectedItem.get()!!.weight
                        it.height =  selectedItem.get()!!.height
                        it.pulse_Rate =  selectedItem.get()!!.pulse_Rate
                        it.nihss =  selectedItem.get()!!.nihss
                        it.nihsS_AnswerRecordId =  selectedItem.get()!!.nihsS_AnswerRecordId
                        it.mrs =  selectedItem.get()!!.mrs
                        it.mrS_AnswerRecordId =  selectedItem.get()!!.mrS_AnswerRecordId
                        it.bi =  selectedItem.get()!!.bi
                        it.bI_AnswerRecordId =  selectedItem.get()!!.bI_AnswerRecordId
                        it.swallow =  selectedItem.get()!!.swallow
                        it.swallow_AnswerRecordId =  selectedItem.get()!!.swallow_AnswerRecordId
                        it.patientId =  selectedItem.get()!!.patientId
                        it.evaluatePlanId =  selectedItem.get()!!.evaluatePlanId
                        it.showColumn =  selectedItem.get()!!.showColumn
                        it.createdDate =  selectedItem.get()!!.createdDate
                        it.modifiedDate =  selectedItem.get()!!.modifiedDate
                        it.createrId =  selectedItem.get()!!.createrId
                        it.createrName =  selectedItem.get()!!.createrName
                        it.modifierId =  selectedItem.get()!!.modifierId
                        it.modifierName =  selectedItem.get()!!.modifierName
                        it.columns =  selectedItem.get()!!.columns
                    }
                } else{
//                    selectedItem.set(result?.first { it.strTime==selectedItem.get()?.strTime })
                    selectedItem.set(result?.get(clickIndex.get()!!))
                }
            }
        }
    }

    fun save(){
        selectedItem.get()?.let {
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
//            messageAction.value= Event("保存成功")
        }
    }
    fun select(item: EvaluateItem){
        checkUpDatas()
        val index = clickIndex.get()!!
        if (item?.id.isNullOrEmpty()&&(index > 0)) {
            var ii = items.value?.get(index-1)
            item.let {
                it.consciousness_Type_Name =  ii!!.consciousness_Type_Name
                it.consciousness_Type =  ii!!.consciousness_Type
                it.dbp =  ii!!.dbp
                it.sbp =  ii!!.sbp
                it.heart_Rate =  ii!!.heart_Rate
                it.respiration_Rate =  ii!!.respiration_Rate
                it.spO2 =  ii!!.spO2
                it.weight =  ii!!.weight
                it.height =  ii!!.height
            }
        }
        selectedItem.set(item)
        choseItem.let {
            it.id =  selectedItem.get()!!.id
            it.name =  selectedItem.get()!!.name
            it.consciousness_Type_Name =  selectedItem.get()!!.consciousness_Type_Name
            it.consciousness_Type =  selectedItem.get()!!.consciousness_Type
            it.dbp =  selectedItem.get()!!.dbp
            it.sbp =  selectedItem.get()!!.sbp
            it.heart_Rate =  selectedItem.get()!!.heart_Rate
            it.respiration_Rate =  selectedItem.get()!!.respiration_Rate
            it.spO2 =  selectedItem.get()!!.spO2
            it.weight =  selectedItem.get()!!.weight
            it.height =  selectedItem.get()!!.height
            it.pulse_Rate =  selectedItem.get()!!.pulse_Rate
            it.nihss =  selectedItem.get()!!.nihss
            it.nihsS_AnswerRecordId =  selectedItem.get()!!.nihsS_AnswerRecordId
            it.mrs =  selectedItem.get()!!.mrs
            it.mrS_AnswerRecordId =  selectedItem.get()!!.mrS_AnswerRecordId
            it.bi =  selectedItem.get()!!.bi
            it.bI_AnswerRecordId =  selectedItem.get()!!.bI_AnswerRecordId
            it.swallow =  selectedItem.get()!!.swallow
            it.swallow_AnswerRecordId =  selectedItem.get()!!.swallow_AnswerRecordId
            it.patientId =  selectedItem.get()!!.patientId
            it.evaluatePlanId =  selectedItem.get()!!.evaluatePlanId
            it.showColumn =  selectedItem.get()!!.showColumn
            it.createdDate =  selectedItem.get()!!.createdDate
            it.modifiedDate =  selectedItem.get()!!.modifiedDate
            it.createrId =  selectedItem.get()!!.createrId
            it.createrName =  selectedItem.get()!!.createrName
            it.modifierId =  selectedItem.get()!!.modifierId
            it.modifierName =  selectedItem.get()!!.modifierName
            it.columns =  selectedItem.get()!!.columns
        }
    }

    fun openRating(ratId:String,id:String?){
        val name =when(ratId){
            "9"->"NIHSS评分"
            "10"->"mRS评分"
            "25"->"BI评分"
            "21"->"吞咽测试"
            else->""
        }
        rating.value=Triple(ratId,name,id?:"-1")
    }

    private fun checkUpDatas(){
        selectedItem.get()?.let {
            if((!it.consciousness_Type.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))&&(!it.consciousness_Type.equals(choseItem.consciousness_Type))){
                save()
                return
            }
            if(((!it.sbp.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))&&(it.sbp!=choseItem.sbp))){
                save()
                return
            }
            if(((!it.dbp.isNullOrEmpty())&&(!it.evaluatePlanId.equals("9"))&&(it.dbp!=choseItem.dbp))){
                save()
                return
            }
            if((it.nihss != null && it.nihss!=0)&&(it.nihss!=choseItem.nihss)){
                save()
                return
            }
            if((it.mrs != null && it.mrs!=0)&&(it.mrs!=choseItem.mrs)){
                save()
                return
            }
            if((it.bi != null && it.bi!=0)&&(it.bi!=choseItem.bi)){
                save()
                return
            }
            if((it.swallow != null && it.swallow!=0)&&(it.swallow!=choseItem.swallow)){
                save()
                return
            }
            if(((it.heart_Rate!=0 && it.heart_Rate != null)&&(!it.evaluatePlanId.equals("9"))&&(it.respiration_Rate!=choseItem.respiration_Rate))){
                save()
                return
            }
            if(((it.respiration_Rate!=0 && it.respiration_Rate != null)&&(!it.evaluatePlanId.equals("9"))&&(it.respiration_Rate!=choseItem.respiration_Rate))){
                save()
                return
            }
            if(((it.height!=0 && it.height != null)&&(it.evaluatePlanId.equals("1"))&&(it.height!=choseItem.height))){
                save()
                return
            }
            if(((it.weight!=0 && it.weight != null)&&(it.evaluatePlanId.equals("1"))&&(it.weight!=choseItem.weight))){
                save()
                return
            }
            if(((it.spO2!=0 && it.spO2 != null)&&(!it.evaluatePlanId.equals("9"))&&(it.spO2!=choseItem.spO2))){
                save()
                return
            }
        }
    }

}