package com.example.data.quotes

import com.example.common.Result
import com.example.domain.quotes.Quote

fun Result<QuoteRaw?>.toQuoteResult() : Result<Quote> {
    return when(this) {
        is Result.Success -> {
            with(this.value) {
                Result.Success(Quote(this?.id, this?.author, this?.text ?: ""))
            }
        }
        is Result.Error -> this
    }
}

fun Result<List<QuoteRaw>?>.toQuotesResult() : Result<List<Quote>> {
    return when(this) {
        is Result.Success -> {
            val quotesList = this.value?.let {
                it.map { raw ->
                    raw.toQuote()
                }
            } ?: emptyList()
            Result.Success(quotesList)
        }
        is Result.Error -> this
    }
}