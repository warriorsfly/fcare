package com.wxsoft.fcare.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import com.wxsoft.fcare.BR

data class Pci constructor( var id:String=""): BaseObservable() {

    /**业务流程
     * step1 ->step2->....->step9
     */

    /**
     * 患者id
     */
    @Bindable
    var patientId:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.patientId)
        }


    /**
     * 记录当前步骤
     */
    @Bindable
    @Transient
    var businessProcess:String="step1"
        set(value) {
            field=value
            notifyPropertyChanged(BR.businessProcess)
        }

    /**
     * 提交按钮文字  下一步
     * 最后一步 提交
     */
    @Bindable
    @Transient
    var submitBtnTxt:String="下 一 步"
        set(value) {
            field=value
            notifyPropertyChanged(BR.submitBtnTxt)
        }

    /**
     * 决策医生id
     */
    @Bindable
    var doctor_Id:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.doctor_Id)
        }

    /**
     * 决策医生姓名
     */
    @Bindable
    var doctor_Name:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.doctor_Name)
        }

    /**
     * 介入人员id,id
     */
    @Bindable
    var intervention_Person_Id:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.intervention_Person_Id)
        }
    /**
     * 介入人员姓名
     */
    @Bindable
    var intervention_Person:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.intervention_Person)
        }


    /**
     * 决定介入手术的时间
     */
    @Bindable
    var decision_Operation_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.decision_Operation_Time)
        }

    /**
     * 启动导管室的时间
     */
    @Bindable
    var start_Conduit_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Conduit_Time)
        }

    /**
     * 开始知情同意书时间
     */
    @Bindable
    var start_Agree_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Agree_Time)
        }

    /**
     * 签署知情同意书时间
     */
    @Bindable
    var sign_Agree_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.sign_Agree_Time)
        }

    /**
     * 导管室激活时间
     */
    @Bindable
    var activate_Conduit_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.activate_Conduit_Time)
        }

    /**
     * 患者到达导管室时间
     */
    @Bindable
    var arrive_Conduit_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.arrive_Conduit_Time)
        }

    /**
     * 开始穿刺时间
     */
    @Bindable
    var start_Puncture_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Puncture_Time)
        }

    /**
     * 穿刺成功时间
     */
    @Bindable
    var success_Puncture_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.success_Puncture_Time)
        }

    /**
     * 开始造影时间
     */
    @Bindable
    var start_Radiography_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Radiography_Time)
        }

    /**
     * 造影结束时间
     */
    @Bindable
    var end_Radiography_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.end_Radiography_Time)
        }

    /**
     * 再次签署知情同意书
     */
    @Bindable
    var again_Sign_Agree_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.again_Sign_Agree_Time)
        }

    /**
     * 导丝通过
     */
    @Bindable
    var balloon_Expansion_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.balloon_Expansion_Time)
        }

    /**
     * 开始手术时间
     */
    @Bindable
    var start_Operation_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.start_Operation_Time)
        }

    /**
     * 结束手术时间
     */
    @Bindable
    var end_Operation_Time:String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.end_Operation_Time)
        }


}