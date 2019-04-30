package com.wxsoft.fcare.di

import com.wxsoft.fcare.App
import com.wxsoft.fcare.core.di.NetWorkModule
import com.wxsoft.fcare.core.di.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class
        , AppModule::class
        , ViewModelModule::class
        , ActivityBindingModule::class
        , NetWorkModule::class
        , AMapModule::class
        , BaiduMapModule::class]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}