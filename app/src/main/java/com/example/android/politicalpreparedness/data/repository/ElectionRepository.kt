package com.example.android.politicalpreparedness.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.data.local.ElectionLocalDataSourceImpl
import com.example.android.politicalpreparedness.data.network.ElectionNetworkDataSourceImpl
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.data.network.models.toDomainModel
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionRepository(
    private val api: ElectionNetworkDataSourceImpl,
    private val database: ElectionLocalDataSourceImpl,
) {

    //Elections
    val elections: LiveData<List<ElectionDomain>> = map(database.getElections()) {
        it?.toDomainModel()
    }

    val savedElections: LiveData<List<ElectionDomain>> = map(database.getElections()) {
        it.filter { election -> election.isSaved }.toDomainModel()

    }

    suspend fun forceUpdateElectionsData() = withContext(Dispatchers.IO) {
        when (val result = api.getElections()) {
            is Result.Success -> {
                database.deleteAllData()
                database.saveElections(result.data)
                Timber.i("Elections base successfully saved")
            }
            is Result.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun updateElectionsData() = withContext(Dispatchers.IO) {
        when (val result = api.getElections()) {
            is Result.Success -> {
                when (val localResult = database.getSavedElections()) {
                    is Result.Success -> {
                        val filteredData = result.data.map { item ->
                            item.copy(isSaved = localResult.data.firstOrNull { db -> db.id == item.id }?.isSaved == true)
                        }
                        database.deleteAllData()
                        database.saveElections(filteredData)
                        Timber.i("Elections base successfully updated")
                    }
                    is Result.Error -> {
                        database.deleteAllData()
                        database.saveElections(result.data)
                        Timber.i(localResult.exception.localizedMessage)
                        Timber.i("Elections base successfully saved")
                    }
                }
            }
            is Result.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun getElectionById(id: Int): ElectionDomain {
        return when (val result = database.getElectionById(id)) {
            is Result.Success -> result.data.toDomainModel()
            is Result.Error -> throw result.exception
        }
    }

    suspend fun updateElectionState(electionID: Int, isSaved: Boolean) {
        database.updateElectionState(id = electionID, isSaved = isSaved)
    }


    //Voter Info
    suspend fun getVoterInfo(electionId: Int, address: String): VoterInfoResponse {
        return when (val result = api.getVoterInfo(address = address, electionId = electionId)) {
            is Result.Success -> result.data
            is Result.Error -> throw result.exception
        }
    }

    //Representatives
    suspend fun searchRepresentatives(address: Address): List<Representative> {
        return when (val result = api.getRepresentatives(address)) {
            is Result.Success -> {
                val officials = result.data.officials
                val offices = result.data.offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }
                offices
            }
            is Result.Error -> throw result.exception
        }
    }
}

