package com.batool.josequaltask.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.batool.josequaltask.BR
import com.batool.josequaltask.R
import com.batool.josequaltask.databinding.ActivityMainBinding
import com.batool.josequaltask.model.PlaceModel
import com.batool.josequaltask.ui.main.placedetails.PlaceDetailsDialog
import com.batool.josequaltask.utility.LocationHelper
import com.batool.josequaltask.utility.PermissionsHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding: ActivityMainBinding? = null
    private var mapView: View? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var currentCoordinates: LatLng
    private lateinit var placesAdapter: PlacesAdapter

    var markers: HashMap<String, PlaceModel> = HashMap()


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
        observeViewModel()
    }

    private fun observeViewModel() {
        with(mainViewModel) {
            lifecycleScope.launchWhenCreated {
                placeModels.collect {
                    if (it != null) {
                        updateMapWithPlaces(it)
                    }
                }
            }
        }
    }

    private fun updateMapWithPlaces(places: List<PlaceModel>) {
        googleMap.clear()
        for (place in places) {
            val markerOptions = MarkerOptions()
                .position(place.latLang)
                .title(place.placeName)
            val marker = googleMap.addMarker(markerOptions)
            if (marker != null) {
                markers[marker.id] = place
            }
        }
    }

    private fun initPlacesRecyclerView() {
        placesAdapter = PlacesAdapter() {
            //move map camera to clicked position
            currentCoordinates = it.latLang
            updateMapCamera()
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
        googleMap.setOnMarkerClickListener { marker ->
            val place: PlaceModel? = markers[marker.id]
            if (place != null) {
                PlaceDetailsDialog.newInstance(place).show(supportFragmentManager, "")
            }
            true
        }
        mainViewModel.setModels()
    }

    private fun initMapStyle() {
        try {
            with(googleMap) {
                setMinZoomPreference(13f)
                enableMapGestures()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun enableMapGestures() {
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
    }

    private fun initCameraListener() {
        googleMap.setOnCameraIdleListener {
            currentCoordinates = googleMap.cameraPosition.target
        }
    }

    private fun updateMapCamera(coordinates: LatLng = currentCoordinates) {
        if (::googleMap.isInitialized) {
            Log.e("TAG", "onBind: " + "nnnnnnnnnnnnnnn" )

            with(googleMap) {
                moveCamera(CameraUpdateFactory.newLatLng(coordinates))
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        coordinates,
                        13F
                    )
                )
            }
        }
    }

    private fun getUserLocation() {
        LocationHelper(this).loadUserLocation { latLng ->
            currentCoordinates = latLng
            updateMapCamera()
        }
    }

}