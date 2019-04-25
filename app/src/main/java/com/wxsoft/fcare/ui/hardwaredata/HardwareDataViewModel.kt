package com.wxsoft.fcare.ui.hardwaredata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.hardware.MindrayDetection
import com.wxsoft.fcare.core.data.entity.hardware.MindrayDevices
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.HardwareApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HardwareDataViewModel @Inject constructor(private val hardwareApi: HardwareApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value

        }

    val mindrayDevices: LiveData<List<MindrayDevices>>
    private val loadMindrayDevicesResult = MediatorLiveData<List<MindrayDevices>>()

    val vital: LiveData<MindrayDetection>
    private val loadVitalResult = MediatorLiveData<MindrayDetection>()


    init {
        mindrayDevices = loadMindrayDevicesResult.map { it?: emptyList() }
        vital = loadVitalResult.map { it?:MindrayDetection("","","","","",0,0,0,0,"") }
        loadMindrayDevices()
    }

    fun loadMindrayDevices(){
        disposable.add(hardwareApi.getMindrayDevices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getMindrayDevices,::error))
    }
    private fun getMindrayDevices(response:List<MindrayDevices>){
        loadMindrayDevicesResult.value = response
    }

    fun loadVital(id:String){
        disposable.add(hardwareApi.getVital(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getVital,::error))
    }
    private fun getVital(response:MindrayDetection){
        loadVitalResult.value = response
    }

    fun selectDevice(device:MindrayDevices){
        mindrayDevices.value?.filter { it.checked }?.map { it.checked = false }
        device.checked = true
        loadVital(device.id)
    }

}