package com.example.android.politicalpreparedness.representative

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RepresentativeFragment : BaseFragment() {

    override val _viewModel: RepresentativeViewModel by viewModel()

    private lateinit var binding: FragmentRepresentativeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        initViewListeners(binding)
        initRecyclerView(binding)

        binding.executePendingBindings()

        return binding.root

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
    }

    private fun initRecyclerView(binding: FragmentRepresentativeBinding) {
        binding.rvRepresentative.adapter = RepresentativeListAdapter(clickListener = RepresentativeListener {
            Timber.i("Clicked representative $it")
        })
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        //TODO: Handle location permission result to get location on permission granted
//    }
//
//    private fun checkLocationPermissions(): Boolean {
//        return if (isPermissionGranted()) {
//            true
//        } else {
//            //TODO: Request Location permissions
//            false
//        }
//    }
//
//    private fun isPermissionGranted(): Boolean {
//        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
//        return true
//    }
//
//    private fun getLocation() {
//        //TODO: Get location from LocationServices
//        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
//    }
//
//    private fun geoCodeLocation(location: Location): Address {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
//            .map { address ->
//                Address(address.thoroughfare,
//                    address.subThoroughfare,
//                    address.locality,
//                    address.adminArea,
//                    address.postalCode)
//            }
//            .first()
//    }
//
//    private fun hideKeyboard() {
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
//    }

    companion object {
        val TAG = RepresentativeFragment::class.java.simpleName
    }

}
