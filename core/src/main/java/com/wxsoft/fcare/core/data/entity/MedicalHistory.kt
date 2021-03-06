package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.previoushistory.History1
import com.wxsoft.fcare.core.data.entity.previoushistory.History2
import com.wxsoft.fcare.core.utils.DateTimeUtils

data class MedicalHistory(val createrId:String,val id:String=""): BaseObservable() {

    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var provide:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.provide)
        }
    @Bindable
    @SerializedName("provide_Name")
    var provideName:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.provideName)
        }

    @Bindable
    var cc:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.cc)
        }

    @Bindable
    var hpi:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.hpi)
        }

    @Bindable
    var ph:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.ph)
        }

    @Bindable
    @SerializedName("pH_Name")
    var phName:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.phName)
        }

    //过敏史
    @Bindable
    var ah:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.ah)
        }

    @Bindable
    var attachments:List<Attachment>  = emptyList()
        set(value) {
            field=value
            notifyPropertyChanged(BR.attachments)
        }
    @Bindable
    var createdDate:String= DateTimeUtils.getCurrentTime()
        set(value) {
            field=value
            notifyPropertyChanged(BR.createdDate)
        }


    var drugHistorys:List<History2> = emptyList()
    var pastHistorys:List<History1> = emptyList()

    @Bindable
    @Transient
    var pastHistorysString:String= ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.pastHistorysString)
        }

    fun getPastHistorys(){
        var anamStr = ""
        if (pastHistorys.size>1){
            pastHistorys.map { anamStr = if(anamStr.isNullOrEmpty()) it.phCode_Name else anamStr +"、"+it.phCode_Name  }
        }else{
            pastHistorys.map { anamStr = it.phCode_Name }
        }
        pastHistorysString = anamStr
    }
}