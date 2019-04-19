package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Strategy (val id:String=""): BaseObservable() {
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }
    @Bindable
    var strategyCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.strategyCode)
        }
    @Bindable
    var strategyCode_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.strategyCode_Name)
        }
    @Bindable
    var selectiveOrTransportTime: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectiveOrTransportTime)
        }
    @Bindable
    var otherTreatmentMeasure: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.otherTreatmentMeasure)
        }
    @Bindable
    var noReperfusionCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.noReperfusionCode)
        }
    @Bindable
    var intensify_Statins_Treat: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.intensify_Statins_Treat)
        }
    @Bindable
    var receptor_Retardant_Using: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.receptor_Retardant_Using)
        }
    @Bindable
    var preoperative_Timi_Level: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.preoperative_Timi_Level)
        }
    @Bindable
    var postoperative_Timi_Level: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.postoperative_Timi_Level)
        }
    @SerializedName("noReperfusionCode_Name")
    @Bindable
    var noReperfusionCodeName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.noReperfusionCodeName)
        }
    @Bindable
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

    @Bindable
    @Transient
    var memo: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.memo)
        }



    override fun toString(): String {
        return StringBuilder().append(strategyCode_Name).toString()
    }
}