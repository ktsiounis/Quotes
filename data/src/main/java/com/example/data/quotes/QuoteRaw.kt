package com.example.data.quotes

import com.example.domain.quotes.Quote

data class QuoteRaw(val id: String?, val author: String?, val text: String) {
    fun toQuote() : Quote = Quote(
        id,
        author,
        text
    )
}
