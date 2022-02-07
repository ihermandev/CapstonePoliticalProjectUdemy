package com.example.android.politicalpreparedness.data.repository

import com.example.android.politicalpreparedness.MainCoroutineRule
import com.example.android.politicalpreparedness.data.FakeData.fakeAddress
import com.example.android.politicalpreparedness.data.FakeData.fakeRepresentativeResponse
import com.example.android.politicalpreparedness.data.FakeData.fakeVoterInfoResponse
import com.example.android.politicalpreparedness.data.FakeData.testElection1
import com.example.android.politicalpreparedness.data.FakeData.testElection2
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.local.ElectionLocalDataSource
import com.example.android.politicalpreparedness.data.local.FakeElectionLocalDataSource
import com.example.android.politicalpreparedness.data.network.ElectionNetworkDataSource
import com.example.android.politicalpreparedness.data.network.FakeElectionNetworkDataSource
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.data.succeeded
import org.hamcrest.MatcherAssert.assertThat
import com.example.android.politicalpreparedness.getOrAwaitValue
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.hamcrest.core.IsCollectionContaining
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class ElectionRepositoryTest {

    private lateinit var electionsNetworkDataSource: ElectionNetworkDataSource
    private lateinit var electionLocalDataSource: ElectionLocalDataSource
    private lateinit var electionRepository: ElectionRepository

    private val networkData = listOf(testElection1, testElection2)
    private val localData = listOf(testElection1)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupRepo() {
        electionsNetworkDataSource = FakeElectionNetworkDataSource(networkData.toMutableList())
        electionLocalDataSource = FakeElectionLocalDataSource(localData.toMutableList())
        electionRepository =
            ElectionRepository(api = electionsNetworkDataSource, database = electionLocalDataSource)
    }

    @Test
    fun `get Election by id`() = runBlockingTest {
        val result = electionRepository.getElectionById(1)

        assertThat(result.id, `is`(testElection1.id))
    }

    @Test
    fun `get Election by invalid id and catch error`() = runBlockingTest {
        assertFailsWith<Exception> {
            electionRepository.getElectionById(5)
        }
    }

    @Test
    fun `get Voter info date successfully`() = runBlockingTest {
        val result = electionRepository.getVoterInfo(electionId = 7168, address = "")

        assertThat(result, instanceOf(VoterInfoResponse::class.java))
        assertThat(result.state?.size, `is`(fakeVoterInfoResponse().state?.size))
    }

    @Test
    fun `get Voter info date unsuccessfully with exception`() = runBlockingTest {
        electionsNetworkDataSource = FakeElectionNetworkDataSource(networkData.toMutableList())
        (electionsNetworkDataSource as FakeElectionNetworkDataSource).setReturnError(true)
        electionRepository = ElectionRepository(api = electionsNetworkDataSource, electionLocalDataSource)

        assertFailsWith<Exception> {
            electionRepository.getVoterInfo(electionId = 7168, address = "")
        }
    }

    @Test
    fun `get Representative successfully`() = runBlockingTest {
        val result = electionRepository.searchRepresentatives(fakeAddress)

        assertThat(result, IsCollectionContaining(IsInstanceOf(Representative::class.java)))
        assertThat(result, not(IsNull()))
        assertThat(result.size, not(0))
    }

    @Test
    fun `get Representative unsuccessfully with exception`() = runBlockingTest {
        electionsNetworkDataSource = FakeElectionNetworkDataSource(networkData.toMutableList())
        (electionsNetworkDataSource as FakeElectionNetworkDataSource).setReturnError(true)

        electionRepository = ElectionRepository(api = electionsNetworkDataSource, electionLocalDataSource)

        assertFailsWith<Exception> {
            electionRepository.searchRepresentatives(fakeAddress)
        }
    }

}
