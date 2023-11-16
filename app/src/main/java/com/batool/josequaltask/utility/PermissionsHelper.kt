package com.batool.josequaltask.utility

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionsHelper(
    private val context: Context
) {
    fun checkLocationPermissions(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit = {},
        dismiss: () -> Unit = {}
    ) {
        displayLocationSettingsRequest()
        Dexter.withContext(context)
            .withPermissions(
                mutableListOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            if (isLocationEnabled()) {
                                onPermissionGranted()
                            } else {
                                displayLocationSettingsRequest()
                            }
                        } else {
                            onPermissionDenied()
                            showSettingsDialog(dismiss)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }


    fun showSettingsDialog(
        dismiss: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle("Location Required")
            setMessage("Please enable location permission to access the features in the app")
            setPositiveButton("Settings") { dialog, _ ->
                dialog.cancel()
                openAppSettings()
                dismiss()
            }
            setNegativeButton("cancel") { dialog, _ ->
                dialog.cancel()
                dismiss()
            }
        }.show()
    }

    fun isLocationEnabled(): Boolean {
        var locationMode = 0
        val locationProviders: String
        locationMode = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    fun displayLocationSettingsRequest() {
        val lm: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Log.e("PermissionHelper", "displayLocationSettingsRequest: " + ex.message)
        }
        if (!gpsEnabled) {
            AlertDialog.Builder(context)
                .setMessage("Open location services")
                .setPositiveButton(
                    "Turn on"
                ) { dialog, which -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .show()
        }
    }

    private fun openAppSettings() {
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName)
            )
        )
    }


}