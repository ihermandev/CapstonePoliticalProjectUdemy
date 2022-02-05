package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.data.Result

class ElectionsViewModel(
    private val app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    //TODO: Create live data val for upcoming elections
    val elections = repository.elections

    //TODO: Create live data val for saved elections
    val savedElections = repository.savedElections


    init {
       // updateElectionsData()
    }

    fun updateElectionsData() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                repository.updateElectionsData()
                showLoading.postValue(false)
            } catch (e: Exception) {
                showLoading.postValue(false)
                showErrorMessage.postValue(e.localizedMessage)
            }
        }
    }


    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}
