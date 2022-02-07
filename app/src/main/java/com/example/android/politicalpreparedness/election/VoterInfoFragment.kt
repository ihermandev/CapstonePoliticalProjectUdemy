package com.example.android.politicalpreparedness.election

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.util.showToast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class VoterInfoFragment : BaseFragment() {

    override val _viewModel: VoterInfoViewModel by viewModel()

    private lateinit var binding: FragmentVoterInfoBinding

    private val electionID by lazy {
        VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
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


    private var locationSnackBar: Snackbar? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()
        binding.votingFinder.movementMethod = LinkMovementMethod.getInstance()


        binding.btnVoter.setOnClickListener {
            _viewModel.getElectionById(electionID)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.getElectionById(electionID)
        locationSnackBar = initLocationSnackbar(binding)
        getUserLocation()

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
                        val requiredAddress =
                            Geocoder(requireContext()).getFromLocation(it.latitude,
                                it.longitude, 1).firstOrNull()
                        val stringAddress = "${requiredAddress?.getAddressLine(0)}"
                        Timber.i("Detected user string address $stringAddress")
                        _viewModel.getVoterInfo(electionId = electionID, stringAddress)
                    } catch (e: Exception) {
                        Timber.e(e)
                        showToast("Your address do not recognized")
                    }
                }
            }
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
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
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


    private fun initLocationSnackbar(binding: FragmentVoterInfoBinding): Snackbar {
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
        val TAG: String = VoterInfoFragment::class.java.simpleName
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    }
}
