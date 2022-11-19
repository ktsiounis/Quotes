package com.example.data.quotes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface QuotesApi {

    companion object {
        private const val QUOTES_URL = "quotes"
        private const val QUOTE_URL = "quotes/{id}"
        private const val RANDOM_QUOTE_URL = "quotes/random"
    }

    @GET(QUOTES_URL)
    fun getAllQuotes() : Call<List<QuoteRaw>>

    @GET(QUOTE_URL)
    fun getQuote(@Path("id") quoteId: String) : Call<QuoteRaw>

    @GET(RANDOM_QUOTE_URL)
    fun getRandomQuote() : Call<QuoteRaw>

    @POST(QUOTES_URL)
    fun createQuote(@Body quote: QuoteRaw) : Call<Unit>

    @PUT(QUOTE_URL)
    fun updateQuote(@Path("id") quoteId: String, @Body quote: QuoteRaw) : Call<Unit>

    @DELETE(QUOTE_URL)
    fun deleteQuote(@Path("id") quoteId: String) : Call<Unit>

}