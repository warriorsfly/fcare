package com.wxsoft.fcare.ui.income

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InComeModule {

    @IntoMap
    @Binds
    @ViewModelKey(InComeViewModel::class)
    abstract fun bindInComeViewModel(viewModel: InComeViewModel):ViewModel

}