package com.example.quotes.ui.viewmodels

import app.cash.turbine.test
import com.example.quotes.data.quotes.Quote
import com.example.quotes.data.utils.successOf
import com.example.quotes.domain.quotes.QuotesUseCase
import com.example.quotes.runTestWithDispatcher
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    private val quotesUseCaseMock = mockk<QuotesUseCase>()
    private val testedClass = MainViewModel(quotesUseCaseMock)
    private val quote = Quote("", "John Lennon", "Life is what happens when you’re busy making other plans.")
    private val quote2 = quote.copy(author = "Coello")
    private val quotes = listOf(quote, quote2)

    @Test
    fun `when successfully refresh quote, state includes new quotes`() = runTestWithDispatcher {
        testedClass.uiState.test {
            coEvery { quotesUseCaseMock.getQuotes() } returns flowOf(successOf(quotes))

            testedClass.refreshQuotes()

            skipItems(1)
            awaitItem() shouldBe QuotesUiState.Success(quotes)
            expectNoEvents()
        }
    }

    @Test
    fun `when random quote is retrieved successfully, action to show it is emitted`() = runTestWithDispatcher {
        testedClass.uiActions.test {
            coEvery { quotesUseCaseMock.getRandomQuote() } returns flowOf(successOf(quote))

            testedClass.getRandomQuote()

            awaitItem() shouldBe QuotesUiAction.ShowRandomQuote(quote)
            expectNoEvents()
        }
    }

    @Test
    fun `when quote is deleted successfully, action to close details is emitted`() = runTestWithDispatcher {
        testedClass.uiActions.test {
            coEvery { quotesUseCaseMock.deleteQuote(any()) } returns flowOf(successOf(Unit))

            testedClass.deleteQuote("")

            awaitItem() shouldBe QuotesUiAction.CloseQuoteDetails("Quote deleted successfully")
            expectNoEvents()
        }
    }

    @Test
    fun `when new quote is successfully added, quotes list is refreshed`() = runTestWithDispatcher {
        testedClass.uiState.test {
            coEvery { quotesUseCaseMock.createQuote(quote2) } returns flowOf(successOf(Unit))
            coEvery { quotesUseCaseMock.getQuotes() } returns flowOf(successOf(quotes))

            testedClass.addQuote(quote2)

            skipItems(1)
            awaitItem() shouldBe QuotesUiState.Success(quotes)
            expectNoEvents()
        }
    }

    @Test
    fun `when quote is updated successfully, action to close details is emitted`() = runTestWithDispatcher {
        testedClass.uiActions.test {
            coEvery { quotesUseCaseMock.updateQuote(any(), any()) } returns flowOf(successOf(Unit))

            testedClass.updateQuote(quote)

            awaitItem() shouldBe QuotesUiAction.CloseQuoteDetails("Quote updated successfully")
            expectNoEvents()
        }
    }

}