package com.example.android.politicalpreparedness.data.local.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.politicalpreparedness.data.TestData.testElection1
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ElectionDaoTest {

    private lateinit var database: ElectionDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ElectionDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun saveElectionToDatabase() = runBlockingTest {
        database.electionDao.saveElection(testElection1)

        val result = database.electionDao.getElectionById(1)

        assertThat(result as Election, notNullValue())
        assertThat(result.id, `is`(result.id))
        assertThat(result.name, `is`(result.name))
        assertThat(result.isSaved, `is`(result.isSaved))
        assertThat(result.division.id, `is`(result.division.id))
        assertThat(result.division.country, `is`(result.division.country))
        assertThat(result.division.state, `is`(result.division.state))
    }

    @Test
    fun saveElectionToDatabaseAndDeleteIById() = runBlockingTest {
        database.electionDao.saveElection(testElection1)

        database.electionDao.deleteById(testElection1.id)

        val result = database.electionDao.getElectionById(testElection1.id)

        assertNull(result)
    }

    @Test
    fun updateElectionIsSavedState() = runBlockingTest {
        database.electionDao.saveElection(testElection1)

        database.electionDao.updateElectionState(testElection1.id, testElection1.isSaved.not())

        val result = database.electionDao.getElectionById(testElection1.id)

        assertThat(result as Election, notNullValue())
        assertThat(result.isSaved, not(testElection1.isSaved))
    }

    @Test
    fun getInvalidDataFromDatabase() = runBlockingTest {
        database.electionDao.saveElection(testElection1)

        val invalidID = 123
        val result = database.electionDao.getElectionById(invalidID)

       assertNull(result)
    }
}
