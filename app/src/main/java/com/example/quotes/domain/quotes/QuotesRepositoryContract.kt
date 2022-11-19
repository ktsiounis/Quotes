package com.example.quotes.domain.quotes

import com.example.quotes.data.quotes.Quote
import com.example.quotes.data.Result
import kotlinx.coroutines.flow.Flow

interface QuotesRepositoryContract {

    suspend fun getQuotes() : Flow<Result<List<Quote>?>>

    suspend fun getQuote(String: String) : Flow<Result<Quote?>>

    suspend fun getRandomQuote() : Flow<Result<Quote?>>

    suspend fun createQuote(quote: Quote) : Flow<Result<Unit?>>

    suspend fun updateQuote(id: String, quote: Quote) : Flow<Result<Unit?>>

    suspend fun deleteQuote(id: String) : Flow<Result<Unit?>>

}