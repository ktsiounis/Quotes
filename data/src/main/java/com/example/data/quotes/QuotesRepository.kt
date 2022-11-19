package com.example.data.quotes

import android.content.SharedPreferences
import android.text.format.DateUtils
import com.example.domain.quotes.Quote
import com.example.domain.quotes.QuotesRepositoryContract
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import com.example.common.Result

class QuotesRepository(
    private val client: QuotesClient,
    private val calendarInstance: Calendar,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : QuotesRepositoryContract {

    companion object {
        const val todaysQuoteKey = "todaysQuoteKey"
        const val todaysQuoteDateKey = "todaysQuoteDateKey"
    }

    override suspend fun getQuotes() = flowOf(
        client.getQuotes().toQuotesResult()
    )

    override suspend fun getQuote(id: String) = flowOf(
        client.getQuote(id).toQuoteResult()
    )

    override suspend fun getRandomQuote(): Flow<Result<Quote?>> {
        val quoteDate = sharedPreferences.getLong(todaysQuoteDateKey, 0)
        val quoteString = sharedPreferences.getString(todaysQuoteKey, "")
        val quote: Quote? = gson.fromJson(quoteString, Quote::class.java)

        return if(quote != null && quoteDate != 0L && DateUtils.isToday(quoteDate)) {
            flowOf(Result.Success(quote))
        } else {
            flowOf(client.getRandomQuote().toQuoteResult()).also {
                it.collect { result ->
                    when(result) {
                        is Result.Success -> {
                            sharedPreferences
                                .edit()
                                .putString(todaysQuoteKey, gson.toJson(quote))
                                .putLong(todaysQuoteDateKey, calendarInstance.timeInMillis)
                                .apply()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override suspend fun createQuote(quote: Quote) = flowOf(
        client.createQuote(
            QuoteRaw(quote.id, quote.author, quote.text)
        )
    )

    override suspend fun updateQuote(id: String, quote: Quote) = flowOf(
        client.updateQuote(
            id,
            QuoteRaw(quote.id, quote.author, quote.text)
        )
    )

    override suspend fun deleteQuote(id: String) = flowOf(
        client.deleteQuote(id)
    )

}