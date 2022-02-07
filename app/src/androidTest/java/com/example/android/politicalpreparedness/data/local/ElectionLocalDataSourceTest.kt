package com.example.android.politicalpreparedness.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.data.TestData.testElection1
import com.example.android.politicalpreparedness.data.local.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.TestData.testElection2
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class ElectionLocalDataSourceTest {

    private lateinit var localDataSource: ElectionLocalDataSourceImpl
    private lateinit var database: ElectionDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ElectionDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            ElectionLocalDataSourceImpl(
                database.electionDao,
                Dispatchers.Main
            )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveElectionAndRetrieveElection() = runBlocking {
        localDataSource.saveElection(testElection1)

        val result = localDataSource.getElectionById(testElection1.id)

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.id, `is`(1))
        assertThat(result.data.name, `is`("TITLE"))
        assertThat(result.data.electionDay, `is`(testElection1.electionDay))
        assertThat(result.data.isSaved, `is`(false))
        assertThat(result.data.division.id, `is`("5"))
        assertThat(result.data.division.country, `is`("US"))
        assertThat(result.data.division.state, `is`("AL"))
    }

    @Test
    fun retrieveElectionNotSuccessResultWhenWrongIdIsPassed() = runBlocking {
        val wrongId = 123
        val result = localDataSource.getElectionById(wrongId)
        assertThat(result.succeeded, `is`(false))
    }

    @Test
    fun saveElectionsAndClearDB() = runBlocking {
        localDataSource.saveElection(testElection1)
        localDataSource.saveElection(testElection2)

        localDataSource.deleteAllData()

        val result1 = localDataSource.getElectionById(testElection1.id)
        val result2 = localDataSource.getElectionById(testElection2.id)

        assertThat(result1.succeeded, `is`(false))
        assertThat(result2.succeeded, `is`(false))
    }
}
