package com.wxsoft.fcare.core.data.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.wxsoft.fcare.core.BR

data class OutComeCheck (val id:String) : BaseObservable() {

    @Bindable
    var patientId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.patientId)
        }

    /// <summary>
    /// 72h内肌钙蛋白
    /// </summary>
    @Bindable
    var troponin_72h: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.troponin_72h)
        }


    /// <summary>
    /// 72h内肌钙蛋白最高值
    /// 1:TNT,2:TNI
    /// </summary>
    @Bindable
    var troponin_72h_type: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.troponin_72h_type)
        }

    /// <summary>
    ///  72h内肌钙蛋白最高值
    /// </summary>
    @Bindable
    var ctnI_MAX_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.ctnI_MAX_VALUE)
        }

    /// <summary>
    ///  脑钠肽
    /// </summary>
    @Bindable
    var bnp: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.bnp)
        }

    /// <summary>
    ///  脑钠肽类型
    ///  1:BNP,2:NT-proBNP
    /// </summary>
    @Bindable
    var bnP_TYPE: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.bnP_TYPE)
        }

    /// <summary>
    /// 脑钠肽最高值
    /// </summary>
    @Bindable
    var bnP_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.bnP_VALUE)
        }

    /// <summary>
    /// 总胆固醇(TC)
    /// </summary>
    @Bindable
    var tc: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.tc)
        }

    /// <summary>
    /// 总胆固醇(TC)
    /// </summary>
    @Bindable
    var tc_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.tc_VALUE)
        }


    /// <summary>
    /// 甘油三酯(TG)
    /// </summary>
    @Bindable
    var tg: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.tg)
        }

    /// <summary>
    ///甘油三酯(TG)数值
    /// </summary>
    @Bindable
    var tg_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.tg_VALUE)
        }


    /// <summary>
    /// 高密度脂蛋白(HDL-C)
    /// </summary>
    @Bindable
    var hdL_C: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.hdL_C)
        }

    /// <summary>
    ///高密度脂蛋白(HDL-C)
    /// </summary>
    @Bindable
    var hdL_C_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.hdL_C_VALUE)
        }


    /// <summary>
    /// 低密度脂蛋白(LDL-C)
    /// </summary>
    @Bindable
    var ldL_C: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.ldL_C)
        }

    /// <summary>
    /// 低密度脂蛋白(LDL-C)数值必填
    /// </summary>
    @Bindable
    var ldL_C_VALUE: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.ldL_C_VALUE)
        }

    /// <summary>
    /// 超声心动图
    /// </summary>
    @Bindable
    var ucg: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.ucg)
        }

    /// <summary>
    ///LVEF 诊疗过程中最低值
    /// </summary>
    @Bindable
    var lvef: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.lvef)
        }


    /// <summary>
    /// 室壁瘤
    /// </summary>
    @Bindable
    var vntriculaR_ANEURYSM: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.vntriculaR_ANEURYSM)
        }

    /// <summary>
    /// 局部室壁活动异常
    /// </summary>
    @Bindable
    var parT_VNTRICULAR: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.parT_VNTRICULAR)
        }


}