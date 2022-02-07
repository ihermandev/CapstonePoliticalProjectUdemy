package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()

    private val _representatives = MutableLiveData<List<Representative>>()
    val representative: LiveData<List<Representative>>
        get() = _representatives

    /**
     * Clear the live data objects to start fresh next time the view model gets called
     */
    fun onClear() {
        addressLine1.value = null
        addressLine2.value = null
        city.value = null
        state.value = null
        zipCode.value = null
    }

    fun validateAndSearchRepresentatives() {

        val address = Address(
            line1 = addressLine1.value,
            line2 = addressLine2.value,
            city = city.value,
            state = state.value,
            zip = zipCode.value
        )

        if (validateEnteredData(address)) {
            searchRepresentatives(address)
        }
    }

    private fun searchRepresentatives(address: Address) {
        showLoading.value = true
        viewModelScope.launch {
            try {
                _representatives.value = repository.searchRepresentatives(address)
                showLoading.value = false
            } catch (e: Exception) {
                showErrorMessage.postValue("Expected representatives are not found")
                Timber.e(e)
                showLoading.value = false
            }
        }
    }

    fun setState(stateName: String?) {
        if (!stateName.isNullOrEmpty()) {
            state.value = stateName
        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    private fun validateEnteredData(address: Address): Boolean {
        if (address.line1.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_line1
            return false
        }

        if (address.city.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_city
            return false
        }

        if (address.line1.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_state
            return false
        }

        if (address.zip.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.err_zip
            return false
        }
        return true
    }
}
