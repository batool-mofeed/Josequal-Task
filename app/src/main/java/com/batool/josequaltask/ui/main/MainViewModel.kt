package com.batool.josequaltask.ui.main

import androidx.lifecycle.ViewModel
import com.batool.josequaltask.model.PlaceModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created By Batool Mofeed - Vibes Solutions on 11/16/2023.
 **/
class MainViewModel : ViewModel() {

    private val _placeModels = MutableStateFlow<List<PlaceModel>?>(null)
    val placeModels = _placeModels.asStateFlow()

    fun setModels() {
        _placeModels.value = listOf(
            PlaceModel("Place 1", LatLng(32.016559647390686, 35.86971726138413)),
            PlaceModel("Place 2", LatLng(31.985806881693907, 35.864953657786025)),
            PlaceModel("Place 3", LatLng(31.96562298518118, 35.8862122064548)),
            PlaceModel("Place 4", LatLng(31.967944255129197, 35.92124534815862)),
            PlaceModel("Place 5", LatLng(31.946511728484744, 35.92368838481421)),
            PlaceModel("Place 6", LatLng(31.942655830127585, 35.892612960644804))
        )
    }
}