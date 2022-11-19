package com.example.quotes

import android.app.Application
import com.example.quotes.di.dataModule
import com.example.quotes.di.domainModule
import com.example.quotes.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(domainModule, dataModule, presentationModule)
        }

    }

}