package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class VitalSign(val id:String=""): BaseObservable() {

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
    var dbp: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dbp)
        }
    @Bindable
    var spO2: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.spO2)
        }
    @Bindable
    var sbp: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dbp)
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

    @Transient
    @Bindable
    var killip_Unit_int: Int = 0
        set(value) {
            field = value
            when(value){
                0->killip_Level = ""
                1->killip_Level = "1"
                2->killip_Level = "2"
                3->killip_Level = "3"
                4->killip_Level = "4"
            }
            notifyPropertyChanged(BR.killip_Unit_int)
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

    @Bindable
    var createdDate: String = "0001-01-01 00:00:00"
        set(value) {
            field = value
            notifyPropertyChanged(BR.createdDate)
        }

    override fun toString(): String {
        return  "$consciousness_Type,呼吸$respiration_Rate,脉搏$pulse_Rate,心率$heart_Rate"

    }

    fun setUpChecked(){
        when(killip_Level){
            ""-> killip_Unit_int = 0
            "1"-> killip_Unit_int = 1
            "2"-> killip_Unit_int = 2
            "3"-> killip_Unit_int = 3
            "4"-> killip_Unit_int = 4
        }
    }
}