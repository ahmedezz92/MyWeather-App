package com.example.myweatherapp.base

sealed class BaseResult<out T> {
    data class DataState<T : Any>(val items: T?) : BaseResult<T>()
    data class ErrorState(val errorCode: Int) : BaseResult<Nothing>()
}