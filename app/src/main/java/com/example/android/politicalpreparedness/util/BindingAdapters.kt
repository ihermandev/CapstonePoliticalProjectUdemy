package com.example.android.politicalpreparedness.util

import android.os.Build
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
import android.text.Html

import android.text.Spanned
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.State
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative


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
    fun bindElectionRecyclerView(recyclerView: RecyclerView, data: List<ElectionDomain>?) {
        data?.let {
            val adapter = recyclerView.adapter as ElectionListAdapter
            adapter.submitList(it)
        } ?: return
    }

    @BindingAdapter("android:representativeData")
    @JvmStatic
    fun bindRepresentativeRecyclerView(recyclerView: RecyclerView, data: List<Representative>?) {
        data?.let {
            val adapter = recyclerView.adapter as RepresentativeListAdapter
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

    @BindingAdapter("android:isStateAvailable")
    @JvmStatic
    fun bindStateAvailability(view: View, data: LiveData<State>?) {
        data?.value?.let {
            view.visible()
        } ?: view.gone()
    }

    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {
        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {
            view.animate().cancel()
            if (visible == true) {
                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {
                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
            }
        }
    }

    @BindingAdapter("android:checkIfVisible")
    @JvmStatic
    fun setSimpleVisible(view: View, visible: Boolean? = true) {
        visible?.let {
            if (it) view.visible() else view.gone()
        } ?: return
    }

    @BindingAdapter("android:htmlText")
    @JvmStatic
    fun setHtmlTextValue(textView: TextView, htmlText: String?) {
        htmlText?.let {
            val result: Spanned =
                Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
            textView.text = result
        } ?: return
    }
}
