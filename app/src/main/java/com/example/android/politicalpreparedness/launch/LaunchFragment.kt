package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchFragment : BaseFragment() {

    override val _viewModel: LaunchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentLaunchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.btnRepresentative.setOnClickListener { navToRepresentatives() }
        binding.btnUpcoming.setOnClickListener { navToElections() }

        return binding.root
    }

    private fun navToElections() {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
        )
    }

    private fun navToRepresentatives() {
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
        )
    }
}
