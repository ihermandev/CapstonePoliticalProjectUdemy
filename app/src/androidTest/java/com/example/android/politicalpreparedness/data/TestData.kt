package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.data.network.models.Election
import java.util.*

object TestData {

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
}
