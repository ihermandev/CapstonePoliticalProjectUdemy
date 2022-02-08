package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.VoterInfoFragment
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.util.fadeIn
import com.example.android.politicalpreparedness.util.fadeOut
import com.example.android.politicalpreparedness.util.isNetworkAvailable
import com.example.android.politicalpreparedness.util.showToast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class RepresentativeFragment : BaseFragment() {

    override val _viewModel: RepresentativeViewModel by viewModel()

    private lateinit var binding: FragmentRepresentativeBinding


    private var locationSnackBar: Snackbar? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Access granted.
                Timber.i("Location permission is granted")
                checkDeviceLocationSettingsAndGetUserAddress()
            } else {
                // No location access granted.
                showLocationPermissionSnackbar()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationSnackBar = initLocationSnackbar(binding)

        initViewListeners(binding)
        initRecyclerView(binding)
        observeLiveData(binding)

        binding.executePendingBindings()
    }

    private fun initViewListeners(binding: FragmentRepresentativeBinding) {
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                _viewModel.setState(requireContext().resources.getStringArray(R.array.states)[position])
            }
        }

        binding.buttonLocation.setOnClickListener {
            if (isNetworkAvailable(requireContext())) {
                getUserLocation()
            } else _viewModel.showNetworkError()
        }

        binding.buttonSearch.setOnClickListener {
            if (isNetworkAvailable(requireContext())) {
                _viewModel.validateAndSearchRepresentatives()
            } else _viewModel.showNetworkError()
        }
    }

    private fun observeLiveData(binding: FragmentRepresentativeBinding) {
        //Temporary avoid direct databinding for motion layout because of strange issue
        //https://stackoverflow.com/questions/66676986/motionlayout-and-data-binding
        _viewModel.showLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                hideKeyboard()
                binding.progressBar.fadeIn()
            } else {
                binding.progressBar.fadeOut()
            }
        })
    }

    private fun initRecyclerView(binding: FragmentRepresentativeBinding) {
        binding.rvRepresentative.adapter =
            RepresentativeListAdapter(clickListener = RepresentativeListener {
                Timber.i("Clicked representative $it")
            })
    }

    private fun getUserLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                checkDeviceLocationSettingsAndGetUserAddress()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showLocationPermissionSnackbar()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkUserAddress() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                loc?.let {
                    try {
                        val requiredAddress = geoCodeLocation(loc)
                        Timber.i("Detected user address $requiredAddress")
                        _viewModel.validateAndSearchRepresentativesForLocation(address = requiredAddress)
                    } catch (e: Exception) {
                        Timber.e(e)
                        showToast("Your address do not recognized")
                    }
                }
            }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode)
            }
            .first()
    }

    private fun checkDeviceLocationSettingsAndGetUserAddress(
        resolve: Boolean = true,
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireContext())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(VoterInfoFragment.TAG,
                        "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.container,
                    R.string.location_required_error,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndGetUserAddress()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.i("Location enabled.Checking address...")
                checkUserAddress()
            }
        }
    }

    private fun hideKeyboard() {
        view?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initLocationSnackbar(binding: FragmentRepresentativeBinding): Snackbar {
        val message = R.string.permission_denied_explanation

        return Snackbar.make(
            binding.container,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.settings) {
            // Displays App settings screen.
            startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    private fun showLocationPermissionSnackbar() {
        locationSnackBar?.show()
    }

    override fun onDestroy() {
        locationSnackBar?.dismiss()
        super.onDestroy()
    }

    companion object {
        val TAG: String = RepresentativeFragment::class.java.simpleName
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    }

}
