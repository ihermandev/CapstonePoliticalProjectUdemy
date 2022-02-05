package com.example.android.politicalpreparedness.util

import okhttp3.logging.HttpLoggingInterceptor

fun makeInterceptor(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(level)
}
