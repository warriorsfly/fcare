package com.wxsoft.fcare.ui.details.pharmacy.selectdrugs

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.pharmacy.drugrecords.DrugRecordsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SelectDrugsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SelectDrugsViewModel::class)
    abstract fun bindSelectDrugsViewModel(viewModel: SelectDrugsViewModel): ViewModel
}