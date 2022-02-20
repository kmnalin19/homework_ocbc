package com.app.homework.domain

public sealed class Response<out T > {
    
    public final data class ErrorResponse(val error : String) : Response<Any>()

    public final data class SuccessResponse<out T >(val response: T): Response<T>()
}