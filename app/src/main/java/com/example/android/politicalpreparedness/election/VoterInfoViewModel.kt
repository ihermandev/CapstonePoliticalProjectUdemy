package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.toDomainModel
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    //TODO: Add live data to hold voter info
    private val _election = MutableLiveData<ElectionDomain>()
    val election: LiveData<ElectionDomain>
        get() = _election

    //  private val _electionState = Mutable

    fun getElectionById(id: Int) {
        viewModelScope.launch {
            try {
                showLoading.value = true
                val result = repository.getElectionById(id)
                _election.value = result
                println(result)
                showLoading.postValue(false)
            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.postValue(false)
            }
        }
    }

    fun getVoterInfo(electionId: Int, address: String) {
        showLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getVoterInfo(electionId = electionId, address = address)
                println(result.state)

            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.postValue(false)
            }
        }
    }

    fun updateElectionState() {
        viewModelScope.launch {
            election.value?.let { data ->
                repository.updateElectionState(electionID = data.id, isSaved = data.isSaved.not())
                navigationCommand.value = NavigationCommand.Back
            }
        }
    }

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}
