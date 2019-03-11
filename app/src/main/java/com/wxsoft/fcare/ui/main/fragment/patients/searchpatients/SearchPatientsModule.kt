package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SearchPatientsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchPatientsViewModel::class)
    abstract fun bindSearchPatientsViewModel(viewModel: SearchPatientsViewModel): ViewModel

}