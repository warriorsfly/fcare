package com.wxsoft.fcare.ui.details.thrombolysis

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CatheterModule {
    @Binds
    @IntoMap
    @ViewModelKey(CatheterViewModel::class)
    abstract fun bindCatheterViewModel(viewModel: CatheterViewModel): ViewModel

}