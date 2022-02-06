package com.example.android.politicalpreparedness.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.util.Const.ELECTION_TABLE_NAME

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElections(elections: List<Election>)

    @Query("SELECT * FROM $ELECTION_TABLE_NAME ORDER BY electionDay ASC")
    fun getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM $ELECTION_TABLE_NAME  where isSaved = 1")
    suspend fun getSavedElections(): List<Election>

    @Query("SELECT * FROM $ELECTION_TABLE_NAME where id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("UPDATE $ELECTION_TABLE_NAME SET isSaved  = :isSaved WHERE id = :id")
    suspend fun updateElectionState(id: Int, isSaved: Boolean)

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM $ELECTION_TABLE_NAME")
    suspend fun deleteAllData()

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteById(id: Int)

}
