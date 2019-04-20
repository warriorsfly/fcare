package com.wxsoft.fcare.ui.share

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ShareApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareViewModel  @Inject constructor(private val api: ShareApi,

                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){
    val uploading:LiveData<Boolean>
    val loadUploading = MediatorLiveData<Boolean>()

    val imageUrl=ObservableField<String>()
    val url:LiveData<String>
    private val loadUrlResult= MediatorLiveData<Response<String>>()

    init {
        uploading = loadUploading.map { it }
        url = loadUrlResult.map { it.result?:""}
        loadUploading.value = true
    }

    private fun doUrl(response: Response<String>){
        loadUrlResult.value=response
    }
    fun getImageUrl(patientId:String, uri:List<String>){
        api.getImageUrl(patientId,uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doUrl,::error)
    }

}