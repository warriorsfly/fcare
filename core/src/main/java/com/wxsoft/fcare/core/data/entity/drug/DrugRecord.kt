package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import java.io.Serializable

data class DrugRecord (val id:String) : BaseObservable() , Serializable {


//    @Bindable
//    @Transient
//    var checked = false
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.checked)
//        }
    var actionCode:String = ""
    @Bindable
    var excuteTime: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.excuteTime)
        }
    var staffId: String? = null
    @Bindable
    var staffName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.staffName)
        }

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var selected: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.selected)
        }

    @Bindable
    var drugId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugId)
        }

    @Bindable
    var drugName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugName)
        }

    @Bindable
    var dose: Float = 0f
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
        }

    @Transient
    @Bindable
    var doseString: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doseString)
            dose = if(field.isNullOrEmpty()) 0f else field?.toFloat()?:0f
        }

    @Bindable
    var doseUnit: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.doseUnit)
        }

    @Bindable
    var drugRecordId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugRecordId)
        }
    @Bindable
    var businessId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.businessId)
        }

    @Bindable
    var drugPackageId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugPackageId)
        }


    @Bindable
    var createdDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

    @Bindable
    var createrName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrName)
        }
}