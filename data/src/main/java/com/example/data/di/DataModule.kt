package com.example.quotes.di

import android.content.Context.MODE_PRIVATE
import com.example.data.quotes.QuotesApi
import com.example.data.quotes.QuotesClient
import com.example.data.quotes.QuotesRepository
import com.example.domain.quotes.QuotesRepositoryContract
import com.example.network.ApiProvider
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.Calendar

val dataModule = module {

    single { Calendar.getInstance() }
    single { Gson() }
    single { androidApplication().getSharedPreferences("Quotes", MODE_PRIVATE) }
    factory { QuotesClient(apiProvider = ApiProvider(QuotesApi::class.java)) }
    factoryOf(::QuotesRepository) bind QuotesRepositoryContract::class

}