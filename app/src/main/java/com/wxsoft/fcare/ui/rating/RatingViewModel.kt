package com.wxsoft.fcare.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.RatingApi
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RatingViewModel @Inject constructor(
    private val ratingApi: RatingApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {
    override var title = "评分"
    override val clickableTitle: String
        get() = ""


    var scenceId:String=""
    var patientId=""
    set(value) {
        field=value
        loadScenceRating()
        loadRating()
    }

    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=false
    }

    /**
     * 场景化数据
     */
    val scenceRatings:LiveData<List<ScencelyRatingResult>>
    private val loadRatingResult =MediatorLiveData<List<ScencelyRatingResult>>()

    init {

        clickable=clickResult.map { it }
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
            .subscribe(::doScenceLoadRating))
    }

    private fun doScenceLoadRating(response: Response<List<ScencelyRatingResult>>){

        loadRatingResult.value=response.result
    }

    private fun loadRating(){
        disposable.add(ratingApi.getRatings(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doLoadRating))
    }

    private fun doLoadRating(response: Response<List<Rating>>){

        loadResult.value=response.result
    }

    override fun click(){

    }
}