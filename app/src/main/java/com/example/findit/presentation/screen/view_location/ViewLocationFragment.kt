package com.example.findit.presentation.screen.view_location

import android.graphics.Color
import android.location.Location
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.findit.R
import com.example.findit.databinding.FragmentViewLocationBinding
import com.example.findit.presentation.base.BaseMapFragment
import com.example.findit.presentation.extension.getBitmapDescriptorFromVector
import com.example.findit.presentation.extension.getIconRes
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewLocationFragment : BaseMapFragment<FragmentViewLocationBinding>(FragmentViewLocationBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: ViewLocationViewModel by viewModels()
    private val args: ViewLocationFragmentArgs by navArgs()

    private lateinit var map: GoogleMap

    override fun onLocationSetupSuccess() {
        super.onLocationSetupSuccess()
        fetchCurrentUser()
    }

    override fun setUp() {
        super.setUp()
        initializeMap()
        viewModel.onEvent(ViewLocationEvent.GetPost(args.postId))
    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

     private fun observers() {
        launchCoroutine {
            viewModel.state.collect { state ->
                if (::map.isInitialized && state.postLocation != null && state.currentUserLocation != null) {

                    val userLocation = state.currentUserLocation
                    val postLocation = state.postLocation

                    val icon = requireContext().getBitmapDescriptorFromVector(state.type.getIconRes())
                    map.addMarker(
                        MarkerOptions()
                            .position(postLocation)
                            .title(getString(R.string.item_location))
                            .icon(icon)
                    )

                    map.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title(getString(R.string.your_location))
                            .icon(requireContext().getBitmapDescriptorFromVector(R.drawable.me_icon))
                    )

                    val polylineOptions = PolylineOptions()
                        .add(userLocation)
                        .add(postLocation)
                        .color(Color.RED)
                        .width(3f)
                    map.addPolyline(polylineOptions)

                    val results = FloatArray(1)
                    Location.distanceBetween(
                        userLocation.latitude, userLocation.longitude,
                        postLocation.latitude, postLocation.longitude,
                        results
                    )

                    val distance = results[0] / 1000

                    binding.distanceTextView.text = getString(R.string.distance_km, distance.toString())

                    val latLngBounds = LatLngBounds.Builder()
                        .include(userLocation)
                        .include(postLocation)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))
                }
                binding.progressBar.isVisible = state.isLoading
                binding.distanceTextView.isVisible = !state.isLoading

                state.error?.let {
                    binding.root.showSnackBar(it)
                    viewModel.onEvent(ViewLocationEvent.ClearError)
                }
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        observers()
    }

    private fun fetchCurrentUser() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        viewModel.onEvent(ViewLocationEvent.SetCurrentUserLocation(latLng))
                    }
                }
                .addOnFailureListener {
                    binding.root.showSnackBar(getString(R.string.failed_to_get_location))
                }
        } catch (e: SecurityException) {
            binding.root.showSnackBar(getString(R.string.location_permission_denied))
        }
    }

}