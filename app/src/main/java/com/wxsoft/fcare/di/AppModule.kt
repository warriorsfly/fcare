package com.wxsoft.fcare.di

import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import com.wxsoft.fcare.App
import com.wxsoft.fcare.R
import com.wxsoft.fcare.core.data.prefs.PreferenceStorage
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Provides
    fun providesWifiManager(context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun providesClipboardManager(context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

    @Singleton
    @Provides
    @Named("WorkOperationIcon")
    fun providesIcoWorkOperations() =
        arrayOf(
            R.drawable.ic_work_space_patient_info,
            R.drawable.ic_work_space_diagnosis,
            R.drawable.ic_work_space_etc,
            R.drawable.ic_work_space_signs,
            R.drawable.ic_work_space_complaints,
            R.drawable.ic_work_space_checking,
            R.drawable.ic_work_space_ct_mri,
            R.drawable.ic_work_space_notification,
            R.drawable.ic_work_space_disease_history,
            R.drawable.ic_work_space_rating,
            R.drawable.ic_work_space_therapeutic_solution,
            R.drawable.ic_work_space_inform,
            R.drawable.ic_work_space_medication,
            R.drawable.ic_work_space_thrombolytic_timeline,
            R.drawable.ic_work_space_pci,
            R.drawable.ic_work_space_complications,
            R.drawable.ic_work_space_rating_result,
            R.drawable.ic_work_space_checkbody,
            R.drawable.ic_work_space_measures,
            R.drawable.ic_work_space_notification,
            R.drawable.ic_work_space_notification,
            R.drawable.ic_work_space_catheter,
            R.drawable.ic_work_space_cabg,
            R.drawable.ic_work_space_leavehospital,
            R.drawable.ic_work_space_lapse,
            R.drawable.ic_work_space_pci,
            R.drawable.ic_work_space_medication,
            R.drawable.ic_call_phone,
            R.drawable.ic_work_space_jgdb,
            R.drawable.ic_work_space_lapse
        )

    @Singleton
    @Provides
    @Named("WorkOperationTint")
    fun providesTintWorkOperations(context: Context): IntArray =
        context.resources.getIntArray(R.array.color_work_space_operations)

    @Singleton
    @Provides
    @Named("ratingTint")
    fun providesTintRating(context: Context): IntArray =
        context.resources.getIntArray(R.array.color_ratings)
    @Singleton
    @Provides
    @Named("WorkOperationKey")
    fun providesKeyWorkOperations(context: Context): Array<String> =
        context.resources.getStringArray(R.array.key_work_space_operations)



}