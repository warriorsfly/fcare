package com.wxsoft.fcare.core.di

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named

@Module
class AMapModule{

   /**
     * 创建位置检测点
     */
    @Provides
    @Reusable
    @Named("single")
    fun provideLocationClient(context: Context,@Named("single" )option:AMapLocationClientOption)
           = AMapLocationClient(context).apply {
       setLocationOption(option)
   }

    /**
     * 创建位置检测点
     */
    @Provides
    @Reusable
    @Named("single")
    fun provideLocationOption()= AMapLocationClientOption().apply{

        locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        isOnceLocation = true
        isNeedAddress = true
        httpTimeOut = 20000
    }


}