package com.example.android.politicalpreparedness.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.politicalpreparedness.base.BaseRecyclerViewAdapter
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @BindingAdapter("android:showLoading")
    @JvmStatic
    fun bindLoading(view: SwipeRefreshLayout, loading: Boolean?) {
        loading?.let {
            view.isRefreshing = it
        }
    }

    @BindingAdapter("android:electionData")
    @JvmStatic
    fun bindRecyclerView(recyclerView: RecyclerView, data: List<ElectionDomain>?) {
        data?.let {
            val adapter = recyclerView.adapter as ElectionListAdapter
            adapter.submitList(it)
        } ?: return
    }

    @BindingAdapter("android:date")
    @JvmStatic
    fun bindDate(view: TextView, date: Date) {
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        view.text = format.format(date)
    }
}
