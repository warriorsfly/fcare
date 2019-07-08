package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR
data class ComingBy(val id:String=""): BaseObservable() {

   @SerializedName("coming_Way_Code")
   @Bindable
   var comingWayCode: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.comingWayCode)
       }

    @SerializedName("coming_Way_Code_Name")
    @Bindable
    var comingWayName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.comingWayName)
        }
   /**
    *出车单位代码
    * 字典：18
    * 1 120救护车 2 本院救护车 3 外院救护车，传1,2,3或空值
    */
  
   @SerializedName("dispatch_Unit_Code")
   @Bindable
   var dispatchCode: String = ""
      set(value) {
          field = value
          notifyPropertyChanged(BR.dispatchCode)
       }

    @SerializedName("dispatch_Unit_Code_Name")
    @Bindable
    var dispatchName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dispatchName)
        }

  @SerializedName("arrived_Emergency_Time")
   @Bindable
   var arrivedTime: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.arrivedTime)
       }

   @Bindable
   var arrived_Ccu_Date: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.arrived_Ccu_Date)
       }
   @Bindable
   var is_Netword_Hospital: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.is_Netword_Hospital)
       }
   @Bindable
   var hospital_Name: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.hospital_Name)
       }
   @Bindable
   var department: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.department)
       }
   @Bindable
   var outhospital_Visit_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.outhospital_Visit_Time)
       }
   @Bindable
   var transfer_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.transfer_Time)
       }
   @Bindable
   var ambulance_Arrived_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.ambulance_Arrived_Time)
       }
   @Bindable
   var leave_Outhospital_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.leave_Outhospital_Time)
       }
   @Bindable
   var arrived_Scene_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.arrived_Scene_Time)
       }
   @Bindable
   var arrived_Hospital_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.arrived_Hospital_Time)
       }
   @Bindable
   var inhospital_Admission_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.inhospital_Admission_Time)
       }
   @Bindable
   var attack_Department: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.attack_Department)
       }
   @Bindable
   var consultation_Time: String? = null
       set(value) {
           field = value
           notifyPropertyChanged(BR.consultation_Time)
       }
   @Bindable
   var leave_Department_Time: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.leave_Department_Time)
       }
   @Bindable
   var patientId: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.patientId)
       }

   @Bindable
   var createrId: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.createrId)
       }
   @Bindable
   var createrName: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.createrName)
       }
   @Bindable
   var createdDate: String? = null
       set(value) {
           field = value
           notifyPropertyChanged(BR.createdDate)
       }
   @Bindable
   var modifierId: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.modifierId)
       }
   @Bindable
   var modifierName: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.modifierName)
       }
   @Bindable
   var modifiedDate: String = ""
       set(value) {
           field = value
           notifyPropertyChanged(BR.modifiedDate)
       }

    var comingWayStaffs: List<ComingByStaff> = emptyList()

    /**
     * 接诊医生
     */
    @Transient
    @Bindable
    var emergencyDoctor:User=User()
      set(value) {
                field = value
                notifyPropertyChanged(BR.emergencyDoctor)
            }
    /**
     * 接诊护士
     */
    @Transient
    @Bindable
    var emergencyNurse:User=User()
      set(value) {
                field = value
                notifyPropertyChanged(BR.emergencyNurse)
            }
    /**
     * 会诊医生
     */
    @Transient
    @Bindable
    var consultantDoctors:String = ""
    set(value) {
           field = value
           notifyPropertyChanged(BR.consultantDoctors)
       }
}