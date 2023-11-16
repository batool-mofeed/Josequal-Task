package com.batool.josequaltask.utility

import android.annotation.SuppressLint
import android.content.Context
import com.birjuvachhani.locus.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationHelper(
    private val context: Context
) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val permissionHelper by lazy {
        PermissionsHelper(context)
    }

    @SuppressLint("MissingPermission")
    fun loadUserLocation(
        result: (LatLng) -> Unit
    ) {
        permissionHelper.checkLocationPermissions(
            onPermissionGranted = {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val location = task.result
                        if (location != null) {
                            val latLng = LatLng(location.latitude, location.longitude)
                            result(latLng)
                        } else {
                            loadCurrentLocation(result)
                        }
                    } else {
                        loadCurrentLocation(result)
                    }
                }
            }
        )
    }

    private fun loadCurrentLocation(locationResult: (LatLng) -> Unit) {
        Locus.getCurrentLocation(context) { result ->
            result.location?.let {
                locationResult(LatLng(it.latitude, it.longitude))
            }
        }
    }
}