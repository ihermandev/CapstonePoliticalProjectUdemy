package com.example.android.politicalpreparedness.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import okhttp3.logging.HttpLoggingInterceptor


//OkHttp interceptor
fun makeInterceptor(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(level)
}
