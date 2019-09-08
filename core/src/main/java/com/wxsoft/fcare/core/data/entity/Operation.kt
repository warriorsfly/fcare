package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.core.BR

data class Operation(var id:String): BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    //入路
    @Bindable
    var route: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.route)
        }
    //入路
    @Bindable
    var route_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.route_Name)
        }
    //阻塞性病变 必填
    @Bindable
    var block: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.block)
        }

    //梗死相关动脉 部位 254
    @Bindable
    var infarct_Position: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.infarct_Position)
        }
    @Bindable
    var infarct_Position_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.infarct_Position_Name)
        }
    //狭窄程度 255
    @Bindable
    var narrow_Level: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.narrow_Level)
        }
    @Bindable
    var narrow_Level_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.narrow_Level_Name)
        }
    //术前 靶血管血流（TIMI）等级
    @Bindable
    var preoperative_Timi_Level: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.preoperative_Timi_Level)
        }
    //术后 靶血管血流（TIMI）等级
    @Bindable
    var postoperative_Timi_Level: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.postoperative_Timi_Level)
        }
    //支架内血栓
    @Bindable
    var bracket_Thrombus: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.bracket_Thrombus)
        }
    //是否分叉病变
    @Bindable
    var branching_Sick: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.branching_Sick)
        }
    //是否CTO
    @SerializedName("is_Cto")
    @Bindable
    var cto: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.cto)
        }
    //非罪犯血管狭窄>50%
    @SerializedName("is_Not_Culprit_Vessel")
    @Bindable
    var culpritVessel: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.culpritVessel)
        }
    //非罪犯血管病变 部位 254
    @Bindable
    var narrow_Position: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.narrow_Position)
        }
    @Bindable
    var narrow_Position_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.narrow_Position_Name)
        }
    //腔内影像 256
    @Bindable
    var intracavity_Image: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.intracavity_Image)
        }
    @Bindable
    var intracavity_Image_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.intracavity_Image_Name)
        }

    //功能检测 257
    @Bindable
    var function_Test: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.function_Test)
        }
    @Bindable
    var function_Test_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.function_Test_Name)
        }
    //血栓抽吸
    @Bindable
    var thrombus_Suction: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.thrombus_Suction)
        }
    //IABP
    @Bindable
    var iabp: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.iabp)
        }
    //临时起搏器
    @Bindable
    var pacemaker: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.pacemaker)
        }
    //ECMO
    @Bindable
    var ecmo: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.ecmo)
        }

    //左心室辅助装置
    @Bindable
    var auxiliary_Device: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.auxiliary_Device)
        }
    //植入支架个数 258
    @Bindable
    var bracket_Num: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bracket_Num)
        }
    @Bindable
    var bracket_Num_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bracket_Num_Name)
        }
    //术中并发症 259
    @Bindable
    var complication: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.complication)
        }
    @Bindable
    var complication_Name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.complication_Name)
        }

    @Bindable
    var createrId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.createrId)
        }

}