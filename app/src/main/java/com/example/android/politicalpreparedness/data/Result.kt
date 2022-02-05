package com.example.android.politicalpreparedness.data

sealed class Result<out T : Any> {
    class Loading<T>() : Result<Any>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success
