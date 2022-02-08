package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.util.Const.FIRST_TIME_FLOW
import com.example.android.politicalpreparedness.util.getSharedPref
import com.example.android.politicalpreparedness.util.isNetworkAvailable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : BaseFragment() {

    override val _viewModel: ElectionsViewModel by viewModel()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentElectionBinding.inflate(inflater, container, false)

        with(binding) {
            viewModel = _viewModel
            lifecycleOwner = this@ElectionsFragment

            setupRecyclerView(this)
            setupViewListeners(this)
        }

        checkFirstTimeUserFlow()

        return binding.root
    }

    private fun setupViewListeners(binding: FragmentElectionBinding) {
        binding.srlUpcoming.setOnRefreshListener {
            if (isNetworkAvailable(requireContext())) {
                _viewModel.updateElectionsData()
            } else _viewModel.showNetworkError()
        }
    }

    private fun checkFirstTimeUserFlow() {
        activity?.getSharedPref()?.let { pref ->
            if (pref.getBoolean(FIRST_TIME_FLOW, true)) {
                if (isNetworkAvailable(requireContext())) {
                    _viewModel.forceUpdateElectionsData()
                    pref.edit {
                        putBoolean(FIRST_TIME_FLOW, false)
                        commit()
                        apply()
                    }
                } else _viewModel.showNetworkError()
            } else {
                _viewModel.updateElectionsData()
            }
        } ?: _viewModel.updateElectionsData()
    }

    private fun setupRecyclerView(binding: FragmentElectionBinding) {
        binding.rvUpcoming.adapter =
            ElectionListAdapter(clickListener = ElectionListener {
                _viewModel.navigateToVoterInfo(it)
            })

        binding.rvSaved.adapter =
            ElectionListAdapter(clickListener = ElectionListener {
                _viewModel.navigateToVoterInfo(it)
            })
    }
}
