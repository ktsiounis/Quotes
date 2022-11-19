package com.example.quotes.domain.quotes

import com.example.quotes.data.quotes.Quote

class QuotesUseCase(
    private val repository: QuotesRepositoryContract
) {

    suspend fun getQuotes() = repository.getQuotes()

    suspend fun getQuote(id: String) = repository.getQuote(id)

    suspend fun getRandomQuote() = repository.getRandomQuote()

    suspend fun createQuote(quote: Quote) = repository.createQuote(quote)

    suspend fun updateQuote(id: String, quote: Quote) = repository.updateQuote(id, quote)

    suspend fun deleteQuote(id: String) = repository.deleteQuote(id)

}