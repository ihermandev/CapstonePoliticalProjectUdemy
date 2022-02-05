package com.example.android.politicalpreparedness.data.local.database

import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.util.Const.ELECTION_TABLE_NAME

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    @Query("SELECT * FROM $ELECTION_TABLE_NAME ORDER BY electionDay ASC")
    suspend fun getElections(): List<Election>

    @Query("SELECT * FROM $ELECTION_TABLE_NAME where id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM $ELECTION_TABLE_NAME")
    suspend fun deleteAllData()

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteById(id: Int)

}
