package com.wxsoft.fcare.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingResult
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.RatingApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named


class RatingViewModel @Inject constructor(
    private val ratingApi: RatingApi,
    @Named("ratingTint")
    private val tints:IntArray,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    var scenceId:String=""
    var patientId=""
    set(value) {
        field=value
        loadScenceRating()
        loadRating()
    }

    fun refresh(){
        if(patientId.isNotEmpty()){
            loadScenceRating()
        }
    }

    /**
     * 场景化数据
     */
    val scenceRatings:LiveData<List<RatingResult>>
    private val loadRatingResult =MediatorLiveData<List<RatingResult>>()

    init {
        scenceRatings=loadRatingResult.map { it ?: emptyList() }
    }

    /**
     * 评分列表
     */
    val ratings:LiveData<List<Rating>>
    private val loadResult =MediatorLiveData<List<Rating>>()

    init {
        ratings=loadResult.map { it ?: emptyList() }
    }

    private fun loadScenceRating(){
        disposable.add(ratingApi.getScencelyRatings(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doScenceLoadRating,::error))
    }

    private fun doScenceLoadRating(response: Response<List<RatingResult>>) {

        loadRatingResult.value = response.result
    }

    private fun loadRating(){
        disposable.add(ratingApi.getRatings(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doLoadRating))
    }

    private fun doLoadRating(response: Response<List<Rating>>){

        loadResult.value=response.result
    }
}