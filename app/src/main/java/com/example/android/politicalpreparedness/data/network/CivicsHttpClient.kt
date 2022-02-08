package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.util.Const.apiKey
import com.example.android.politicalpreparedness.util.Const.keyQueryParameter
import com.example.android.politicalpreparedness.util.makeInterceptor
import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor(makeInterceptor())
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url
                        .newBuilder()
                        .addQueryParameter(keyQueryParameter, apiKey)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .build()
        }
    }
}
