package com.wxsoft.fcare.ui.workspace
import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
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


    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeWorkingEmrFragment(): WorkingEmrFragment
}