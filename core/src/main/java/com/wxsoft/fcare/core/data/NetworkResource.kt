package com.wxsoft.fcare.core.data

import com.wxsoft.fcare.core.result.Resource
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

inline fun <reified T:Any> Maybe<T>.toResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!

inline fun <reified T:Any> Single<T>.toResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!