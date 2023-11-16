package com.batool.josequaltask.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    var placeName: String = "",
    var latLang: LatLng = LatLng(0.0, 0.0)
) : Parcelable
