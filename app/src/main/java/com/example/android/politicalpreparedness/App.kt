package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.network.CivicsApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        val mainModule = module {
            single { CivicsApi.createCivicRetrofitService() }
        }

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(listOf(mainModule))
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}