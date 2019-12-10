package com.wxsoft.fcare.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectSignDoctorViewModel @Inject constructor(
    private val doctorApi: TaskApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage, gon) {

    var filterString: String?= null
    set(value) {
        field =value
        if(field.isNullOrEmpty()){
            filterDoctors.value = loadConsultantDoctors.value?.result?: emptyList()
        }else {
            field?.let {
                f->
                filterDoctors.value =loadConsultantDoctors.value?.result?.filter { it.trueName.contains(f) }
            }

        }
    }
    val doctors: LiveData<List<User>>

    private val loadConsultantDoctors = MediatorLiveData<Response<List<User>>>()
    private val filterDoctors = MediatorLiveData<List<User>>()

    val selected = MediatorLiveData<Int>()

    init {
        doctors = filterDoctors.map { it }
        loadDocs()
    }

    private fun loadDocs() {
        fun doDoctor2(response: Response<List<User>>) {
            loadConsultantDoctors.value = response
            filterString=filterString
        }

        disposable.add(
            doctorApi.getSignUsers(account.id,"215-2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doDoctor2, ::error))
    }

}