package com.example.android.politicalpreparedness.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.data.local.ElectionLocalDataSourceImpl
import com.example.android.politicalpreparedness.data.network.ElectionNetworkDataSourceImpl
import com.example.android.politicalpreparedness.data.network.models.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionRepository(
    private val api: ElectionNetworkDataSourceImpl,
    private val database: ElectionLocalDataSourceImpl,
) {

    val elections: LiveData<List<ElectionDomain>> = map(database.getElections()) {
        it?.toDomainModel()
    }

    suspend fun updateElectionsData() {
        withContext(Dispatchers.IO) {
            when (val result = api.getElections()) {
                is Result.Success -> {
                    database.saveElections(result.data)
                    Timber.i("Elections base successfully updated")
                }
                is Result.Error -> {
                    throw result.exception
                }
            }
        }
    }


    private fun handleError(message: String?) {
        Timber.e(message)
    }
}
