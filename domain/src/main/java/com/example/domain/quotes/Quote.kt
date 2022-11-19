package com.example.domain.quotes

import java.io.Serializable

data class Quote(val id: String?, val author: String?, val text: String) : Serializable
