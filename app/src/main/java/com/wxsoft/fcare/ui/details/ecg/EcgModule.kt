package com.wxsoft.fcare.ui.details.ecg

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.ecg.fragment.EcgEditFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class EcgModule {

    @Binds
    @IntoMap
    @ViewModelKey(EcgViewModel::class)
    abstract fun bindEcgViewModel(viewModel: EcgViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeEcgEditFragment(): EcgEditFragment
}