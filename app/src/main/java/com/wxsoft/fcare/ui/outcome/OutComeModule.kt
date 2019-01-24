package com.wxsoft.fcare.ui.outcome
import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class OutComeModule {
    @Binds
    @IntoMap
    @ViewModelKey(OutComeViewModel::class)
    abstract fun bindOutComeViewModel(viewModel: OutComeViewModel): ViewModel

}