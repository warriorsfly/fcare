package com.wxsoft.fcare.core.data.entity.drug

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.utils.DateTimeUtils
import java.io.Serializable

data class DrugRecord (val id:String) : BaseObservable() , Serializable {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
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
    var dose: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.dose)
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
    var createdDate: String = DateTimeUtils.getCurrentTime()
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


//    @Bindable
//    var drug: Drug = Drug("")
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.drug)
//        }
//
//    @Bindable
//    var drugPackage: DrugPackage = DrugPackage("")
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.drugPackage)
//        }


}