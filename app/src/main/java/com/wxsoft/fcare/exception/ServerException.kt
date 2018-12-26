package com.wxsoft.fcare.exception

import java.lang.Exception

class ServerException :Exception {

    constructor(msg: String):super(msg)
    constructor():super()
}