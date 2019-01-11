package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi{

    @Multipart
    @POST("File/UploadAsync")
    fun save(@Part("patient")patient: Patient,@Part files: List<MultipartBody.Part>):Maybe<Response<List<String>>>
}