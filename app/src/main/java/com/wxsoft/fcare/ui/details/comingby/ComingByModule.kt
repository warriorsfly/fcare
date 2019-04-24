package com.wxsoft.fcare.ui.details.comingby

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ComingByModule {
    @Binds
    @IntoMap
    @ViewModelKey(ComingByViewModel::class)
    abstract fun bindComingByViewModel(viewModel: ComingByViewModel): ViewModel
}