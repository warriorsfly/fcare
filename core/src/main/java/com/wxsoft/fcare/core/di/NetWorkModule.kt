package com.wxsoft.fcare.core.di

import com.google.gson.Gson
import com.wxsoft.fcare.core.BuildConfig
import com.wxsoft.fcare.core.data.remote.*
import com.wxsoft.fcare.core.data.remote.log.LogInterceptor
import com.wxsoft.fcare.core.data.remote.log.Logger
import com.wxsoft.fcare.core.data.remote.version.VersionApi
import com.wxsoft.fcare.core.domain.repository.message.IMessageRepository
import com.wxsoft.fcare.core.domain.repository.message.PageKeyMessageRepository
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.core.domain.repository.patients.PageKeyPatientRepository
import com.wxsoft.fcare.core.domain.repository.tasks.ITaskRepository
import com.wxsoft.fcare.core.domain.repository.tasks.PageKeyTaskRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
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
    fun provideVitalSignApi(retrofit: Retrofit): VitalSignApi {

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
    fun provideMedicalHistoryApi(retrofit: Retrofit):MedicalHistoryApi{

        return retrofit.create(MedicalHistoryApi::class.java)
    }

    @Provides
    fun provideFileApi(retrofit: Retrofit):FileApi{

        return retrofit.create(FileApi::class.java)
    }

    @Provides
    fun provideEmrApi(retrofit: Retrofit):EmrApi{

        return retrofit.create(EmrApi::class.java)
    }

    @Provides
    fun provideMeasuresApi(retrofit: Retrofit):MeasuresApi{

        return retrofit.create(MeasuresApi::class.java)
    }

    @Provides
    fun providePharmacyApi(retrofit: Retrofit):PharmacyApi{

        return retrofit.create(PharmacyApi::class.java)
    }

    @Provides
    fun provideRatingApi(retrofit: Retrofit):RatingApi{

        return retrofit.create(RatingApi::class.java)
    }

    @Provides
    fun provideDiagnoseApi(retrofit: Retrofit):DiagnoseApi{

        return retrofit.create(DiagnoseApi::class.java)
    }

    @Provides
    fun provideECGApi(retrofit: Retrofit):ECGApi{

        return retrofit.create(ECGApi::class.java)
    }

    @Provides
    fun provideInformedApi(retrofit: Retrofit):InformedApi{

        return retrofit.create(InformedApi::class.java)
    }

    @Provides
    fun provideInterventionApi(retrofit: Retrofit):InterventionApi{

        return retrofit.create(InterventionApi::class.java)
    }

    @Provides
    fun providePatientRepository(patientApi: PatientApi):IPatientRepository{
        return PageKeyPatientRepository(patientApi)
    }
    @Provides
    fun provideTaskRepository(taskApi: TaskApi): ITaskRepository {
        return PageKeyTaskRepository(taskApi)
    }

    @Provides
    fun provideMessageRepository(api: MessageApi): IMessageRepository {
        return PageKeyMessageRepository(api)
    }

    @Provides
    fun provideThrombolysisApi(retrofit: Retrofit):ThrombolysisApi{
        return retrofit.create(ThrombolysisApi::class.java)
    }

    @Provides
    fun provideDischargeApi(retrofit: Retrofit):DischargeApi{
        return retrofit.create(DischargeApi::class.java)
    }

    @Provides
    fun providePACSApi(retrofit: Retrofit):PACSApi{
        return retrofit.create(PACSApi::class.java)
    }

    @Provides
    fun provideLISApi(retrofit: Retrofit):LISApi{
        return retrofit.create(LISApi::class.java)
    }

    @Provides
    fun provideQualityControlApi(retrofit: Retrofit):QualityControlApi{
        return retrofit.create(QualityControlApi::class.java)
    }

    @Provides
    fun provideNotificationApi(retrofit: Retrofit):NotificationApi{
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    fun provideVersionApi(retrofit: Retrofit):VersionApi{
        return retrofit.create(VersionApi::class.java)
    }

    @Provides
    fun provideMessageApi(retrofit: Retrofit):MessageApi{
        return retrofit.create(MessageApi::class.java)
    }

    @Provides
    @Reusable
    fun provideShareApi(retrofit: Retrofit):ShareApi{
        return retrofit.create(ShareApi::class.java)
    }

}