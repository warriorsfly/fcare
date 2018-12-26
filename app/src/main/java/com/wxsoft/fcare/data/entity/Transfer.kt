package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.wxsoft.fcare.BR
import com.wxsoft.fcare.R

data class Transfer(val id:String):BaseObservable() {

    var coming_Way_Code	:String=""
    @Bindable get() {return  field}

    @Expose

    var selectedItemIndex:Int=0
        get() {
             if(field==0){
                field=when (coming_Way_Code) {
                    "1" -> R.id.in_typ1
                    "2" -> R.id.in_typ2
                    "3" -> R.id.in_typ3
                    "4" -> R.id.in_typ4
                    else -> -1
                }
            }
            return field
        }
    @Bindable set(value) {
            if(field!=value) {
                field = value
                coming_Way_Code = when (selectedItemIndex) {
                    R.id.in_typ1 -> "1"
                    R.id.in_typ2 -> "2"
                    R.id.in_typ3 -> "3"
                    R.id.in_typ4 -> "4"
                    else -> ""
                }
                notifyPropertyChanged(BR.selectedItemIndex)
                notifyPropertyChanged(BR.coming_Way_Code)
            }




        }


    @Expose
    var dispatchIndex:Int=0
        get(){
            if(field==0){
                field=when (dispatch_Unit_Code) {
                    "1" -> R.id.trans_typ1
                    "2" -> R.id.trans_typ2
                    "3" -> R.id.trans_typ3
                    else -> -1
                }
            }

            return field
        }
    @Bindable set(value) {
            if(field!=value) {
                field = value

                dispatch_Unit_Code = when (dispatchIndex) {
                    R.id.trans_typ1 -> "1"
                    R.id.trans_typ2 -> "2"
                    R.id.trans_typ3 -> "3"
                    else -> ""
                }
                notifyPropertyChanged(BR.dispatchIndex)
                notifyPropertyChanged(BR.dispatch_Unit_Code)

            }


        }
    /**
     *出车单位代码
     * 字典：18
     * 1 120救护车 2 本院救护车 3 外院救护车，传1,2,3或空值
     */
    var dispatch_Unit_Code	:String=""
        @Bindable get() {return  field}
//        set(value) {
//            if(field!=value) {
//                field = value
//                dispatchIndex = when (dispatch_Unit_Code) {
//                    "1" -> R.id.trans_typ1
//                    "2" -> R.id.trans_typ2
//                    "3" -> R.id.trans_typ3
//                    else -> -1
//                }
//            }
//            notifyPropertyChanged(BR.dispatch_Unit_Code)
//        }


    @Bindable
    var arrived_Emergency_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.arrived_Emergency_Time)
        }

    @Bindable
    var arrived_Ccu_Date:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.arrived_Ccu_Date)
        }
    @Bindable
    var is_Netword_Hospital:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.is_Netword_Hospital)
        }
    @Bindable
    var hospital_Name:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.hospital_Name)
        }
    @Bindable
    var department:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.department)
        }
    @Bindable
    var outhospital_Visit_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.outhospital_Visit_Time)
        }
    @Bindable
    var transfer_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.transfer_Time)
        }
    @Bindable
    var  ambulance_Arrived_Time	:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.ambulance_Arrived_Time)
        }
    @Bindable
    var leave_Outhospital_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.leave_Outhospital_Time)
        }
    @Bindable
    var arrived_Scene_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.arrived_Scene_Time)
        }
    @Bindable
    var arrived_Hospital_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.arrived_Hospital_Time)
        }
    @Bindable
    var inhospital_Admission_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.inhospital_Admission_Time)
        }
    @Bindable
    var attack_Department:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.attack_Department)
        }
    @Bindable
    var consultation_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.consultation_Time)
        }
    @Bindable
    var leave_Department_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.leave_Department_Time)
        }
    @Bindable
    var patientId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.patientId)
        }

    @Bindable
    var createrId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrId)
        }
    @Bindable
    var createrName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createrName)
        }
    @Bindable
    @Transient
    var createdDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.createdDate)
        }
    @Bindable
    var modifierId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierId)
        }
    @Bindable
    var modifierName:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifierName)
        }
    @Bindable
    var modifiedDate:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.modifiedDate)
        }
}