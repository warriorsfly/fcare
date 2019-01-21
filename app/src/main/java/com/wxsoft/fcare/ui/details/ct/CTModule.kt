package com.wxsoft.fcare.ui.details.ct
import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CTModule {
    @Binds
    @IntoMap
    @ViewModelKey(CTViewModel::class)
    abstract fun bindCatheterViewModel(viewModel: CTViewModel): ViewModel

}