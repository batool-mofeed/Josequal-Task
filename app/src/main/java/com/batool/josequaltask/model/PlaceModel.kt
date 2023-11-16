package com.batool.josequaltask.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    var primaryName: String = "",
    var address: String = "",
    var placeId: String = "",
    var placeName: String = "",
    var latLang: LatLng = LatLng(0.0, 0.0)
) : Parcelable