package com.wxsoft.fcare.ui.details.dominating

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.ViewPoolModule
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
    abstract fun contributeProcessStartFragment(): ProcessFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeProcessReturningFragment(): GisFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeTaskSheetFragment(): TaskSheetFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributePatientManagerFragment(): PatientManagerFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector(modules = [ViewPoolModule::class])
    abstract fun contributeEmrFragment(): EmrFragment
}