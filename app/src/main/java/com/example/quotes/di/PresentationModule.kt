package com.example.quotes.di

import com.example.quotes.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(::MainViewModel)

}