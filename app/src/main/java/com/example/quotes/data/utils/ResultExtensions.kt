package com.example.quotes.data.utils

import com.example.quotes.data.Result

fun <T> successOf(value: T) = Result.Success(value)