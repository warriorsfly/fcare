package com.wxsoft.fcare.ui.launch

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class LaunchModule {

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindLauncherViewModel(viewModel: LauncherViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUpgradeDialog(): UpgradeDialog

}