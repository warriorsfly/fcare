package com.wxsoft.fcare.ui.sign

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.sign.SelectSignDoctorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SignInModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectSignDoctorViewModel::class)
    abstract fun bindSelectSignDoctorViewModel(viewModel: SelectSignDoctorViewModel): ViewModel
}