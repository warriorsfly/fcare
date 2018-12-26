package com.wxsoft.fcare.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.BR


data class VitalSign(val id:String): BaseObservable() {

    @Bindable
    var consciousness_Type: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.consciousness_Type)
        }
    @Bindable
    var respiration_Rate: Int = 0
        set(value) {

            field = value
            notifyPropertyChanged(BR.respiration_Rate)
        }
    @Bindable
    var pulse_Rate: Int = 0
        set(value) {

            field = value
            notifyPropertyChanged(BR.pulse_Rate)
        }
    @Bindable
    var body_Temperature: Float = 0f
        set(value) {

            field = value
            notifyPropertyChanged(BR.body_Temperature)
        }
    @Bindable
    var heart_Rate: Int = 0
        set(value) {

            field = value
            notifyPropertyChanged(BR.heart_Rate)
        }
    @Bindable
    var blood_Pressure: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.blood_Pressure)
        }
    @Bindable
    var killip_Level: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.killip_Level)
        }
    @Bindable
    var killip_Unit: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.killip_Unit)
        }
    /**
     * 病人id
     */
    @Bindable
    var patientId: String = ""
        set(value) {

            field = value
            notifyPropertyChanged(BR.patientId)
        }

    override fun toString(): String {

        return  "$consciousness_Type,呼吸$respiration_Rate,脉搏$pulse_Rate,心率$heart_Rate,血压$blood_Pressure,Killip等级$killip_Level"

    }
}