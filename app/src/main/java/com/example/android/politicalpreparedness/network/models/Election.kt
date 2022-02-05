package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.example.android.politicalpreparedness.domain.ElectionDomain
import com.example.android.politicalpreparedness.util.Const.ELECTION_TABLE_NAME
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = ELECTION_TABLE_NAME)
data class Election(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "electionDay") val electionDay: Date,
    @ColumnInfo(name = "isSaved") val isSaved: Boolean = false,
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division,
)

fun Election.toDomainModel(): ElectionDomain {
    return ElectionDomain(
        id = id, name = name, electionDay = electionDay, isSaved = isSaved, division = division
    )
}

fun List<Election>.toDomainModel(): List<ElectionDomain> {
    return map {
        ElectionDomain(
            id = it.id,
            name = it.name,
            electionDay = it.electionDay,
            isSaved = it.isSaved,
            division = it.division
        )
    }
}
