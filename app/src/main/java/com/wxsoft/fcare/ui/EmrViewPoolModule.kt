package com.wxsoft.fcare.ui

import android.support.v7.widget.RecyclerView
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class EmrViewPoolModule {

    @ChildFragmentScoped
    @Provides
    @Named("emrViewPool")
    fun providesEmrViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    @ChildFragmentScoped
    @Provides
    @Named("emrItemViewPool")
    fun providesEmrItemViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
}