package com.wxsoft.fcare.core.result

import com.wxsoft.fcare.core.data.entity.Response

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

@Deprecated(message = "接口并非restful的，使用这个不是很合适",
    level = DeprecationLevel.WARNING,
    replaceWith =ReplaceWith(expression = "com.wxsoft.fcare.core.data.entity.Response"))
sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[item=$data]"
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
