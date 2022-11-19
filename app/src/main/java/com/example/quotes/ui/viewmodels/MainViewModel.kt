package com.example.quotes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotes.data.quotes.Quote
import com.example.quotes.domain.quotes.QuotesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.quotes.data.Result

class MainViewModel(
    private val quotesUseCase: QuotesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuotesUiState>(QuotesUiState.Success(emptyList()))
    val uiState: StateFlow<QuotesUiState> = _uiState

    private val _uiActions = MutableSharedFlow<QuotesUiAction>()
    val uiActions: SharedFlow<QuotesUiAction> = _uiActions

    private val generalErrorMessage = "Something unexpected happen"

    fun refreshQuotes() {
        getQuotes()
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            quotesUseCase
                .getRandomQuote()
                .collect {
                    when(it) {
                        is Result.Success -> _uiActions.emit(QuotesUiAction.ShowRandomQuote(it.value))
                        is Result.Error -> _uiState.value = QuotesUiState.Error(it.message ?: generalErrorMessage)
                    }
            }
        }
    }

    fun deleteQuote(quoteId: String) {
        viewModelScope.launch {
            quotesUseCase
                .deleteQuote(quoteId)
                .collect {
                    when(it) {
                        is Result.Success -> _uiActions.emit(QuotesUiAction.CloseQuoteDetails("Quote deleted successfully"))
                        is Result.Error -> _uiState.value = QuotesUiState.Error(it.message ?: generalErrorMessage)
                    }
                }

        }
    }

    fun addQuote(quote: Quote) {
        viewModelScope.launch {
            quotesUseCase
                .createQuote(quote)
                .collect {
                    when(it) {
                        is Result.Success -> getQuotes()
                        is Result.Error -> _uiState.value = QuotesUiState.Error(it.message ?: generalErrorMessage)
                    }
                }
        }
    }

    fun updateQuote(quote: Quote) {
        viewModelScope.launch {
            quotesUseCase
                .updateQuote(quote.id ?: "", quote)
                .collect {
                    when(it) {
                        is Result.Success -> _uiActions.emit(QuotesUiAction.CloseQuoteDetails("Quote updated successfully"))
                        is Result.Error -> _uiState.value = QuotesUiState.Error(it.message ?: generalErrorMessage)
                    }
                }
        }
    }

    private fun getQuotes() {
        viewModelScope.launch {
            quotesUseCase
                .getQuotes()
                .collect {
                    when(it) {
                        is Result.Success -> {
                            it.value?.let { quotes ->
                                _uiState.value = QuotesUiState.Success(quotes)
                            } ?: run {
                                _uiState.value = QuotesUiState.Error(generalErrorMessage)
                            }
                        }
                        is Result.Error -> _uiState.value = QuotesUiState.Error(it.message ?: generalErrorMessage)
                    }
                }
        }
    }

}

sealed interface QuotesUiState {
    data class Success(val quotes: List<Quote>) : QuotesUiState
    data class Error(val message: String) : QuotesUiState
}

sealed interface QuotesUiAction {
    data class ShowRandomQuote(val quote: Quote?) : QuotesUiAction
    data class CloseQuoteDetails(val message: String) : QuotesUiAction
}