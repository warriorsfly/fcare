package com.wxsoft.fcare.utils

import java.text.SimpleDateFormat

class DateTimeUtils {


    companion object {
        var formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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

