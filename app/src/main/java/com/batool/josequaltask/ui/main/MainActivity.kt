package com.batool.josequaltask.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.data.kml.KmlLayer
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var binding: ActivityMainBinding? = null
    private var mapView: View? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var currentCoordinates: LatLng
    private lateinit var placesAdapter: PlacesAdapter

    var markers: HashMap<String, PlaceModel> = HashMap()

    // added to handle click when there are kml layers
    lateinit var markerManager: MarkerManager
    lateinit var markerCollection: MarkerManager.Collection

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
        observeViewModel()
    }

    private fun observeViewModel() {
        with(mainViewModel) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    placeModels.collect {
                        if (it != null) {
                            updateMapWithPlaces(it)
                        }
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
//            val marker = googleMap.addMarker(markerOptions)
            val marker = markerCollection.addMarker(markerOptions)
            if (marker != null) {
                markers[marker.id] = place
            }
        }
        loadKmlLayers()
    }

    private fun initPlacesRecyclerView() {
        placesAdapter = PlacesAdapter() {
            currentCoordinates = it.latLang
            updateMapCamera()
        }
        binding?.placesRecycler?.adapter = placesAdapter
    }

    private fun checkLocationPermission() {
        PermissionsHelper(this).checkLocationPermissions(
            onPermissionGranted = {
                initPlacesRecyclerView()
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

        // added to handle click when there are kml layers
        markerManager = MarkerManager(googleMap)
        markerCollection = markerManager.newCollection()

        initMapStyle()
        initCameraListener()
        if (::currentCoordinates.isInitialized) {
            updateMapCamera()
        }

        //To handle click when kml layers exists
        markerCollection.setOnMarkerClickListener { marker ->
            handleMarkerClick(marker)
            true
        }

//        googleMap.setOnMarkerClickListener { marker ->
//            handleMarkerClick(marker)
//            true
//        }

        mainViewModel.setModels()
        loadKmlLayers()
    }

    private fun handleMarkerClick(marker: Marker) {
        val place: PlaceModel? = markers[marker.id]
        if (place != null) {
            Log.d("MarkerClick", "Marker clicked: ${place.placeName}")
            PlaceDetailsDialog.newInstance(place).show(supportFragmentManager, "")
        }
    }

    private fun initMapStyle() {
        try {
            with(googleMap) {
                setMinZoomPreference(10f)
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
            with(googleMap) {
                moveCamera(CameraUpdateFactory.newLatLng(coordinates))
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        coordinates,
                        15F
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

    private fun loadKmlLayers() {
        try {
            val kmlFiles = listOf(
                R.raw.marker_1_rectangle,
                R.raw.marker_2_circle,
                R.raw.marker_3_triangle
            )

            for (kmlFile in kmlFiles) {
                val kmlLayer = KmlLayer(
                    googleMap, kmlFile, this,
                    markerManager, null, null, null, null
                )
                kmlLayer.addLayerToMap()
            }

            Log.e("KML", "All layers added successfully")

        } catch (e: IOException) {
            Log.e("KML", "Error loading KML layers: ${e.message}")
        }
    }
}
