package com.example.android.politicalpreparedness.data.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: Election,
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
//    TODO: Future Use
//    val pollingLocations: String? = null,
//    val contests: String? = null
)
