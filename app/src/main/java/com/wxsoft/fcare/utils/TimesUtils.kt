package com.wxsoft.fcare.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import java.util.*

@Deprecated(message = "将使用BindingAdapter")
class TimesUtils {
    interface SelectTimeListener {
        fun theTime(mTime: String, type: String)
    }

    companion object {
        private var mSelectTimeListener: SelectTimeListener? = null
        @JvmStatic
        fun selectTime(contect: Context, selectTimeListener: SelectTimeListener, type: String) {
            var ca = Calendar.getInstance()
            var mYear = ca.get(Calendar.YEAR)
            var mMonth = ca.get(Calendar.MONTH)
            var mDay = ca.get(Calendar.DAY_OF_MONTH)
            var mHour = ca.get(Calendar.HOUR_OF_DAY)
            var mMin = ca.get(Calendar.MINUTE)
            mSelectTimeListener = selectTimeListener
            var dialog =
                DatePickerDialog(contect, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    mYear = year
                    mMonth = monthOfYear
                    mDay = dayOfMonth
                    val timeDialog = TimePickerDialog(
                        contect,
                        TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, i: Int, i1: Int ->
                            mHour = i
                            mMin = i1
                            var times =
                                StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay)
                                    .append(" ")
                                    .append(mHour).append(":").append(mMin).append(":").append("00").toString()
                            mSelectTimeListener?.theTime(times, type)
                            if (mSelectTimeListener != null)
                                mSelectTimeListener = null
                        }, mHour, mMin, true
                    )

                    timeDialog.setOnCancelListener { mSelectTimeListener = null }
                    timeDialog.show()
                }, mYear, mMonth, mDay)
            dialog.setOnCancelListener { mSelectTimeListener = null }
            dialog.show()
        }

        fun getCurrentTime(): String {
            val calendar = Calendar.getInstance()
            //年
            var year = calendar.get(Calendar.YEAR)
            //月
            var month = frontCompWithZore(calendar.get(Calendar.MONTH) + 1, 2)
            //日
            var day = frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH), 2)
            //获取系统时间
            //小时
            var hour = frontCompWithZore(calendar.get(Calendar.HOUR_OF_DAY), 2)
            //分钟
            var minute = frontCompWithZore(calendar.get(Calendar.MINUTE), 2)
            //秒
            var second = frontCompWithZore(calendar.get(Calendar.SECOND), 2)

            var date = "" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second
            return date
        }

        /**
        　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
        　　* @param sourceDate
        　　* @param formatLength
        　　* @return 重组后的数据
        　　*/
        fun frontCompWithZore(sourceDate: Int, formatLength: Int): String {
            /*
        　　      * 0 指前面补充零
        　　      * formatLength 字符总长度为 formatLength
        　　      * d 代表为正数。
        　　      */
            var newString = String.format("%0" + formatLength + "d", sourceDate)
            return newString

        }

    }
}






