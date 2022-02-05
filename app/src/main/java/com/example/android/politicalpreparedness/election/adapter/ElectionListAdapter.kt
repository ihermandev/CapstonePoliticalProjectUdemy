package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<ElectionDomain, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<ElectionDomain>() {
        override fun areItemsTheSame(oldItem: ElectionDomain, newItem: ElectionDomain): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ElectionDomain, newItem: ElectionDomain): Boolean {
            return oldItem == newItem
        }
    }

    class ElectionViewHolder(val binding: ViewholderElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) = ElectionViewHolder(
                ViewholderElectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(election: ElectionDomain, listener: ElectionListener) {
            binding.election = election
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class ElectionListener(val clickListener: (election: ElectionDomain) -> Unit) {
    fun onClick(election: ElectionDomain) = clickListener(election)
}
