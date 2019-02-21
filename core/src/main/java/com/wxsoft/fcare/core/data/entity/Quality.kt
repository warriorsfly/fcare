package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class Quality (val id:String): BaseObservable(){
    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }
    @Bindable
    var patientName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientName)
        }
    @Bindable
    var diagnosisCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisCode)
        }
    @Bindable
    var diagnosisName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.diagnosisName)
        }
    @Bindable
    var dnt: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dnt)
        }
    @Bindable
    var ont: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.ont)
        }
    @Bindable
    var odt: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.odt)
        }
    @Bindable
    var dpt: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dpt)
        }
    @Bindable
    var drt: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drt)
        }
    @Bindable
    var d2B: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.d2B)
        }
    @Bindable
    var d2N: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.d2N)
        }
    @Bindable
    var fmC2N: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.fmC2N)
        }
    @Bindable
    var fmC2W: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.fmC2W)
        }

    @Bindable
    @Transient
    var qt1:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.qt1)
        }
    @Bindable
    @Transient
    var qt2:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.qt2)
        }
    @Bindable
    @Transient
    var qt3:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.qt3)
        }
    @Bindable
    @Transient
    var qt4:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.qt4)
        }
    @Bindable
    @Transient
    var qt5:String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.qt5)
        }
    @Bindable
    @Transient
    var ct1:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ct1)
        }
    @Bindable
    @Transient
    var ct2:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ct2)
        }
    @Bindable
    @Transient
    var ct3:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ct3)
        }
    @Bindable
    @Transient
    var ct4:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ct4)
        }
    @Bindable
    @Transient
    var ct5:String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.ct5)
        }

    @Bindable
    @Transient
    var showct5:Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.showct5)
        }

    fun judgeData(){
        when(diagnosisCode){
            "215-1"->{//胸痛
                qt1 = "D2N"
                qt2 = "D2B"
                qt3 = "FMC2W"
                qt4 = "FMC2N"
                qt5 = ""
                ct1 = trueData(d2N)
                ct2 = trueData(d2B)
                ct3 = trueData(fmC2W)
                ct4 = trueData(fmC2N)
                ct5 = ""
                showct5 = false
            }
            "215-2"->{//卒中
                qt1 = "DNT"
                qt2 = "ONT"
                qt3 = "ODT"
                qt4 = "DPT"
                qt5 = "DRT"
                ct1 = trueData(dnt)
                ct2 = trueData(ont)
                ct3 = trueData(odt)
                ct4 = trueData(dpt)
                ct5 = trueData(drt)
                showct5 = true
            }

        }
    }

    fun trueData(num:Int?):String{
        if (num==null) return "" else return num.toString()
    }




}