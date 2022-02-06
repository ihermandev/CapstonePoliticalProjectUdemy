package com.example.android.politicalpreparedness.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.google.android.material.snackbar.Snackbar

//fun Fragment.initLocationSnackBar(
//    container: View,
//    duration: Int = Snackbar.LENGTH_LONG,
//): Snackbar {
//    val message = R.string.permission_denied_explanation
//
//    return Snackbar.make(
//        container,
//        message,
//        duration
//    ).setAction(R.string.settings) {
//        // Displays App settings screen.
//        startActivity(Intent().apply {
//            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        })
//    }
//}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
