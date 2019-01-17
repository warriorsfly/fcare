package com.wxsoft.fcare.core.domain.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

data class Listing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<Boolean>,
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
//    val refreshState: LiveData<Boolean>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit)