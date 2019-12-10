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

    val doctors: LiveData<List<User>>

    private val loadConsultantDoctors = MediatorLiveData<Response<List<User>>>()

    val selected = MediatorLiveData<Int>()

    init {
        doctors = loadConsultantDoctors.map { it.result ?: emptyList() }
        loadDocs()
    }

    private fun loadDocs() {
        fun doDoctor2(response: Response<List<User>>) {
            loadConsultantDoctors.value = response
        }

        disposable.add(
            doctorApi.getSignUsers(account.id,"215-2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doDoctor2, ::error))
    }

    fun change(flag: Int) {
        selected.value = flag
    }
}