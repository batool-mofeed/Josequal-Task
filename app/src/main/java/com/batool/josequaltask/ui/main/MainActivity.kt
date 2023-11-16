package com.batool.josequaltask.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.batool.josequaltask.BR
import com.batool.josequaltask.R
import com.batool.josequaltask.databinding.ActivityMainBinding
import com.batool.josequaltask.model.PlaceModel
import com.batool.josequaltask.utility.LocationHelper
import com.batool.josequaltask.utility.PermissionsHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding: ActivityMainBinding? = null
    private var mapView: View? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var currentCoordinates: LatLng
    private lateinit var placeModel: PlaceModel
    private lateinit var placesAdapter: PlacesAdapter

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(binding) {
            this?.setVariable(BR.viewModel, mainViewModel)
            this?.lifecycleOwner = this@MainActivity
            this?.executePendingBindings()
        }
        checkLocationPermission()
        initPlacesRecyclerView()
        mainViewModel.setModels()
    }

    private fun initPlacesRecyclerView() {
        placesAdapter = PlacesAdapter() {
            //click on place
        }
        binding?.placesRecycler?.adapter = placesAdapter
    }

    private fun checkLocationPermission() {
        PermissionsHelper(this).checkLocationPermissions(
            onPermissionGranted = {
                initMap()
                getUserLocation()
            },
            onPermissionDenied = { checkLocationPermission() }
        )
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
            mapView = mapFragment.view
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        initMapStyle()
        initCameraListener()
        if (::currentCoordinates.isInitialized) {
            updateMapCamera()
        }
    }

    private fun initMapStyle() {
        try {
            with(googleMap) {
                setMinZoomPreference(14f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initCameraListener() {
        googleMap.setOnCameraIdleListener {
            currentCoordinates = googleMap.cameraPosition.target
        }
    }

    private fun updateMapCamera(coordinates: LatLng = currentCoordinates) {
        if (::googleMap.isInitialized) {
            with(googleMap) {
                moveCamera(CameraUpdateFactory.newLatLng(coordinates))
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        coordinates,
                        18F
                    )
                )
            }
        }
    }

    private fun getUserLocation() {
        LocationHelper(this).loadUserLocation { latLng ->
            currentCoordinates = latLng
            placeModel = PlaceModel(latLang = latLng)
            updateMapCamera()
        }
    }

}