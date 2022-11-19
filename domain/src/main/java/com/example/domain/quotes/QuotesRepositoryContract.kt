package com.example.domain.quotes

import kotlinx.coroutines.flow.Flow
import com.example.common.Result

interface QuotesRepositoryContract {

    suspend fun getQuotes() : Flow<Result<List<Quote>?>>

    suspend fun getQuote(id: String) : Flow<Result<Quote?>>

    suspend fun getRandomQuote() : Flow<Result<Quote?>>

    suspend fun createQuote(quote: Quote) : Flow<Result<Unit?>>

    suspend fun updateQuote(id: String, quote: Quote) : Flow<Result<Unit?>>

    suspend fun deleteQuote(id: String) : Flow<Result<Unit?>>

}