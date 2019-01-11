package com.wxsoft.fcare.ui.details.checkbody

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
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
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class CheckBodyViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                             private val checkBodyApi:CheckBodyApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {
    override fun click() {

    }

    override val title: String
        get() = "体格检查"
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
        }

    val checkBody:LiveData<CheckBody>
    private val loadCheckBodyResult = MediatorLiveData<Resource<Response<CheckBody>>>()

    val coordinationItems: LiveData<List<Dictionary>>
    private val loadCoordinationItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val skinItems: LiveData<List<Dictionary>>
    private val loadSkinItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val leftPupilsItems: LiveData<List<Dictionary>>
    private val loadLeftPupilsItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val leftResponseLightItems: LiveData<List<Dictionary>>
    private val loadLeftResponseLightItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val rightPupilsItems: LiveData<List<Dictionary>>
    private val loadRightPupilsItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val rightResponseLightItems: LiveData<List<Dictionary>>
    private val loadRightResponseLightItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {
        checkBody = loadCheckBodyResult.map { (it as? Resource.Success)?.data?.result ?: CheckBody("")  }
        coordinationItems = loadCoordinationItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        skinItems = loadSkinItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        leftPupilsItems = loadLeftPupilsItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        leftResponseLightItems = loadLeftResponseLightItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        rightPupilsItems = loadRightPupilsItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        rightResponseLightItems = loadRightResponseLightItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }

    }

    fun loadItems(){
        loadCoordinationItems()
        loadSkinItems()
        leftPupilsItems()
        rightPupilsItems()
        loadLeftResponseLightItems()
        loadRightResponseLightItems()
    }


    fun loadCoordinationItems() {
        dicEnumApi.loadCoordinationItems().toResource()
            .subscribe{
                loadCoordinationItemsResult.value = it
                coordinationItems.value?.filter { it.itemCode.equals(checkBody.value?.coordination) }?.map {it.checked = true }
            }
    }

    fun loadSkinItems() {
        dicEnumApi.loadSkinItems().toResource()
            .subscribe{
                loadSkinItemsResult.value = it
                skinItems.value?.filter { it.itemCode.equals(checkBody.value?.skin) }?.map {it.checked = true }
            }
    }

    fun leftPupilsItems() {
        dicEnumApi.loadPupilsItems().toResource()
            .subscribe{
                loadLeftPupilsItemsResult.value = it
                leftPupilsItems.value?.filter { it.itemCode.equals(checkBody.value?.leftPupils)}?.map {it.checked = true }
            }
    }
    fun rightPupilsItems() {
        dicEnumApi.loadPupilsItems().toResource()
            .subscribe{
                loadRightPupilsItemsResult.value = it
                rightPupilsItems.value?.filter { it.itemCode.equals(checkBody.value?.rightPupils) }?.map {it.checked = true }
            }
    }

    fun loadLeftResponseLightItems() {
        dicEnumApi.loadResponseLightItems().toResource()
            .subscribe{
                loadLeftResponseLightItemsResult.value = it
                leftResponseLightItems.value?.filter { it.itemCode.equals(checkBody.value?.leftResponseLight) }?.map {it.checked = true }
            }
    }
    fun loadRightResponseLightItems() {
        dicEnumApi.loadResponseLightItems().toResource()
            .subscribe{
                loadRightResponseLightItemsResult.value = it
                rightResponseLightItems.value?.filter { it.itemCode.equals(checkBody.value?.rightResponseLight)}?.map {it.checked = true }
            }
    }

    fun loadCheckBody(){
        checkBodyApi.loadCheckBody(patientId).toResource()
            .subscribe{
                loadCheckBodyResult.value = it
                haveData()
            }
    }

    fun saveCheckBody(){
        checkBodyApi.save(checkBody.value!!).toResource()
            .subscribe {
                initbackToLast.value = true
            }
    }

    fun haveData(){
         coordinationItems.value?.filter { it.itemCode.equals(checkBody.value?.coordination) }?.map {it.checked = true }
         skinItems.value?.filter { it.itemCode.equals(checkBody.value?.skin) }?.map {it.checked = true }
         leftPupilsItems.value?.filter { it.itemCode.equals(checkBody.value?.leftPupils)}?.map {it.checked = true }
         leftResponseLightItems.value?.filter { it.itemCode.equals(checkBody.value?.leftResponseLight) }?.map {it.checked = true }
         rightPupilsItems.value?.filter { it.itemCode.equals(checkBody.value?.rightPupils) }?.map {it.checked = true }
         rightResponseLightItems.value?.filter { it.itemCode.equals(checkBody.value?.rightResponseLight)}?.map {it.checked = true }
    }

    fun clickSelect(item:Dictionary){
        when(item.section){
            0->{ coordinationItems.value?.filter { it.checked }?.map {it.checked = false } }
            1->{ skinItems.value?.filter { it.checked }?.map {it.checked = false } }
            2->{ leftPupilsItems.value?.filter { it.checked }?.map {it.checked = false } }
            3->{ leftResponseLightItems.value?.filter { it.checked }?.map {it.checked = false } }
            4->{ rightPupilsItems.value?.filter { it.checked }?.map {it.checked = false } }
            5->{ rightResponseLightItems.value?.filter { it.checked }?.map {it.checked = false } }
        }
        item.checked = !item.checked
    }

    fun submit(){
        coordinationItems.value?.filter { it.checked }
            ?.map { checkBody.value?.coordination = it.itemCode }

        skinItems.value?.filter { it.checked }
            ?.map { checkBody.value?.skin = it.itemCode }

        leftPupilsItems.value?.filter { it.checked }
            ?.map { checkBody.value?.leftPupils = it.itemCode }

        leftResponseLightItems.value?.filter { it.checked }
            ?.map { checkBody.value?.leftResponseLight = it.itemCode }

        rightPupilsItems.value?.filter { it.checked }
            ?.map { checkBody.value?.rightPupils = it.itemCode }

        rightResponseLightItems.value?.filter { it.checked }
            ?.map { checkBody.value?.rightResponseLight = it.itemCode }

        checkBody.value?.patientId = patientId

        saveCheckBody()
    }



}