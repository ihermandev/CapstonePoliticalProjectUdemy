package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.data.Result

interface ElectionNetworkDataSource {

    suspend fun getElections(): Result<List<Election>>

    suspend fun getVoterInfo(
        address: String,
        electionId: Int,
    ): Result<VoterInfoResponse>

    suspend fun getRepresentatives(
        address: String,
    ): Result<RepresentativeResponse>
}
