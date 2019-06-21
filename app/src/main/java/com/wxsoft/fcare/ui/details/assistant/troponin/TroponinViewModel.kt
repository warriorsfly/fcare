package com.wxsoft.fcare.ui.details.assistant.troponin

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class TroponinViewModel @Inject constructor(private val lisApi: LISApi,
                                            private var dictEnumApi:DictEnumApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {

    val lisCr:LiveData<LisCr>
    private val loadLisCrResult = MediatorLiveData<LisCr>()

    private var saveAble=true
    val bitmaps= mutableListOf<String>()
    val uploading:LiveData<Boolean>
    val savePatientResult =MediatorLiveData<Boolean>()

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    /**
     * 肌酐蛋白单位
     */

    val ctniIndex=ObservableInt()
    val ctntIndex=ObservableInt()
    val troponinUnitsItems: LiveData<List<String>>
    private lateinit var troponinUnits: List<Dictionary>
    /**
     *获取肌酐蛋白单位字典
     */
    private val loadTroponinDictEnumResult = MediatorLiveData<List<Dictionary>>()

    val clickEdit:LiveData<String>
    private val loadClickEdit = MediatorLiveData<String>()


    init {
        clickEdit = loadClickEdit.map { it }
        lisCr = loadLisCrResult.map { it?:LisCr("") }
        troponinUnitsItems= loadTroponinDictEnumResult.map {
            val cos=it?: emptyList()
            troponinUnits=cos
            return@map troponinUnits.map { item -> item.itemName }
        }
        uploading = savePatientResult.map { it }
//        loadTroponin()
    }

    fun getCrById(id:String) {
        if (id.isEmpty()) {
            loadLisCrResult.value = null
            return
        }
        disposable.add(

            dictEnumApi.loadTroponinUnit()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        loadTroponinDictEnumResult.value = it
                        disposable.add(
                            lisApi.getPoct(id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(::getPoct, ::error)
                        )

                    }, ::error)
        )

    }
    private fun getPoct(response:Response<LisCr>) {

        loadLisCrResult.value = response.result?.apply {
            setUpChecked()
            val indexCtniUnit=troponinUnits.indexOfFirst {
                it.id == ctniUnit
            }
            ctniIndex.set(if(indexCtniUnit<0)0 else indexCtniUnit)

            val indexCtntUnit=troponinUnits.indexOfFirst {
                it.id == ctntUnit
            }
            ctntIndex.set(if(indexCtntUnit<0)0 else indexCtntUnit)
        }
    }


    fun submit(fs:List<File>){


        if (lisCr.value == null) return
        if(lisCr.value?.samplingTime.isNullOrEmpty()){
            messageAction.value=Event("抽血时间不能为空")
            return
        }
        lisCr.value!!.patientId = patientId
        savePatientResult.value= true
        lisCr.value?.also {lisCr->
            if (saveAble) {
                saveAble = false
                val files = fs.map {
                    return@map MultipartBody.Part.createFormData(
                        "images",
                        it.path.split("/").last(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), it)
                    )
                }
                if (files.isNullOrEmpty()) {
                    lisApi.savePoct(lisCr.apply {
                        ctniUnit=troponinUnits[ctniIndex.get()].id
                        ctntUnit=troponinUnits[ctntIndex.get()].id
                    }
                    ).toResource().subscribe {
                        savePatientResult.value= false
                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                saveAble = true
                                loadClickEdit.value = "success"
                            }
                            is Resource.Error -> {
//                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                        }
                    }
                } else {
                    lisApi.savePoct(lisCr, files).toResource().subscribe {
                        savePatientResult.value= false
                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                saveAble = true
                                loadClickEdit.value = "success"
                            }
                            is Resource.Error -> {
//                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                        }
                    }
                }
            }
        }
    }

    fun clickTime(id:String){
        loadClickEdit.value = id
    }

    //从机器获取数据
//    fun loadJGDB(id:String){
//        disposable.add(hardwareApi.getJGDB(id)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(::getJGDB,::error))
//    }
//    private fun getJGDB(response: LepuDetection){
//        loadLisCrResult.value = LisCr("").apply {
//
//        }
//    }

}