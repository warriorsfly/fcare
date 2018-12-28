package com.wxsoft.fcare.ui.details.dominating

import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DoMinaModule {

    @Binds
    @IntoMap
    @ViewModelKey(DoMinaViewModel::class)
    abstract fun bindDoMinaViewModel(viewModel: DoMinaViewModel): ViewModel


}