package com.example.quotes.di

import com.example.domain.quotes.QuotesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::QuotesUseCase)

}