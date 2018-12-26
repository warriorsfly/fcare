package com.wxsoft.fcare.data

import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.exception.ServerException
import com.wxsoft.fcare.result.Resource
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

inline fun <reified T:Any> Flowable<T>.toResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .startWith(Resource.Loading)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!



@Deprecated(message = "接口返回的数据可能为null，这个不是很匹配")
inline fun <reified T:Any> Flowable<Response<T>>.toWxResource( )
        =
    this.map<Resource<T>>
    {
        if (it.success) {
            if (it.result != null) {
                Resource.Success(it.result!!)
            } else {
                Resource.Error(ServerException(it.msg))
            }
        } else
            Resource.Error(ServerException(it.msg))
    }
        .onErrorReturn { Resource.Error(it) }
        .startWith(Resource.Loading)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!

inline fun <reified T:Any> Maybe<T>.toResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!

inline fun <reified T:Any> Maybe<T>.toMaybeResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object :DisposableMaybeObserver<Resource<T>>(){
            override fun onSuccess(t: Resource<T>) {

            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }


        })


inline fun <reified T:Any> Single<T>.toResource( )
        =
    this.map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { Resource.Error(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())!!