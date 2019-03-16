package com.wxsoft.fcare.ui.details.checkbody.select

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SelectBodyItemsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SelectBodyItemsViewModel::class)
    abstract fun bindSelectBodyItemsViewModel(viewModel: SelectBodyItemsViewModel): ViewModel
}