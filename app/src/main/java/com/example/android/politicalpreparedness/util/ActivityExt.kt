package com.example.android.politicalpreparedness.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

fun AppCompatActivity.getSharedPref(): SharedPreferences? {
    return this.getPreferences(Context.MODE_PRIVATE)
}

fun FragmentActivity.getSharedPref(): SharedPreferences? {
    return this.getPreferences(Context.MODE_PRIVATE)
}
