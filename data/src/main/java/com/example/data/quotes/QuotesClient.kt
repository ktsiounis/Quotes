package com.example.data.quotes

import com.example.network.ApiProvider
import com.example.network.utils.toResult

class QuotesClient(
    private val apiProvider: ApiProvider<QuotesApi>
) {

    suspend fun getQuotes() = apiProvider.api().getAllQuotes().toResult()

    suspend fun getQuote(id: String) = apiProvider.api().getQuote(id).toResult()

    suspend fun getRandomQuote() = apiProvider.api().getRandomQuote().toResult()

    suspend fun createQuote(quote: QuoteRaw) = apiProvider.api().createQuote(quote).toResult()

    suspend fun updateQuote(id: String, quote: QuoteRaw) = apiProvider.api().updateQuote(id, quote).toResult()

    suspend fun deleteQuote(id: String) = apiProvider.api().deleteQuote(id).toResult()

}