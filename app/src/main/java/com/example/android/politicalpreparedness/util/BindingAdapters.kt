package com.example.android.politicalpreparedness.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.domain.ElectionDomain
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {

    @BindingAdapter("android:showLoading")
    @JvmStatic
    fun bindLoading(view: SwipeRefreshLayout, loading: Boolean?) {
        loading?.let {
            view.isRefreshing = it
        } ?: return
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
    fun bindDate(view: TextView, date: Date?) {
        date?.let {
            val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            view.text = format.format(it)
        } ?: return
    }

    @BindingAdapter("android:electionActionText")
    @JvmStatic
    fun bindButtonAction(textView: TextView, isSaved: Boolean?) {
        isSaved?.let {
            val label =
                if (isSaved) textView.context.getString(R.string.remove_action) else textView.context.getString(
                    R.string.save_action)
            textView.contentDescription = label
            textView.text = label
        } ?: textView.gone()
    }

    @BindingAdapter("android:isVisible")
    @JvmStatic
    fun setIsVisible(view: View, count: Int?) {
        count?.let {
            if (it > 0) view.visible() else view.gone()
        } ?: view.gone()
    }
}
