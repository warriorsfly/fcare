package com.wxsoft.fcare.ui.workspace
import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.emr.EmrViewModel
import com.wxsoft.fcare.ui.emr.ProfileViewModel
import com.wxsoft.fcare.ui.workspace.addpoint.AddTimeLinePointViewModel
import com.wxsoft.fcare.ui.workspace.notify.NotifyFragment
import com.wxsoft.fcare.ui.workspace.notify.OneTouchCallingViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class WorkSpaceModule {
    @Binds
    @IntoMap
    @ViewModelKey(WorkingViewModel::class)
    abstract fun bindWorkingViewModel(viewModel: WorkingViewModel): ViewModel

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
    @ViewModelKey(TimePointViewModel::class)
    abstract fun bindTimePointViewModel(viewModel: TimePointViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmrViewModel::class)
    abstract fun bindEmrViewModel(viewModel: EmrViewModel): ViewModel


    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeWorkingEmrFragment(): WorkingEmrFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeNotifyFragment(): NotifyFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeOtherOperationFragment(): OtherOperationFragment
}