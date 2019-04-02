package com.wxsoft.fcare.core.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtils {


    companion object {
        var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        @JvmStatic
        fun getAgeByCertId(certId: String): Int {
            var birthday = ""
            if (certId.length == 18) {
                birthday = (certId.substring(6, 10) + "-"
                        + certId.substring(10, 12) + "-"
                        + certId.substring(12, 14))
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val now = Date()
            val birth =sdf.parse(birthday)

            val intervalMilli = now.time - birth.time
            val age = (intervalMilli / (24 * 60 * 60 * 1000)).toInt() / 365
            println(age)
            return age
        }

        fun getCurrentTime():String{
            val calendar = Calendar.getInstance()
            //年
            val year = calendar.get(Calendar.YEAR)
            //月
            val month = frontCompWithZore(calendar.get(Calendar.MONTH)+1,2)
            //日
            val day = frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH),2)
            //获取系统时间
            //小时
            val hour = frontCompWithZore(calendar.get(Calendar.HOUR_OF_DAY),2)
            //分钟
            val minute = frontCompWithZore(calendar.get(Calendar.MINUTE),2)
            //秒
            val second = frontCompWithZore(calendar.get(Calendar.SECOND),2)

            return "$year-$month-$day $hour:$minute:$second"
        }

        fun getCurrentDate():String{
            val calendar = Calendar.getInstance()
            //年
            val year = calendar.get(Calendar.YEAR)
            //月
            val month = frontCompWithZore(calendar.get(Calendar.MONTH)+1,2)
            //日
            val day = frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH),2)

            return "$year-$month-$day"
        }

        /**
        　　* 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
        　　* @param sourceDate
        　　* @param formatLength
        　　* @return 重组后的数据
        　　*/
        fun frontCompWithZore(sourceDate:Int,formatLength:Int):String {
            /*
        　　      * 0 指前面补充零
        　　      * formatLength 字符总长度为 formatLength
        　　      * d 代表为正数。
        　　      */
            return String.format("%0"+formatLength+"d", sourceDate)

        }
        private fun getStringToDate(dateString :String, pattern:String) :Long{
            val dateFormat = SimpleDateFormat(pattern)
            val date= dateFormat.parse(dateString)
            return date.time
        }
        fun getAfromB(startDate:String,endDaye:String):String{
            val start = getStringToDate(startDate,"yyyy-MM-dd HH:mm:ss")
            val end = getStringToDate(endDaye,"yyyy-MM-dd HH:mm:ss")
            var c=end-start

            val hour=if(c>3600*1000)  String.format("%02d", c/3600000) else "00"
            c %= (3600 * 1000)
            val minute=if(c>60*1000) String.format("%02d", c/60000)   else  "00"
            c %= (60 * 1000)
            val second=if(c>1000) String.format("%02d", c/1000) else "00"
            return StringBuilder().append(hour).append(":").append(minute).append(":").append(second).toString()
        }
        fun getAAfromBBMinutes(startDate:String,endDaye:String):String{
            val start = getStringToDate(startDate,"yyyy-MM-dd HH:mm:ss")
            val end = getStringToDate(endDaye,"yyyy-MM-dd HH:mm:ss")
            var c=end-start
            val hour=if(c>=3600*1000)  String.format("%d", c/3600000) else "0"
            c %= (3600 * 1000)
            val minute=if(c>=60*1000) String.format("%d", c/60000)   else  "0"
            c %= (60 * 1000)
            val second=if(c>=1000) String.format("%d", c/1000) else "0"
            if (hour.equals("0")){
                return StringBuilder().append(minute).append("分钟").toString()
            }
            return StringBuilder().append(hour).append("小时").append(minute).append("分钟").toString()
        }

        fun compareAandB(startDate:String,endDaye:String):Boolean{
            val start = getStringToDate(startDate,"yyyy-MM-dd HH:mm:ss")
            val end = getStringToDate(endDaye,"yyyy-MM-dd HH:mm:ss")
            var c=end-start
            return c>=0
        }
    }
}

fun getLastMinutes(start:Long?,end:Long?) :Long?{

    val c=end?:0  -(start?:0)
    return if(c>60*1000) c/60000  else null
}


