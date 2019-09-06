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
    @Named("WorkOperations")
    fun providesWorkOperations(context: Context): Array<Pair<String,Int>> =
        arrayOf(

            //患者信息
    Pair("hzxxlr",R.drawable.ic_work_space_patient_info),
    //初步诊断-->
    Pair("zd",R.drawable.ic_work_space_diagnosis),
    //心电图-->
    Pair("xdt",R.drawable.ic_work_space_etc),
    //体征-->
    Pair("smtz",R.drawable.ic_work_space_signs),
    //主诉-->
    Pair("cc",R.drawable.ic_work_space_complaints),

    //检查检验-->
    Pair("fzjc",R.drawable.ic_work_space_checking),

    //ct/mri-->
    Pair("ctscz",R.drawable.ic_work_space_ct_mri),

    //通知-->
    Pair("xxtz",R.drawable.ic_work_space_notification),

    //疾病史-->
    Pair("illness_history",R.drawable.ic_work_space_disease_history),

    //量表评估-->
    Pair("gracepf",R.drawable.ic_work_space_rating),

    //治疗方案-->
    Pair("zlcl",R.drawable.ic_work_space_therapeutic_solution),

    //知情谈话-->
    Pair("informed_consent_statement",R.drawable.ic_work_space_inform),
    //用药-->
    Pair("hzgy",R.drawable.ic_work_space_medication),
    //溶栓时间轴-->
    Pair("rscz",R.drawable.ic_work_space_thrombolytic_timeline),
    //pci-->
    Pair("pci",R.drawable.ic_work_space_pci),
    //并发症-->
    Pair("complications",R.drawable.ic_work_space_complications),

    //评估结果-->
    Pair("rating_result",R.drawable.ic_work_space_rating_result),
    //查体-->
    Pair("physical_examination",R.drawable.ic_work_space_checkbody),
    //措施-->
    Pair("disposition_measures",R.drawable.ic_work_space_lapse),
    //通知启动导管室-->
    Pair("tzqddgs",R.drawable.ic_work_space_pci),


    //通知启动CT室-->
    Pair("tzqdcts",R.drawable.ic_work_space_ct_mri),
    //导管室操作-->
    Pair("dgscz",R.drawable.ic_work_space_pci),

    //CABG-->
    Pair("cabg",R.drawable.ic_work_space_complications),

    //出院诊断-->
    Pair("cyzd",R.drawable.ic_work_space_leavehospital),

    //患者转归-->
    Pair("hzzg",R.drawable.ic_work_space_complaints),
    //治疗操作-->
    Pair("zlcz",R.drawable.ic_work_space_measures),
    //ACS给药-->
    Pair("acsgy",R.drawable.ic_work_space_medication),
    //一键通知-->
    Pair("yjtz",R.drawable.ic_work_space_notification),
    //肌钙蛋白-->
    Pair("jgdb",R.drawable.ic_work_space_checkbody),
    //来院方式-->
    Pair("lyfs",R.drawable.ic_work_space_lapse),
    //血压监测-->
    Pair("xyjc",R.drawable.ic_blood_pressure),
    //评估表-->
    Pair("pgb",R.drawable.ic_get_blood),
    Pair("ry",R.drawable.ic_in_hospital),
    Pair("cx",R.drawable.ic_blood_test),
    Pair("xtzd",R.drawable.ic_work_space_diagnosis)
        )



}