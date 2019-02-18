package com.wxsoft.fcare.ui.details.dominating

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.EmrViewPoolModule
import com.wxsoft.fcare.ui.details.dominating.fragment.*
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrFragment
import com.wxsoft.fcare.ui.details.dominating.fragment.emr.EmrViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class DoMinaModule {

    @Binds
    @IntoMap
    @ViewModelKey(DoMinaViewModel::class)
    abstract fun bindDoMinaViewModel(viewModel: DoMinaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmrViewModel::class)
    abstract fun bindEmrViewModel(viewModel: EmrViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProcessStartFragment(): ProcessStartFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProcessArriveFragment(): ProcessArriveFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProcessReturningFragment(): ProcessReturningFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProcessArriveHosFragment(): ProcessArriveHosFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributePatientManagerFragment(): PatientManagerFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector(modules = [EmrViewPoolModule::class])
    abstract fun contributeEmrFragment(): EmrFragment
}