package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.RatingApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RatingSubjectViewModel @Inject constructor(
    private val ratingApi: RatingApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    var patientId=""

    var ratingId=""
        set(value) {
            field=value
            if(value.isNullOrEmpty())return
            loadRating()
        }




    val rating:LiveData<Rating>
    private val loadRatingResult =MediatorLiveData<Rating>()

    init {

        rating=loadRatingResult.map { it ?: Rating("","","",0f) }
//        loadRating()
    }

    private fun loadRating(){
        ratingApi.getOne(ratingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                loadRatingResult.value=it.result
            },
                {
                    messageAction.value= Event(it.message?:"")
                }
        )
    }

}