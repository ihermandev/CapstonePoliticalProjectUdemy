package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : BaseFragment() {

    override val _viewModel: ElectionsViewModel by viewModel()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)

        //TODO: Add ViewModel values and create ViewModel
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setupRecyclerView(binding)

        binding.srlUpcoming.setOnRefreshListener {
            _viewModel.updateElectionsData()
        }

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    private fun setupRecyclerView(binding: FragmentElectionBinding) {
        binding.rvUpcoming.adapter =
            ElectionListAdapter(clickListener = ElectionListener {
                _viewModel.showSnackBar.postValue(it.toString())
            })
    }

    //TODO: Refresh adapters when fragment loads

}
