package com.example.quotes.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.quotes.data.ApiProvider
import com.example.quotes.data.quotes.QuotesApi
import com.example.quotes.data.quotes.QuotesClient
import com.example.quotes.data.quotes.QuotesRepository
import com.example.quotes.domain.quotes.QuotesRepositoryContract
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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