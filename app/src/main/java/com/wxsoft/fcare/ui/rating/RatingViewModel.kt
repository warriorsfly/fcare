package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.RatingApi
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RatingViewModel @Inject constructor(
    private val ratingApi: RatingApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {
    override val title: String
        get() = "评分"
    override val clickableTitle: String
        get() = ""

    var patientId=""

    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=false
    }

    val ratings:LiveData<List<Rating>>
    private val loadRatingResult =MediatorLiveData<List<Rating>>()

    init {

        clickable=clickResult.map { it }
        ratings=loadRatingResult.map { it ?: emptyList() }
        loadRating()
    }

    private fun loadRating(){
        ratingApi.getRatings().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it->
                loadRatingResult.value=it.result
            }
    }


    override fun click(){

    }
}