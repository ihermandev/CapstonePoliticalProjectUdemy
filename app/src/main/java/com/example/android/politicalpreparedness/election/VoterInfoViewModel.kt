package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.data.network.models.State
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    private val _election = MutableLiveData<ElectionDomain>()
    val election: LiveData<ElectionDomain>
        get() = _election

    private val _electionStateData = MutableLiveData<State>()
    val electionStateData
        get() = _electionStateData


    fun getElectionById(id: Int) {
        viewModelScope.launch {
            try {
                showLoading.value = true
                val result = repository.getElectionById(id)
                _election.value = result
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
                val stateInfo = result.state?.firstOrNull()
                if (stateInfo != null) {
                    _electionStateData.postValue(stateInfo)
                } else {
                    showErrorMessage.value = "State not found"
                }
                showLoading.value = false

            } catch (e: Exception) {
                showErrorMessage.postValue("Expected voter details are not found")
                Timber.e(e)
                showLoading.value = false
            }
        }
    }

    fun updateElectionSavedState() {
        viewModelScope.launch {
            election.value?.let { data ->
                repository.updateElectionState(electionID = data.id, isSaved = data.isSaved.not())
                navigationCommand.value = NavigationCommand.Back
            }
        }
    }

}
