package com.wxsoft.fcare.ui.emr
import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.emr.EmrPageViewModel
import com.wxsoft.fcare.ui.emr.EmrViewModel
import com.wxsoft.fcare.ui.emr.ProfileViewModel
import com.wxsoft.fcare.ui.emr.fragments.EmrFragment
import com.wxsoft.fcare.ui.emr.fragments.ProfileFragment
import com.wxsoft.fcare.ui.workspace.addpoint.AddTimeLinePointViewModel
import com.wxsoft.fcare.ui.workspace.notify.NotifyFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class EmrModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTimeLinePointViewModel::class)
    abstract fun bindAddTimeLinePointViewModel(viewModel: AddTimeLinePointViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmrViewModel::class)
    abstract fun bindEmrViewModel(viewModel: EmrViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmrPageViewModel::class)
    abstract fun bindEmrPageViewModel(viewModel: EmrPageViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEmrFragment(): EmrFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeProfileFragment(): ProfileFragment
}