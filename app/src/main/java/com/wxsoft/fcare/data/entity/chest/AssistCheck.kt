package com.wxsoft.fcare.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.fcare.BR

data class AssistCheck(val id:String=""):BaseObservable() {

    @Bindable
    var sampling_Time	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.sampling_Time)
        }
    @Bindable
    var report_Time	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.report_Time)
        }
    @Bindable
    var ctni_Value	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.ctni_Value)
        }
    @Bindable
    var ctni_Unit	:String="1"
        set(value){
            field=value
            notifyPropertyChanged(BR.ctni_Unit)
        }
    @Transient
    @Bindable
    var select_ctni_Unit:Int=0
        set(value){
            field=value
            ctni_Unit = (select_ctni_Unit +1).toString()
            notifyPropertyChanged(BR.select_ctni_Unit)
        }
    @Bindable
    var ctni_Status	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.ctni_Status)
        }
    @Transient
    @Bindable
    var select_ctni_Status:Int=0
        set(value){
            field=value
            when(value){
                0->ctni_Status = ""
                1->ctni_Status = "阴性"
                2->ctni_Status = "阳性"
            }
            notifyPropertyChanged(BR.select_ctni_Status)
        }
    @Bindable
    var ctnt_Value	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.ctnt_Value)
        }
    @Bindable
    var ctnt_Unit	:String="1"
        set(value){
            field=value
            notifyPropertyChanged(BR.ctnt_Unit)
        }
    @Transient
    @Bindable
    var select_ctnt_Unit:Int=0
        set(value){
            field=value
            ctnt_Unit = (select_ctnt_Unit+1).toString()
            notifyPropertyChanged(BR.select_ctnt_Unit)
        }
    @Bindable
    var ctnt_Status	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.ctnt_Status)
        }
    @Transient
    @Bindable
    var select_ctnt_Status:Int=0
        set(value){
            field=value
            when(value){
                0->ctnt_Status = ""
                1->ctnt_Status = "阴性"
                2->ctnt_Status = "阳性"
            }
            notifyPropertyChanged(BR.select_ctnt_Status)
        }
    @Bindable
    var cr_Value:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.cr_Value)
        }
    @Bindable
    var cr_Unit	:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.cr_Unit)
        }
    @Transient
    @Bindable
    var select_cr_Unit	:Int=0
        set(value){
            field=value
            when(value){
                0->cr_Unit = "1"
                1->cr_Unit = "0"
            }
            notifyPropertyChanged(BR.select_cr_Unit)
        }

    var patientId	:String=""

    var createrId	:String=""
    var createrName	:String=""
    var modifierId	:String=""
    var modifierName	:String=""




    fun setUpChecked(){
        when(cr_Unit){
            "1"-> select_cr_Unit = 0
            "0"-> select_cr_Unit = 1
        }
        when(ctnt_Status){
            ""->select_ctnt_Status = 0
            "阴性"->select_ctnt_Status = 1
            "阳性"->select_ctnt_Status = 2
        }
        when(ctni_Status){
            ""->select_ctni_Status = 0
            "阴性"->select_ctni_Status = 1
            "阳性"->select_ctni_Status = 2
        }
        select_ctni_Unit = ctni_Unit.toInt()-1
        select_ctnt_Unit = ctnt_Unit.toInt()-1
    }

}