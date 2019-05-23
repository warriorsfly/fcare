package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR
import com.wxsoft.fcare.core.data.entity.chest.Intervention

data class Cure (val id:String=""): BaseObservable(){
    //策略
    @Bindable
    var treatStrategy :Strategy = Strategy("",0)
        set(value) {
            field = value
            notifyPropertyChanged(BR.treatStrategy)
        }
    //pci
    @Bindable
    var intervention :Intervention = Intervention("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.intervention)
        }
    //溶栓
    @Bindable
    var throm :Thrombolysis = Thrombolysis("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.throm)
        }
    //CABG
    @Bindable
    var cabg :CABG = CABG("")
        set(value) {
            field = value
            notifyPropertyChanged(BR.cabg)
        }


}