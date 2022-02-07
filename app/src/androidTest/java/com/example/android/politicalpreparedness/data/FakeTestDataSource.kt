package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.data.local.ElectionLocalDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import java.lang.Exception

class FakeTestDataSource(var elections: MutableList<Election>? = mutableListOf()) :
    ElectionLocalDataSource {
    override suspend fun saveElection(election: Election) {
        elections?.add(election)
    }

    override suspend fun saveElections(elections: List<Election>) {
        this.elections?.addAll(elections)
    }

    override fun getElections(): LiveData<List<Election>> {
        val liveData = MutableLiveData<List<Election>>()
        liveData.value = elections
        return liveData
    }

    override suspend fun getSavedElections(): Result<List<Election>> {
        elections?.filter { it.isSaved }?.let { return Result.Success(it) }
        return Result.Error(Exception("Elections not found"))
    }

    override suspend fun getElectionById(id: Int): Result<Election> {
        elections?.firstOrNull { it.id == id }?.let { return Result.Success(it) }
        return Result.Error(Exception("Election not found"))
    }

    override suspend fun updateElectionState(id: Int, isSaved: Boolean) {
        elections?.firstOrNull { it.id == id }?.let { it.copy(isSaved = isSaved) }
    }

    override suspend fun deleteElection(election: Election) {
        elections?.remove(election)
    }

    override suspend fun deleteById(id: Int) {
        elections?.removeIf { item -> item.id == id }
    }


    override suspend fun deleteAllData() {
        elections?.clear()
    }
}
