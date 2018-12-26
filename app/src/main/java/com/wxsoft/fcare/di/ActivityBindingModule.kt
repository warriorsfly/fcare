package com.wxsoft.fcare.di

import com.wxsoft.fcare.ui.calling.CallingActivity
import com.wxsoft.fcare.ui.calling.CallingModule
import com.wxsoft.fcare.ui.detail.PatientDetailActivity
import com.wxsoft.fcare.ui.detail.PatientDetailModule
import com.wxsoft.fcare.ui.income.InComeActivity
import com.wxsoft.fcare.ui.income.InComeModule
import com.wxsoft.fcare.ui.login.LoginActivity
import com.wxsoft.fcare.ui.login.LoginModule
import com.wxsoft.fcare.ui.main.MainActivity
import com.wxsoft.fcare.ui.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have getAll these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PatientDetailModule::class])
    internal abstract fun patientDetailActivity(): PatientDetailActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CallingModule::class])
    internal abstract fun callingActivity(): CallingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [InComeModule::class])
    internal abstract fun incomeActivity(): InComeActivity

}