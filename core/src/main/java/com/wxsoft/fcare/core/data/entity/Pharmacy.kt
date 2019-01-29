package com.wxsoft.fcare.core.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import java.io.Serializable

data class Pharmacy (val id:String): BaseObservable(), Serializable {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var createdDate: String = "2019-01-28 14:27:00"
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

    @Bindable
    @Transient
    var drugBagChecked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugBagChecked)
        }

    @Bindable
    var drugRecordDetails: List<DrugRecord> = emptyList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.drugRecordDetails)
        }


}