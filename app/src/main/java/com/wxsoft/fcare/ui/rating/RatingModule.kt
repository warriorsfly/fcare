package com.wxsoft.fcare.ui.rating

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import com.wxsoft.fcare.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
internal abstract class RatingModule {
    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel::class)
    abstract fun bindRatingViewModel(viewModel: RatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RatingSubjectViewModel::class)
    abstract fun bindRatingSubjectViewModel(viewModel: RatingSubjectViewModel): ViewModel

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeRatingsSheetFragment(): RatingsSheetFragment
}