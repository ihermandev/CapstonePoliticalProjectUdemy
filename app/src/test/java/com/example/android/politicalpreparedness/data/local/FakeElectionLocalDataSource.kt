package com.example.android.politicalpreparedness.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election

class FakeElectionLocalDataSource(val elections: MutableList<Election>? = mutableListOf()) :
    ElectionLocalDataSource {

    var shouldReturnError = false

    override suspend fun saveElection(election: Election) {
        elections?.add(election)
    }

    override suspend fun saveElections(elections: List<Election>) {
        this.elections?.addAll(elections)
    }

    override fun getElections(): LiveData<List<Election>> {
        return MutableLiveData(elections)
    }

    override suspend fun getSavedElections(): Result<List<Election>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Saved elections not found"))
        }
        val data = elections?.filter { it.isSaved }
        if (data.isNullOrEmpty()) return Result.Error(Exception("Saved elections not found"))

        return Result.Success(data)
    }

    override suspend fun getElectionById(id: Int): Result<Election> {
        if (shouldReturnError) {
            return Result.Error(Exception("Saved elections not found"))
        }
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

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}
