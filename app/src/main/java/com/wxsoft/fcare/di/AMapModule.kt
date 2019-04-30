package com.wxsoft.fcare.di

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
    fun provideLocationClient(context: Context):AMapLocationClient{
       return AMapLocationClient(context).apply {
           val option=AMapLocationClientOption().apply{
               locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
               isOnceLocation = true
               isNeedAddress = true
               httpTimeOut = 20000
           }
           setLocationOption(option)
       }
   }

}