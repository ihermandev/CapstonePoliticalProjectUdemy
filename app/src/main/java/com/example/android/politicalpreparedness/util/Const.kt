package com.example.android.politicalpreparedness.util

import com.example.android.politicalpreparedness.BuildConfig

object Const {
    // Database
    const val ELECTION_DATABASE_NAME = "election_database"
    const val ELECTION_TABLE_NAME = "election_table"

    //API
    const val apiKey = BuildConfig.API_KEY
    const val keyQueryParameter = "key"
    const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"
    const val GET_ELECTIONS = "elections"
    const val GET_VOTERINFO = "voterinfo"
    const val GET_REPRESENTATIVES = "representatives"
}
