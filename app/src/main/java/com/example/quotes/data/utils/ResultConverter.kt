package com.example.quotes.data.utils

import retrofit2.Call
import retrofit2.awaitResponse
import com.example.quotes.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Suppress("UNCHECKED_CAST")
suspend fun <R> Call<R>.toResult(): Flow<Result<R?>> = runCatchingFromSuspend {
    val response = awaitResponse()
    when {
        response.isSuccessful -> successOf(response.body())
        else -> Result.Error(message = response.message() ?: "")
    }
}
    .fold(
        onSuccess = { flowOf(it) },
        onFailure = { throwable -> flowOf(Result.Error(throwable.message, throwable)) }
    )