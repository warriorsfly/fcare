package com.wxsoft.fcare.ui

import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.di.ActivityScoped
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class ViewPoolModule {

    @ChildFragmentScoped
    @Provides
    @Named("emrViewPool")
    fun providesEmrViewPool() = RecyclerView.RecycledViewPool()

    @ChildFragmentScoped
    @Provides
    @Named("emrItemViewPool")
    fun providesEmrItemViewPool() = RecyclerView.RecycledViewPool()

    @ActivityScoped
    @Provides
    @Named("ratingResultViewPool")
    fun providesRatingResultViewPool() = RecyclerView.RecycledViewPool()

    @ActivityScoped
    @Provides
    @Named("ratingOptionViewPool")
    fun providesRatingOptionViewPool() = RecyclerView.RecycledViewPool()

    @ActivityScoped
    @Provides
    @Named("emrImageViewPool")
    fun providesEmrImageViewPool() = RecyclerView.RecycledViewPool()


    @ActivityScoped
    @Provides
    @Named("optionViewPool")
    fun providesOptionViewPool() = RecyclerView.RecycledViewPool()
}