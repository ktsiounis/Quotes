package com.example.domain.quotes

import kotlinx.coroutines.flow.map

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