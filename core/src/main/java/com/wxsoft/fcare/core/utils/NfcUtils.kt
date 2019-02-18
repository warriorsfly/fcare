package com.wxsoft.fcare.core.utils

class NfcUtils{

    companion object {

         fun toHexString(bytes: ByteArray): String {
            var i: Int
            var j = 0
            var hexInt: Int
            val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
            var out = ""


            while (j < bytes.size) {
                hexInt = bytes[j].toInt() and 0xff
                i = hexInt shr 4 and 0x0f
                out += hex[i]
                i = hexInt and 0x0f
                out += hex[i]
                ++j
            }
            return out
        }
    }

}