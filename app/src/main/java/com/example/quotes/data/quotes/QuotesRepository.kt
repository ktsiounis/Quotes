package com.example.quotes.data.quotes

import android.content.SharedPreferences
import android.text.format.DateUtils
import com.example.quotes.data.Result
import com.example.quotes.domain.quotes.QuotesRepositoryContract
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar

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

    override suspend fun getQuotes() = client.getQuotes()

    override suspend fun getQuote(id: String) = client.getQuote(id)

    override suspend fun getRandomQuote(): Flow<Result<Quote?>> {
        val quoteDate = sharedPreferences.getLong(todaysQuoteDateKey, 0)
        val quoteString = sharedPreferences.getString(todaysQuoteKey, "")
        val quote: Quote? = gson.fromJson(quoteString, Quote::class.java)

        return if(quote != null && quoteDate != 0L && DateUtils.isToday(quoteDate)) {
            flowOf(Result.Success(quote))
        } else {
            client.getRandomQuote().also {
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

    override suspend fun createQuote(quote: Quote) = client.createQuote(quote)

    override suspend fun updateQuote(id: String, quote: Quote) = client.updateQuote(id, quote)

    override suspend fun deleteQuote(id: String) = client.deleteQuote(id)

}