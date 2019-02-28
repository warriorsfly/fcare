package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class DrugRecordsModule {
    @Binds
    @IntoMap
    @ViewModelKey(DrugRecordsViewModel::class)
    abstract fun bindDrugRecordsViewModel(viewModel: DrugRecordsViewModel): ViewModel

}