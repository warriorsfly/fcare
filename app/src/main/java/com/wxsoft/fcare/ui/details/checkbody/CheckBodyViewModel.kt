package com.wxsoft.fcare.ui.details.checkbody

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.CheckBody
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.CheckBodyApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class CheckBodyViewModel @Inject constructor(private val checkBodyApi:CheckBodyApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,ICommonPresenter {
    override fun click() {
        submit()
    }

    override var title= "查体"
    override val clickableTitle: String
        get() = "保存"
    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    init {
        backToLast = initbackToLast.map { it }
        clickable=clickResult.map { it }
    }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadCheckBody()
        }

    val checkBody:LiveData<CheckBody>
    private val loadCheckBodyResult = MediatorLiveData<Resource<Response<CheckBody>>>()

    val selectItem:LiveData<String>
    private val loadselectItem = MediatorLiveData<String>()


    init {
        checkBody = loadCheckBodyResult.map { (it as? Resource.Success)?.data?.result ?: CheckBody("")  }
        selectItem = loadselectItem.map { it }

    }

    fun loadCheckBody(){
        checkBodyApi.loadCheckBody(patientId).toResource()
            .subscribe{
                loadCheckBodyResult.value = it
            }
    }
    private fun saveCheckBody(){
        checkBodyApi.save(checkBody.value!!).toResource()
            .subscribe {
                initbackToLast.value = true
            }
    }

//    private fun haveData(){
//        if (checkBody.value?.id.isNullOrEmpty()){
//            coordinationItems.value?.first()?.checked = true
//            skinItems.value?.first()?.checked = true
//            leftPupilsItems.value?.first()?.checked = true
//            leftResponseLightItems.value?.first()?.checked = true
//            rightPupilsItems.value?.first()?.checked = true
//            rightResponseLightItems.value?.first()?.checked = true
//        }else{
//            coordinationItems.value?.filter { it.checked }?.map { it.checked = false }
//            skinItems.value?.filter { it.checked }?.map { it.checked = false }
//            leftPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
//            leftResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
//            rightPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
//            rightResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
//            coordinationItems.value?.filter { it.id == checkBody.value?.coordination }?.map {it.checked = true }
//            skinItems.value?.filter { it.id == checkBody.value?.skin }?.map {it.checked = true }
//            leftPupilsItems.value?.filter { it.id == this.checkBody.value?.leftPupils }?.map {it.checked = true }
//            leftResponseLightItems.value?.filter { it.id == checkBody.value?.leftResponseLight }?.map {it.checked = true }
//            rightPupilsItems.value?.filter { it.id == checkBody.value?.rightPupils }?.map {it.checked = true }
//            rightResponseLightItems.value?.filter { it.id == checkBody.value?.rightResponseLight }?.map {it.checked = true }
//        }
//
//    }

//    fun clickSelect(item:Dictionary){
//        when(item.section){
//            0->{ coordinationItems.value?.filter { it.checked }?.map {it.checked = false } }
//            1->{ skinItems.value?.filter { it.checked }?.map {it.checked = false } }
//            2->{ leftPupilsItems.value?.filter { it.checked }?.map {it.checked = false } }
//            3->{ leftResponseLightItems.value?.filter { it.checked }?.map {it.checked = false } }
//            4->{ rightPupilsItems.value?.filter { it.checked }?.map {it.checked = false } }
//            5->{ rightResponseLightItems.value?.filter { it.checked }?.map {it.checked = false } }
//        }
//        item.checked = !item.checked
//    }

    fun submit(){

//
//        skinItems.value?.filter { it.checked }
//            ?.map { checkBody.value?.skin = it.id }
//
//        leftPupilsItems.value?.filter { it.checked }
//            ?.map { checkBody.value?.leftPupils = it.id }
//
//        leftResponseLightItems.value?.filter { it.checked }
//            ?.map { checkBody.value?.leftResponseLight = it.id }
//
//        rightPupilsItems.value?.filter { it.checked }
//            ?.map { checkBody.value?.rightPupils = it.id }
//
//        rightResponseLightItems.value?.filter { it.checked }
//            ?.map { checkBody.value?.rightResponseLight = it.id }

        checkBody.value?.patientId = patientId

        saveCheckBody()
    }


    fun select(num:String){
        loadselectItem.value = num
    }


}