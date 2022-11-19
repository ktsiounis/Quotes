package com.example.quotes.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiProvider<API>(
    private val apiContract: Class<API>
) {

    companion object {
        const val BASE_URL = "http://10.0.2.2:3001/api/"
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    private fun getRetrofit(): Retrofit = with(Retrofit.Builder()) {
        baseUrl(BASE_URL)
        client(client)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    fun api(): API {
        return getRetrofit().create(apiContract)
    }

}