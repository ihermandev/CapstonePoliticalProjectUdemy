package com.example.android.politicalpreparedness.data.local

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.Result

interface ElectionLocalDataSource {

    suspend fun saveElection(election: Election)

    suspend fun saveElections(elections: List<Election>)

    fun getElections(): LiveData<List<Election>>

    suspend fun getSavedElections(): Result<List<Election>>

    suspend fun getElectionById(id: Int): Result<Election>

    suspend fun updateElectionState(id: Int, isSaved: Boolean)

    suspend fun deleteElection(election: Election)

    suspend fun deleteById(id: Int)

    suspend fun deleteAllData()
}
