package com.wxsoft.fcare.ui.details.trajectory

import androidx.lifecycle.ViewModel
import com.wxsoft.fcare.core.di.ViewModelKey
import com.wxsoft.fcare.ui.details.trajectory.TrajectoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class TrajectoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(TrajectoryViewModel::class)
    abstract fun bindTrajectoryViewModel(viewModel: TrajectoryViewModel): ViewModel

}