package com.wxsoft.fcare.ui.patient

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfileModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PatientEmrViewModel::class)
    abstract fun bindPatientEmrViewModel(viewModel: PatientEmrViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TimeLineViewModel::class)
    abstract fun bindTimeLineViewModel(viewModel: TimeLineViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeTimeLineFragment(): TimeLineFragment

}