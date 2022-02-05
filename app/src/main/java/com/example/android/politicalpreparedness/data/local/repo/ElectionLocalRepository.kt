package com.example.android.politicalpreparedness.data.local.repo

import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.android.politicalpreparedness.data.local.Result
import java.lang.Exception

class ElectionLocalRepository(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ElectionDataSource {

    override suspend fun saveElection(election: Election) = withContext(ioDispatcher) {
        electionDao.saveElection(election)
    }

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(electionDao.getElections())
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> = withContext(ioDispatcher) {
        try {
            val election = electionDao.getElectionById(id)
            if (election != null) {
                return@withContext Result.Success(election)
            } else {
                return@withContext Result.Error("Election not found!")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }


    override suspend fun deleteElection(election: Election) {
        withContext(ioDispatcher) {
            electionDao.deleteElection(election)
        }
    }

    override suspend fun deleteById(id: Int) {
        withContext(ioDispatcher) {
            electionDao.deleteById(id)
        }
    }

    override suspend fun deleteAllData() {
        withContext(ioDispatcher) {
            electionDao.deleteAllData()
        }
    }
}
