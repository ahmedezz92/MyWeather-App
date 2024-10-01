package com.example.myweatherapp.core.data.utils

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T>(
    var code: Int,
)

data class WrappedErrorResponse(
    @SerializedName("error") var errorResponse: ErrorResponse,
)

data class ErrorResponse(
    val code: Int,
    val message: String
)