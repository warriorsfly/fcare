package com.wxsoft.fcare.ui.details.blood
import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class BloodModule {
    @Binds
    @IntoMap
    @ViewModelKey(BloodViewModel::class)
    abstract fun bindCatheterViewModel(viewModel: BloodViewModel): ViewModel

}