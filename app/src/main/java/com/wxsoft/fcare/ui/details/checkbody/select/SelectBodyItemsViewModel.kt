package com.wxsoft.fcare.ui.details.checkbody.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.CheckBodyApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class SelectBodyItemsViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                   private val checkBodyApi: CheckBodyApi,
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
        }

    var typeId: String = ""
        set(value) {
            field = value
            loadItems(value)
        }
    var selectedId1: String = ""
        set(value) {
            field = value
        }
    var selectedId2: String = ""
        set(value) {
            field = value
        }

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

    val backToLast: LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    val title1: LiveData<String>
    val initTitle1 = MediatorLiveData<String>()
    val title2: LiveData<String>
    val initTitle2 = MediatorLiveData<String>()

    val submit: LiveData<String>
    val initSubmit = MediatorLiveData<String>()



    init {
        title1 = initTitle1.map { it }
        title2 = initTitle2.map { it }
        submit = initSubmit.map { it }
        backToLast = initbackToLast.map { it }
        coordinationItems = loadCoordinationItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        skinItems = loadSkinItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        leftPupilsItems = loadLeftPupilsItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        leftResponseLightItems = loadLeftResponseLightItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        rightPupilsItems = loadRightPupilsItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        rightResponseLightItems = loadRightResponseLightItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
    }

    fun loadItems(ids:String){

        when(ids){
            "1" -> {
                loadCoordinationItems()
                initTitle1.value = "检查过程"
            }
            "2" ->  {
                loadSkinItems()
                initTitle1.value = "皮肤"
            }
            "3" ->  {
                leftPupilsItems()
                loadLeftResponseLightItems()
                initTitle1.value = "外观"
                initTitle2.value = "对光反应"
            }
            "4" ->{
                rightPupilsItems()
                loadRightResponseLightItems()
                initTitle1.value = "外观"
                initTitle2.value = "对光反应"
            }
        }

    }


    private fun loadCoordinationItems() {
        dicEnumApi.loadCoordinationItems().toResource()
            .subscribe{
                loadCoordinationItemsResult.value = it
                if (selectedId1.isNullOrEmpty()){
                    coordinationItems.value?.first()?.checked = true
                }else{
                    coordinationItems.value?.filter {item-> item.checked }?.map {item-> item.checked = false }
                    coordinationItems.value?.filter {item-> item.id == selectedId1 }?.map { item->item.checked = true }
                }
            }
    }

    private fun loadSkinItems() {
        dicEnumApi.loadSkinItems().toResource()
            .subscribe{
                loadSkinItemsResult.value = it
                if (selectedId1.isNullOrEmpty()){
                    skinItems.value?.first()?.checked = true
                }else{
                    skinItems.value?.filter { it.checked }?.map { it.checked = false }
                    skinItems.value?.filter { it.id == selectedId1 }?.map {it.checked = true }
                }
            }
    }

    private fun leftPupilsItems() {
        dicEnumApi.loadPupilsItems().toResource()
            .subscribe{
                loadLeftPupilsItemsResult.value = it
                if (selectedId1.isNullOrEmpty()){
                    leftPupilsItems.value?.first()?.checked = true
                }else{
                    leftPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
                    leftPupilsItems.value?.filter { it.id == selectedId1 }?.map {it.checked = true }
                }
            }
    }
    private fun rightPupilsItems() {
        dicEnumApi.loadPupilsItems().toResource()
            .subscribe{
                loadRightPupilsItemsResult.value = it
                if (selectedId1.isNullOrEmpty()){
                    rightPupilsItems.value?.first()?.checked = true
                }else{
                    rightPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
                    rightPupilsItems.value?.filter { it.id == selectedId1 }?.map {it.checked = true }
                }
            }
    }

    private fun loadLeftResponseLightItems() {
        dicEnumApi.loadResponseLightItems().toResource()
            .subscribe{
                loadLeftResponseLightItemsResult.value = it
                if (selectedId2.isNullOrEmpty()){
                    leftResponseLightItems.value?.first()?.checked = true
                }else{
                    leftResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
                    leftResponseLightItems.value?.filter { it.id == selectedId2 }?.map {it.checked = true }
                }
            }
    }
    private fun loadRightResponseLightItems() {
        dicEnumApi.loadResponseLightItems().toResource()
            .subscribe{
                loadRightResponseLightItemsResult.value = it
                if (selectedId2.isNullOrEmpty()){
                    rightResponseLightItems.value?.first()?.checked = true
                }else{
                    rightResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
                    rightResponseLightItems.value?.filter { it.id == selectedId2 }?.map {it.checked = true }
                }
            }
    }

    fun selectItem(item:Dictionary){
        when(typeId){
            "1" ->{
                coordinationItems.value?.filter { it.checked }?.map { it.checked = false }
            }
            "2" ->{
                skinItems.value?.filter { it.checked }?.map { it.checked = false }
            }
            "3" ->{
                if (item.id.contains("208")) leftPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
                else leftResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
            }
            "4" ->{
                if (item.id.contains("208")) rightPupilsItems.value?.filter { it.checked }?.map { it.checked = false }
                else rightResponseLightItems.value?.filter { it.checked }?.map { it.checked = false }
            }
        }
        item.checked = true

        initSubmit.value = "success"
    }


}