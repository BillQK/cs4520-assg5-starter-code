package com.cs4520.assignment5.common

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()
    data class Empty(val exception: Exception) : ApiResult<Nothing>()
}
