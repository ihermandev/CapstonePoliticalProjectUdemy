package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.repository.ElectionRepository

class RepresentativeViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

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
        //TODO
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
    fun validateEnteredData(address: Address): Boolean {
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
