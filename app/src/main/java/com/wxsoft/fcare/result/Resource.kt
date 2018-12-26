package com.wxsoft.fcare.result

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[liveDiagnosis=$data]"
            is Error -> "Error[exception=${throwable.message}]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Resource] is of type [Success] & holds non-null [Success.data].
 */
val Resource<*>.succeeded
    get() = this is Resource.Success && data != null
