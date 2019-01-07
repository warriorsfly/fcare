package com.wxsoft.fcare.core.data.remote

import android.graphics.Bitmap
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FileApi{

    @POST("File/UploadAsync")
    fun save(@Body bitmaps:List<Bitmap>):Maybe<Response<String>>
}