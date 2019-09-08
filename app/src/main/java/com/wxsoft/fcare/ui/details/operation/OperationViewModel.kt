package com.wxsoft.fcare.ui.details.operation

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Operation
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OperationViewModel @Inject constructor(private val interventionApi: InterventionApi,
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
            getOperation()
        }

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    var num1= ObservableField<String>()
    var num2= ObservableField<String>()

    val operation: LiveData<Operation>
    private val loadOperation = MediatorLiveData<Operation>()

    init {
        backToLast = initbackToLast.map { it }
        operation = loadOperation.map { it?: Operation("").apply {
            patientId = this@OperationViewModel.patientId
            createrId = account.id
        } }
    }



    fun seletedNum1(id:String){
        num1.set(id)
        operation.value?.preoperative_Timi_Level = id.toInt()
    }
    fun seletedNum2(id:String){
        num2.set(id)
        operation.value?.postoperative_Timi_Level = id.toInt()
    }


    fun getOperation(){
        disposable.add(interventionApi.getOperation(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun getData(response: Response<Operation>){
        loadOperation.value = response.result
        num1.set(response.result?.preoperative_Timi_Level.toString())
        num2.set(response.result?.postoperative_Timi_Level.toString())
    }

    fun save(){
        disposable.add(interventionApi.saveOperation(operation.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }

    private fun saveResult(response: Response<String>){
        if (response.success) initbackToLast.value = true
    }

}