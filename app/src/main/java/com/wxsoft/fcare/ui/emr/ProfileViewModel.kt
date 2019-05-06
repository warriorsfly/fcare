package com.wxsoft.fcare.ui.emr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.EmrApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val api: EmrApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {
    /**
     * 病历数据集
     */
    val emrs: LiveData<List<EmrRecord>>
    /**
     * 获取病历结果集
     */
    private val loadEmrResult = MediatorLiveData<Response<List<EmrRecord>>>()

    init {
        emrs = loadEmrResult.map { it.result ?.apply {
            forEach {
                it.patientId=patientId
                if(it.currUserId==null)
                    it.currUserId=account.id
            }
        }?: emptyList() }
    }

//    val bitmaps= mutableListOf<String>()

    /**
     * 病人ID
     */
    var patientId = ""
        set(value) {
           field=value
            loadEmrs()
        }

    /**
     * 获取emr列表
     */
    fun loadEmrs() {

        fun loadEmrDetails(response:  Response<List<EmrRecord>>) {
            loadEmrResult.value=response
        }
        disposable.add( api.getEmrImages(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::loadEmrDetails, ::error))
    }

    private fun reloadEmrDetails(response: Response<String>?) {
        if(response?.success==true)
            loadEmrs()
    }

    fun savingRecord(record:EmrRecord,fs:List<File>){

        val files = fs.map {
            return@map MultipartBody.Part.createFormData(
                "images",
                it.path.split("/").last(),
                RequestBody.create(MediaType.parse("multipart/form-data"), it)
            )
        }


        disposable.add( api.savingImages(record,files)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::reloadEmrDetails, ::error))

    }



    fun delete(url:String){
        var itemId=""
        var typeId=""
        emrs.value?.let{
            for ( t in it){
                for(item in t.items){
                    if(item.httpUrl==url){
                        itemId=item.id
                        typeId=t.typeId
                        disposable.add( api.deleteImage(typeId,itemId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(::reloadEmrDetails, ::error))

                        return@let
                    }
                }
            }
        }
    }
}
