package com.wxsoft.fcare.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {


    companion object {
        var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


        fun getCurrentTime():String{
            val calendar = Calendar.getInstance()
            //年
            var year = calendar.get(Calendar.YEAR)
            //月
            var month =frontCompWithZore(calendar.get(Calendar.MONTH)+1,2)
            //日
            var day =frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH),2)
            //获取系统时间
            //小时
            var hour = frontCompWithZore(calendar.get(Calendar.HOUR_OF_DAY),2)
            //分钟
            var minute = frontCompWithZore(calendar.get(Calendar.MINUTE),2)
            //秒
            var second =frontCompWithZore(calendar.get(Calendar.SECOND),2)

            var date = ""+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second
            return date
        }

        fun getCurrentDate():String{
            val calendar = Calendar.getInstance()
            //年
            var year = calendar.get(Calendar.YEAR)
            //月
            var month =frontCompWithZore(calendar.get(Calendar.MONTH)+1,2)
            //日
            var day =frontCompWithZore(calendar.get(Calendar.DAY_OF_MONTH),2)

            var date = ""+year+"-"+month+"-"+day
            return date
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
            var newString = String.format("%0"+formatLength+"d", sourceDate)
            return newString

        }


    }
}

fun getAfromB(start:Long,end:Long):String{

    var c=end-start

    val hour=if(c>3600*1000)  String.format("%02d", c/3600000) else "00"
    c %= (3600 * 1000)
    val minute=if(c>60*1000) String.format("%02d", c/60000)   else  "00"
    c %= (60 * 1000)
    val second=if(c>1000) String.format("%02d", c/1000) else "00"
    return StringBuilder().append(hour).append(":").append(minute).append(":").append(second).toString()
}

