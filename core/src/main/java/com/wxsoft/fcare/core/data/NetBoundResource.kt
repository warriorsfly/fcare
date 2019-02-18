package com.wxsoft.fcare.core.data

import com.wxsoft.fcare.core.result.Resource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


abstract class NetFlowableOneResource<T>: NetFlowableResource<T, T>()

abstract class NetFlowableResource<ResultType, RequestType> {

    private val result: Flowable<Resource<ResultType>>

    init {
        // Lazy disk observable.
        val roomObservable = Flowable.defer {
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()

                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .doOnNext {
                    saveCallResult(processResponse(it))
                }
                .onErrorReturn {
                    throw it
                }
                .flatMap { loadFromDb() }
        }

        result = when {
            shouldFetch() -> networkObservable
                .map<Resource<ResultType>> { Resource.Success(it) }
                .onErrorReturn { Resource.Error(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
            else -> roomObservable
                .map<Resource<ResultType>> { Resource.Success(it) }
                .onErrorReturn { Resource.Error(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
        }
    }

    val data: Flowable<Resource<ResultType>>
    get() { return result}

    private fun processResponse(response: RequestType): RequestType {
        return response
    }

    protected abstract fun saveCallResult(request: RequestType)

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<RequestType>

    protected abstract fun shouldFetch(): Boolean
}