package com.example.android.politicalpreparedness.data.local.repo

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.local.Result

interface ElectionDataSource {
    suspend fun saveElection(election: Election)
    suspend fun getElections(): Result<List<Election>>
    suspend fun getElectionById(id: Int): Result<Election>
    suspend fun deleteElection(election: Election)
    suspend fun deleteById(id: Int)
    suspend fun deleteAllData()
}
