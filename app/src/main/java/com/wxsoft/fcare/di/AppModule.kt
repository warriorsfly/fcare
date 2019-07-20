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
            //初步诊断
            R.drawable.ic_work_space_diagnosis,
            //心电图
            R.drawable.ic_work_space_etc,
            //体征
            R.drawable.ic_work_space_signs,
            //主诉
            R.drawable.ic_work_space_complaints,
            //检查检验
            R.drawable.ic_work_space_checking,
            //ct/mri
            R.drawable.ic_work_space_ct_mri,
            //通知
            R.drawable.ic_work_space_notification,
            //疾病史
            R.drawable.ic_work_space_disease_history,
            //量表评估
            R.drawable.ic_work_space_rating,
            //治疗方案
            R.drawable.ic_work_space_therapeutic_solution,
            //知情谈话
            R.drawable.ic_work_space_inform,
            //用药
            R.drawable.ic_work_space_medication,
            //溶栓时间轴
            R.drawable.ic_work_space_thrombolytic_timeline,
            //介入手术
            R.drawable.ic_work_space_pci,
            //并发症
            R.drawable.ic_work_space_complications,
            //评估结果
            R.drawable.ic_work_space_rating_result,
            //查体
            R.drawable.ic_work_space_checkbody,
            //患者转归
            R.drawable.ic_work_space_lapse,
            //pci
            R.drawable.ic_work_space_pci,
            //并发症
            R.drawable.ic_work_space_complications,
            //评估结果
            R.drawable.ic_work_space_rating_result,
            //查体
            R.drawable.ic_work_space_checkbody,
            //措施
            R.drawable.ic_work_space_measures,
            //通知启动导管室
            R.drawable.ic_work_space_pci,
            //通知启动CT室
            R.drawable.ic_work_space_ct_mri,
            //ACS给药
            R.drawable.ic_work_space_medication,
            //一键通知
            R.drawable.ic_call_phone,
            //肌钙蛋白
            R.drawable.ic_work_space_jgdb,
            //来院方式
            R.drawable.ic_work_space_lapse,
            //血压监测
            R.drawable.ic_blood_pressure,
            //评估表
            R.drawable.ic_blood_test,
            R.drawable.ic_in_hospital,
            R.drawable.ic_get_blood
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