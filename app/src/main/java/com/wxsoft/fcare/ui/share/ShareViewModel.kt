package com.wxsoft.fcare.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatActionListener
import cn.jiguang.share.android.api.Platform
import cn.jiguang.share.android.api.ShareParams
import cn.jiguang.share.wechat.Wechat
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareViewModel  @Inject constructor(private val api: DischargeApi,
                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "分享预览"
    override val clickableTitle: String
        get() = "分享"
    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
    val clickShare:LiveData<String>
    private val initClickShare = MediatorLiveData<String>()

    val selectUrl:LiveData<String>
    private val initSelectUrl = MediatorLiveData<String>()

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

    val urls:LiveData<List<String>>
    private val initUrls = MediatorLiveData<List<String>>()

    init {
        clickable = clickResult.map { it }
        clickShare = initClickShare.map { it }
        selectUrl= initSelectUrl.map { it }
        urls = initUrls.map { it }
    }

    override fun click() {
        if (selectUrl.value != null) initClickShare.value = "share"
    }

    private fun loadShareImg(){
        disposable.add(api.getShareImg(patientId,typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getImg,::error))
    }

    private fun getImg(response: Response<List<String>>){
        initUrls.value = response.result
    }

    fun selectImage(uri:String){
        initSelectUrl.value = uri
    }

}