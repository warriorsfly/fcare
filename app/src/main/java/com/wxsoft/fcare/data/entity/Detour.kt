package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR

class Detour constructor(@Bindable var id:String=""): BaseObservable() {

    /**
     * 是否绕行急诊
     */
    @Transient
    @Bindable var hasDetour:Boolean=false
        set(value) {
            field=value
            if (value) has_Bypass_Emergency = "1" else has_Bypass_Emergency = "2"
            notifyPropertyChanged(BR.hasDetour)
        }


    /**
     * 是否绕行CUU
     */
    @Transient
    @Bindable var hasCCU:Boolean=false
        set(value) {
            field=value
            if (value) has_Bypass_Ccu = "1" else has_Bypass_Ccu = "2"
            notifyPropertyChanged(BR.hasCCU)
        }


    /**
     * 是否绕行急诊
     */
    @SerializedName("is_Bypass_Emergency")
    @Bindable var has_Bypass_Emergency:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Bypass_Emergency)
        }
    /**
     * 绕行到哪个地方 传代码
     */
    @Bindable var bypass_Emergency_Code:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.bypass_Emergency_Code)
        }

    @Bindable var bypass_Emergency_Leave_Time:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.bypass_Emergency_Leave_Time)
        }

    @Bindable var arrived_Emergency_Time:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.arrived_Emergency_Time)
        }

    @SerializedName("is_Bypass_Ccu")
    @Bindable var has_Bypass_Ccu:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.has_Bypass_Ccu)
        }
    @Bindable var arrived_Ccu_Date:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.arrived_Ccu_Date)
        }
    @Bindable var arrived_Other_Date:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.arrived_Other_Date)
        }
    @Bindable var patientId:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable var createrId:String = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.createrId)
        }


    fun setUpClick(){
        hasDetour = has_Bypass_Emergency.equals("1")
        hasCCU = has_Bypass_Ccu.equals("1")
    }

}