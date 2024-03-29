package com.cs4520.assignment5.common

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Empty(val exception: Exception) : Result<Nothing>()
}
