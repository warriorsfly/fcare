package com.wxsoft.fcare.ui

import androidx.recyclerview.widget.RecyclerView
import com.wxsoft.fcare.core.di.ChildFragmentScoped
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class EmrViewPoolModule {

    @ChildFragmentScoped
    @Provides
    @Named("emrViewPool")
    fun providesEmrViewPool(): androidx.recyclerview.widget.RecyclerView.RecycledViewPool = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()

    @ChildFragmentScoped
    @Provides
    @Named("emrItemViewPool")
    fun providesEmrItemViewPool(): androidx.recyclerview.widget.RecyclerView.RecycledViewPool = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()
}