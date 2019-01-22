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