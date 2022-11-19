package com.example.quotes.data.quotes

import com.example.quotes.data.ApiProvider
import com.example.quotes.data.utils.toResult

class QuotesClient(
    private val apiProvider: ApiProvider<QuotesApi>
) {

    suspend fun getQuotes() = apiProvider.api().getAllQuotes().toResult()

    suspend fun getQuote(id: String) = apiProvider.api().getQuote(id).toResult()

    suspend fun getRandomQuote() = apiProvider.api().getRandomQuote().toResult()

    suspend fun createQuote(quote: Quote) = apiProvider.api().createQuote(quote).toResult()

    suspend fun updateQuote(id: String, quote: Quote) = apiProvider.api().updateQuote(id, quote).toResult()

    suspend fun deleteQuote(id: String) = apiProvider.api().deleteQuote(id).toResult()

}