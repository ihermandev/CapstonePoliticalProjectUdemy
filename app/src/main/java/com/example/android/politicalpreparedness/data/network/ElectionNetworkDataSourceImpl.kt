package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionNetworkDataSourceImpl(
    private val civicsApiService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ElectionNetworkDataSource {

    override suspend fun getElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(civicsApiService.getElections().elections)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getVoterInfo(
        address: String,
        electionId: Int,
    ): Result<VoterInfoResponse> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(civicsApiService.getVoterInfo(address = address,
                electionId = electionId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(civicsApiService.getRepresentatives(address = address))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
