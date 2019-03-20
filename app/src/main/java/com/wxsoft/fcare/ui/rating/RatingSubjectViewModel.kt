package com.wxsoft.fcare.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.core.data.entity.rating.SubjectRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.RatingApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RatingSubjectViewModel @Inject constructor(
    private val ratingApi: RatingApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    var patientId=""
    var scenceType=""

    var ratingId=""
        set(value) {
            field=value
            if(value.isEmpty())return
            loadRating()
        }

    var recordId=""
        set(value) {
            field=value
            if(value.isEmpty())return
            loadRecord()
        }


    val rating:LiveData<Rating>
    private val loadRatingResult =MediatorLiveData<Rating>()
    private val loadRecordResult =MediatorLiveData<RatingRecord>()

    init {

        rating=loadRatingResult.map { it ?: Rating() }
    }

    private fun loadRecord(){

        disposable.add(ratingApi.getOneRecord(recordId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doRecord,::error))

    }

    private fun loadRating(){
            disposable.add(ratingApi.getOne(ratingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doSubjects,::error))

    }
    private fun checkSavable():Boolean{

        if (rating.value==null) return false
        val errorOne=rating.value?.subjects?.firstOrNull { it.options.size>1 && it.options.none {op-> op.checked } }

        errorOne?.let {
            messageAction.value=Event(it.name+"未选择")
            return false
        }
        return true
    }

    private fun doSubjects(response: Response<Rating>) {

        loadRecordResult.value?.records?.forEach {

            val selectedOptions = it.selection.split(",")


            val ops=response.result?.subjects?.
                first { sub -> sub.id == it.subjectId }?.
                options

            selectedOptions.forEach {
                id->
                ops?.firstOrNull { op->op.id==id }?.checked=true
            }

        }

        response.result?.refreshScore()
        loadRatingResult.value = response.result
    }

    private fun doRecord(response: Response<RatingRecord>){
        loadRecordResult.value=response.result

        ratingId=response.result?.ratingId?:""
    }

    fun deleteRecord(){
        if(recordId.isNotEmpty()){
            disposable.add(ratingApi.delete(recordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ messageAction.value = Event("删除成功") },
                    ::error))
        }
    }

    fun saveRecord(){
        if(checkSavable()) {
            //前面对rating.value进行判空
            val ratingRecord = if(recordId.isEmpty()) RatingRecord(
                id = recordId,
                createrId = account.id,
                patientId = patientId,
                sceneType = scenceType,
                ratingId = rating.value!!.id,
                ratingName = rating.value!!.name,
                score = rating.value!!.score,
                createdDate = loadRecordResult.value?.createdDate
            ) else loadRecordResult.value!!.apply { score = rating.value!!.score}
            ratingRecord.records = rating.value?.subjects?.map {
                SubjectRecord(
                    recordId = recordId,
                    subjectId = it.id,
                    selection = it.options.filter { op -> op.checked }.joinToString(separator = ","){ o -> o.id },
                    score = it.options.filter { op->op.checked }.sumBy { op->op.score }
                )
            }!!

            disposable.add(ratingApi.saveRatingResult(ratingRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ messageAction.value = Event("保存成功") },
                    ::error)
            )
        }
    }

}