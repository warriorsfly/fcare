package com.wxsoft.fcare.ui.statistics

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.statistics.Statist1
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.result.succeeded
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class StatisticsViewModel @Inject constructor(
                                              private val accountApi: AccountApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {

    val pie1s = arrayOf("绿通人数", "")

    val pie2s = arrayOf("溶栓人数","")
    val pie3s = arrayOf("取栓人数","")
    val pie4s = arrayOf("蛛网膜下腔出血","")


    val data: LiveData<Statist1>

    private val mData = MediatorLiveData<Statist1>()

    init {
        data = mData.map {
            it
        }

        mData.value = Statist1(2000, 1400,433,622,322)
    }
}