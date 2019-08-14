package com.wxsoft.fcare.ui.patient.choice

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Tag
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChoiceWrisbandViewModel @Inject constructor(
    private val enumApi: DictEnumApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {

    var haveSelectedId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    val wrisbands: LiveData<List<Tag>>
    private val loadWrisbands = MediatorLiveData<List<Tag>>()

    val selectedId= ObservableField<String>()

    val unbindResult:LiveData<String>
    private val initUnbindResult = MediatorLiveData<String>()

    init {
        unbindResult = initUnbindResult.map { it?:"" }
        wrisbands = loadWrisbands.map { it?: emptyList() }
    }

    private fun getTags(response: Response<List<Tag>>){
        loadWrisbands.value = response.result
        haveData()
    }

    fun loadTags(){
        disposable.add(enumApi.loadTags(account.hospitalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getTags,::error))
    }


    fun clickSelectTag(item: Tag){
        wrisbands.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true

    }

    private fun haveData(){
        wrisbands.value?.filter { it.id.equals(haveSelectedId)}?.map { it.checked = true }
    }


    //解绑腕带

    fun clickUnbind(id:String){
        initUnbindResult.value = "delete"
    }

    fun deleteWrisband(id:String){
        disposable.add( enumApi.unbindTag(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::unbindTag, ::error))
    }

    private fun unbindTag(response: Response<String>?) {
        if(response?.success==true)
            messageAction.value= Event("已解绑腕带")
            selectedId.set("")
            initUnbindResult.value = "deleteSuccess"
    }

    fun unbandWrisband(id:String){
        disposable.add( enumApi.unbindTag(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::unbindTagResult, ::error))
    }

    private fun unbindTagResult(response: Response<String>?) {
        if(response?.success==true)
            initUnbindResult.value = "success"
    }

}