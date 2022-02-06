package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.data.domain.ElectionDomain

class ElectionsViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    //TODO: Create live data val for upcoming elections
    val elections = repository.elections

    //TODO: Create live data val for saved elections
    val savedElections = repository.savedElections


  //  private val _dataLoading = MutableLiveData(false)
  // val dataLoading: LiveData<Boolean> = _dataLoading


    init {
        // updateElectionsData()
    }

    fun forceUpdateElectionsData() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                repository.forceUpdateElectionsData()
                showLoading.value = false
            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.value = false
            }
        }
    }

    fun updateElectionsData() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                repository.updateElectionsData()
                showLoading.value = false
            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.value = false
            }
        }
    }

    fun getVoterInfo(electionId: Int, address: String) {
        showLoading.value = true
    }



    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    fun navigateToVoterInfo(election: ElectionDomain) {
        navigationCommand.postValue(
            NavigationCommand.To(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id,
                    election.division
                )
            )
        )
    }

}
