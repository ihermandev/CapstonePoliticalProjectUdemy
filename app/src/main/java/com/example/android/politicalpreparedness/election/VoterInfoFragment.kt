package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : BaseFragment() {

    override val _viewModel: VoterInfoViewModel by viewModel()

    private lateinit var binding: FragmentVoterInfoBinding

    private val electionID by lazy {
        VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
    }

    private val electionDivision by lazy {
        VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)

//        val electionID =
//        val electionDivision = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        _viewModel.getElectionById(electionID)

        binding.btnVoter.setOnClickListener {
            _viewModel.getElectionById(electionID)
        }

//        _viewModel.election.observe(viewLifecycleOwner, Observer {
//            _viewModel.showSnackBar.postValue(it.toString())
//        }
//        )

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    //TODO: Create method to load URL intents

}
