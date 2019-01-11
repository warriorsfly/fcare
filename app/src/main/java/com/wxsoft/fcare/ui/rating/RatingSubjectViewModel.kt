package com.wxsoft.fcare.ui.rating

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.core.data.entity.rating.SubjectRecord
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

        rating=loadRatingResult.map { it ?: Rating() }
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

    private fun checkSavable():Boolean{

        if (rating.value==null) return false
        val errorOne=rating.value?.subjects?.firstOrNull { it.options.size>1 && (it.selectedIndex?:-1)==-1 }

        errorOne?.let {
            messageAction.value=Event(it.name+"未选择")
            return false
        }
        return true
    }

    fun saveRecord(){
        if(checkSavable()){
            //前面对rating.value进行判空
            val ratingRecord=RatingRecord(patientId = patientId,ratingId = rating.value!!.id,ratingName = rating.value!!.name,score = rating.value!!.score)
            ratingRecord.records=rating.value?.subjects?.filter { it.selectedIndex?:-1 >= 0 }?.map {
                SubjectRecord(patientId=patientId,recordId = "",subjectId = it.id,selection = it.options[it.selectedIndex?:-1].id,score = it.options[it.selectedIndex?:-1].score)
            }!!

            ratingApi.saveRatingResult(ratingRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {messageAction.value= Event("保存成功") },
                    { messageAction.value=Event(it.message?:"")})
        }
    }

}