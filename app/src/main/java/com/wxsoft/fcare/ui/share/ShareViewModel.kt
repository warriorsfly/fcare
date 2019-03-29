package com.wxsoft.fcare.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareViewModel  @Inject constructor(private val api: DischargeApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    val clickShare:LiveData<String>
    private val initClickShare = MediatorLiveData<String>()

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    /**
     * typeID
     */
    var typeId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadShareImg()
        }

    val url:LiveData<List<String>>
    private val initUrl = MediatorLiveData<List<String>>()

    init {
        clickShare = initClickShare.map { it }
        url = initUrl.map { it }
    }

    fun click() {
        initClickShare.value = "share"
    }

    private fun loadShareImg(){
        disposable.add(api.getShareImg(patientId,typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getImg,::error))
    }

    private fun getImg(response: Response<List<String>>){
        initUrl.value = response.result
    }


}