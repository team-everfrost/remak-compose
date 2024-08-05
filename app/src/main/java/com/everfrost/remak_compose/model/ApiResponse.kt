package com.everfrost.remak_compose.model

sealed class APIResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode: String? = null
) {
    class Success<T>(data: T? = null) : APIResponse<T>(data)
    class Loading<T>(data: T? = null) : APIResponse<T>(data)
    class Error<T>(message: String, data: T? = null, errorCode: String) :
        APIResponse<T>(data, message, errorCode)

    class Empty<T>(data: T? = null) : APIResponse<T>(data)
}
