package com.example.quotes.data.quotes

import android.content.SharedPreferences
import android.text.format.DateUtils
import com.example.data.quotes.QuotesClient
import com.example.data.quotes.QuotesRepository
import com.example.domain.quotes.Quote
import com.example.quotes.runTestWithDispatcher
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test;
import java.util.Calendar
import com.example.common.Result
import com.example.data.quotes.QuoteRaw
import com.example.network.utils.successOf

@OptIn(ExperimentalCoroutinesApi::class)
internal class QuotesRepositoryTest {

    private val clientMock: QuotesClient = mockk()
    private val calendarMock: Calendar = mockk()
    private val sharedPreferencesMock: SharedPreferences = mockk()
    private val sharedPreferencesEditorMock: SharedPreferences.Editor = mockk(relaxed = true)
    private val gsonMock: Gson = mockk(relaxed = true)

    private val testedClass = QuotesRepository(
        clientMock,
        calendarMock,
        sharedPreferencesMock,
        gsonMock
    )

    private val quote = Quote("", "John Lennon", "Life is what happens when you’re busy making other plans.")
    private val quoteRaw = QuoteRaw("", "John Lennon", "Life is what happens when you’re busy making other plans.")

    @Test
    fun `when today's quote is stored, repository returns this one`() = runTestWithDispatcher {
        every { sharedPreferencesMock.getLong(any(), any()) } returns 1234L
        every { sharedPreferencesMock.getString(any(), any()) } returns ""
        every { gsonMock.fromJson("", Quote::class.java) } returns quote
        mockkStatic(DateUtils::class)
        every { DateUtils.isToday(1234L) } answers { true }

        val result = testedClass.getRandomQuote()

        result.collect {
            it shouldBe Result.Success(quote)
        }

        unmockkStatic(DateUtils::class)
    }

    @Test
    fun `when today's quote is not stored, repository returns new one from client`() = runTestWithDispatcher {
        every { sharedPreferencesMock.getLong(any(), any()) } returns 1234L
        every { sharedPreferencesMock.getString(any(), any()) } returns ""
        every { sharedPreferencesMock.edit() } returns sharedPreferencesEditorMock
        every { gsonMock.fromJson("", Quote::class.java) } returns null
        every { gsonMock.toJson(null) } returns ""
        mockkStatic(DateUtils::class)
        every { DateUtils.isToday(1234L) } answers { true }
        coEvery { clientMock.getRandomQuote() } returns successOf(quoteRaw)
        every { calendarMock.timeInMillis } returns 1234L

        val result = testedClass.getRandomQuote()

        result.collect {
            it shouldBe Result.Success(quote)
        }

        unmockkStatic(DateUtils::class)
    }

}