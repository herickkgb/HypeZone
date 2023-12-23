package com.pocket.apps.infest.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.pocket.apps.infest.R
import com.pocket.apps.infest.adapter.MarkerInfoAdapter
import com.pocket.apps.infest.api.maps.BitMapHelper
import com.pocket.apps.infest.databinding.FragmentHomeBinding
import com.pocket.apps.infest.model.place.Place

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var client: FusedLocationProviderClient
    private lateinit var localAtualBH: LatLng

    private val places = arrayOf(
        Place(
            "Savassi",
            LatLng(-19.932945, -43.938506),
            "R. Cláudio Manoel - Savassi, Belo Horizonte - MG, Brazil",
            4.9f
        )

    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        client = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment?

        uncharismatic(mapFragment)
        return binding.root

    }

    private fun uncharismatic(mapFragment: SupportMapFragment?) {
        mapFragment?.getMapAsync { googleMap ->
            addMarkers(googleMap)
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(requireContext()))
            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()

                places.forEach {
                    bounds.include(it.latLng)
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
            }
        }

    }
    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        client.lastLocation
            .addOnSuccessListener(OnSuccessListener<Location> { location ->
                if(location != null){
                    Log.d("teste","${location.latitude}")
                    Log.d("teste","${location.longitude}")

                    localAtualBH = LatLng(location.latitude,location.longitude)
                }
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.d("erro", "failure")
            })


    }

    @SuppressLint("ResourceType")
    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.address)
                    .position(place.latLng)
                    .icon(
                        BitMapHelper.vectorToBitMap(
                            requireContext(),
                            R.drawable.ic_fire,
                            Color.RED
                        )
                    )
            )
            if (marker != null) {
                marker.tag = place
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
