package com.wxsoft.fcare.ui

import androidx.fragment.app.DialogFragment
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.wxsoft.fcare.R


abstract class BaseTimingActivity : BaseActivity(), OnDateSetListener {


    protected fun createDialog(time:Long): TimePickerDialog {

        return TimePickerDialog.Builder()
            .setThemeColor(resources.getColor(R.color.colorPrimary))
            .setCallBack(this)
            .setCancelStringId("取消")
            .setSureStringId("确定")
            .setTitleStringId("选择时间")
            .setYearText("")
            .setMonthText("")
            .setDayText("")
            .setHourText("")
            .setMinuteText("")
            .setCyclic(false)
            .setCurrentMillseconds(if(time==0L)System.currentTimeMillis() else time)
            .setType(Type.ALL)
            .setWheelItemTextSize(12)
            .build().apply {  setStyle(DialogFragment.STYLE_NORMAL,R.style.Theme_FCare_Dialog)}
    }
}