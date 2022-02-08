package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchFragment : BaseFragment() {

    override val _viewModel: LaunchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentLaunchBinding.inflate(inflater)

        with(binding) {
            lifecycleOwner = this@LaunchFragment

            btnRepresentative.setOnClickListener { navToRepresentatives() }
            btnUpcoming.setOnClickListener { navToElections() }
        }

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
