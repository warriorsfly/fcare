package com.wxsoft.fcare.ui.details.dominating

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.dominating.fragment.GisFragment
import com.wxsoft.fcare.ui.details.dominating.fragment.PatientManagerFragment
import com.wxsoft.fcare.ui.details.dominating.fragment.ProcessFragment
import com.wxsoft.fcare.ui.details.dominating.fragment.TaskSheetFragment
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

}