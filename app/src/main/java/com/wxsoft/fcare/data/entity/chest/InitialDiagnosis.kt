package com.wxsoft.fcare.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.wxsoft.fcare.BR

data class InitialDiagnosis(val id:String):BaseObservable(){

    @Bindable
    var diagnosis_Code: String  = "0"
    set(value) {
        field=value
        notifyPropertyChanged(BR.diagnosis_Code)
    }
    @Bindable
    var diagnosis_Time: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.diagnosis_Time)
        }
    @Bindable
    var diagnosis_Doctor_Name: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.diagnosis_Doctor_Name)
        }
    @Bindable
    var handle_Way: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.handle_Way)
        }
    @Bindable
    var handle_Time: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.handle_Time)
        }
    @Bindable
    var patient_Outcome: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patient_Outcome)
        }
    @Bindable
    var doctor_Name: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.doctor_Name)
        }
    @Bindable
    var patientId: String  = ""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var createrName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrName)
        }

    @Bindable
    var createrId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrId)
        }

    @Bindable
    @Expose
    @Transient()
    var createdDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    @Bindable
    var modifierName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierName)
        }


    @Bindable
    var initialDiagnosisDetails:List<InitialDiagnosisDetail> =emptyList()
        set(value) {

            field=value
            notifyPropertyChanged(BR.initialDiagnosisDetails)
        }

    @Bindable
    var modifierId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierId)
        }

    @Bindable
    var modifiedDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifiedDate)
        }
}