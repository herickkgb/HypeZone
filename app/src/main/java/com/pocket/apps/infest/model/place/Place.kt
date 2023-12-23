package com.pocket.apps.infest.model.place

data class Place(
    val name: String,
    val latLng: com.google.android.gms.maps.model.LatLng,
    val address: String,
    val rating: Float
)
