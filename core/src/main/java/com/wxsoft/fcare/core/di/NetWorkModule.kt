package com.wxsoft.fcare.core.di

import com.google.gson.Gson
import com.wxsoft.fcare.core.BuildConfig
import com.wxsoft.fcare.core.data.remote.AccountApi
import com.wxsoft.fcare.core.data.remote.CarApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.remote.log.LogInterceptor
import com.wxsoft.fcare.core.data.remote.log.Logger
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetWorkModule {

    @Provides
    @Singleton
    fun provideGson():Gson{
        return Gson()
    }
    /**
     * 未登陆时候使用
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val builder= Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)


        //测试模式下打log
        if(BuildConfig.DEBUG){

            val logging = LogInterceptor(Logger())
            logging.setLevel(LogInterceptor.Level.BODY)

            val client= OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(200, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(logging)
                    .build()
            builder.client(client)
        }

        return builder.build()
    }

    @Provides
    fun providePatientApi(retrofit: Retrofit):PatientApi{

        return retrofit.create(PatientApi::class.java)
    }

    @Provides
    fun provideAccountApi(retrofit: Retrofit):AccountApi{

        return retrofit.create(AccountApi::class.java)
    }

    @Provides
    fun provideTaskApi(retrofit: Retrofit):TaskApi{

        return retrofit.create(TaskApi::class.java)
    }

    @Provides
    fun provideCarApi(retrofit: Retrofit):CarApi{

        return retrofit.create(CarApi::class.java)
    }

    @Provides
    fun provideVitalSignApi(retrofit: Retrofit):VitalSignApi{

        return retrofit.create(VitalSignApi::class.java)
    }

    @Provides
    fun provideDictEnumApi(retrofit: Retrofit):DictEnumApi{

        return retrofit.create(DictEnumApi::class.java)
    }

    @Provides
    fun provideCheckBodyApi(retrofit: Retrofit):CheckBodyApi{

        return retrofit.create(CheckBodyApi::class.java)
    }

    @Provides
    fun provideFileApi(retrofit: Retrofit):FileApi{

        return retrofit.create(FileApi::class.java)
    }


}