package com.wxsoft.fcare.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wxsoft.fcare.BuildConfig
import com.wxsoft.fcare.data.remote.*
import com.wxsoft.fcare.data.remote.log.LogInterceptor
import com.wxsoft.fcare.data.remote.log.Logger
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
    fun provideRetrofit(gson:Gson): Retrofit {

        val builder= Retrofit.Builder()

                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.API_ENDPOINT)


        //测试模式下打log
        if(BuildConfig.DEBUG){

            val logging = LogInterceptor(Logger())
            logging.setLevel(LogInterceptor.Level.BODY)

            val client= OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
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
    fun provideEvaluationApi(retrofit: Retrofit):EvaluationApi{

        return retrofit.create(EvaluationApi::class.java)
    }
    @Provides
    fun provideDictEnumApi(retrofit: Retrofit):DictEnumApi{

        return retrofit.create(DictEnumApi::class.java)
    }

    @Provides
    fun provideOperationMenuApi(retrofit: Retrofit):OperationMenuApi{

        return retrofit.create(OperationMenuApi::class.java)
    }

    @Provides
    fun provideEmrLogApi(retrofit: Retrofit):EmrLogApi{

        return retrofit.create(EmrLogApi::class.java)
    }

    @Provides
    fun provideVitalSignApi(retrofit: Retrofit):VitalSignApi{

        return retrofit.create(VitalSignApi::class.java)
    }

    @Provides
    fun provideAccountApi(retrofit: Retrofit):AccountApi{

        return retrofit.create(AccountApi::class.java)
    }

    @Provides
    fun provideGpsApi(retrofit: Retrofit):GpsApi{

        return retrofit.create(GpsApi::class.java)
    }

    @Provides
    fun provideNotifyApi(retrofit: Retrofit):NotifyApi{

        return retrofit.create(NotifyApi::class.java)
    }

    @Provides
    fun provideInitialDiagnosisApi(retrofit: Retrofit):InitialDiagnosisApi{

        return retrofit.create(InitialDiagnosisApi::class.java)
    }

    @Provides
    fun provideAssistCheckApi(retrofit: Retrofit):AssistCheckApi{

        return retrofit.create(AssistCheckApi::class.java)
    }

    @Provides
    fun provideDetourApi(retrofit: Retrofit):DetourApi{

        return retrofit.create(DetourApi::class.java)
    }

    @Provides
    fun provideDrugApi(retrofit: Retrofit):DrugApi{

        return retrofit.create(DrugApi::class.java)
    }

    @Provides
    fun provideOutComeApi(retrofit: Retrofit):OutComeApi{

        return retrofit.create(OutComeApi::class.java)
    }

    @Provides
    fun provideOutHospitalApi(retrofit: Retrofit):OutHospitalApi{

        return retrofit.create(OutHospitalApi::class.java)
    }


    @Provides
    fun provideGraceApi(retrofit: Retrofit):GraceApi{

        return retrofit.create(GraceApi::class.java)
    }

    @Provides
    fun providePciApi(retrofit: Retrofit):PciApi{

        return retrofit.create(PciApi::class.java)
    }

    @Provides
    fun provideThromApi(retrofit: Retrofit):ThromApi{

        return retrofit.create(ThromApi::class.java)
    }

    @Provides
    fun provideTransferApi(retrofit: Retrofit):TransferApi{

        return retrofit.create(TransferApi::class.java)
    }

}