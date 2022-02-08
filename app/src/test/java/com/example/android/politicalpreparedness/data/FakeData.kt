package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.*
import java.util.*

object FakeData {
    val testElection1 = Election(id = 1,
        name = "TITLE",
        electionDay = Date(),
        isSaved = false,
        division = Division("5", "US", "AL"))

    val testElection2 = Election(id = 2,
        name = "TITLE",
        electionDay = Date(),
        isSaved = true,
        division = Division("5", "US", "AK"))

    val fakeAddress = Address(line1 = "1500 11th Street, 5th Floor",
        line2 = "line2",
        city = "Sacramento",
        state = "US",
        zip = "123"
    )

    fun fakeVoterInfoResponse() = VoterInfoResponse(election =
    Election(id = 7168,
        name = "Washington Special Election",
        electionDay = Date(),
        division = Division(id = "ocd-division", country = "us", state = "wa")),
        state = listOf(State(
            name = "Secretary of State",
            electionAdministrationBody = AdministrationBody(
                name = "Administration name",
                electionInfoUrl = "http://www.sos.ca.gov/elections/",
                votingLocationFinderUrl = "https://voterstatus.sos.ca.gov",
                ballotInfoUrl = "https://www.sos.ca.gov/elections/ballot-status/wheres-my-ballot/",
                correspondenceAddress = Address(
                    line1 = "1500 11th Street, 5th Floor",
                    line2 = "line 2",
                    city = "Sacramento",
                    state = "California",
                    zip = "95814"
                )
            )
        )
        )
    )

    fun fakeRepresentativeResponse() = RepresentativeResponse(offices = listOf(
        Office("Test President of the United States",
            Division("1", "US", "California"),
            listOf(1, 1)),
        Office("Test Vice President of the United States",
            Division("2", "US", "California"),
            listOf(1, 1))
    ), officials = listOf(
        Official("Test Joseph R. Biden",
            emptyList(),
            "Test Party",
            listOf("111 111 111", "1111111"),
            listOf("https://www.whitehouse.gov/"),
            null,
            null),
        Official("Test Kamala D. Harris",
            emptyList(),
            "Test Party",
            listOf("111 111 111", "1111111"),
            listOf("https://www.google.com"),
            null,
            null)


    ))
}
