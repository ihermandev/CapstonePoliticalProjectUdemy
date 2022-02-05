package com.example.android.politicalpreparedness.domain

import android.os.Parcelable
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ElectionDomain(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division,
    val isSaved: Boolean,
) : Parcelable

fun ElectionDomain.toDataBaseModel(): Election {
    return Election(
        id = id, name = name, electionDay = electionDay, isSaved = isSaved, division = division
    )
}

fun List<ElectionDomain>.toDataBaseModel(): List<Election> {
    return map {
        Election(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            isSaved = it.isSaved,
            division = it.division
        )
    }
}
