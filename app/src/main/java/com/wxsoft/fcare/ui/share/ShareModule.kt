package com.wxsoft.fcare.ui.share

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ShareModule {
    @Binds
    @IntoMap
    @ViewModelKey(ShareViewModel::class)
    abstract fun bindShareViewModel(viewModel: ShareViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShareItemListViewModel::class)
    abstract fun bindShareItemListViewModel(viewModel: ShareItemListViewModel): ViewModel
}