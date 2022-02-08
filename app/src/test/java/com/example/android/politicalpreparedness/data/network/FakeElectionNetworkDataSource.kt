package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.FakeData.fakeRepresentativeResponse
import com.example.android.politicalpreparedness.data.FakeData.fakeVoterInfoResponse
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse

class FakeElectionNetworkDataSource(val elections: MutableList<Election>? = mutableListOf()) :
    ElectionNetworkDataSource {

    var shouldReturnError = false

    override suspend fun getElections(): Result<List<Election>> {
        elections?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Elections not found"))
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> {
        if (shouldReturnError) {
            return Result.Error(Exception("Voter info not found"))
        }
        return Result.Success(fakeVoterInfoResponse())
    }

    override suspend fun getRepresentatives(address: Address): Result<RepresentativeResponse> {
        if (shouldReturnError) {
            return Result.Error(Exception("Representatives not found"))
        }
        return Result.Success(fakeRepresentativeResponse())
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}
