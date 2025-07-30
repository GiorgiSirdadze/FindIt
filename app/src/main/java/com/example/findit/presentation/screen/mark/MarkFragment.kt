package com.example.findit.presentation.screen.mark

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.findit.R
import com.example.findit.databinding.FragmentMarkBinding
import com.example.findit.presentation.base.BaseMapFragment
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest

class MarkFragment : BaseMapFragment<FragmentMarkBinding>(FragmentMarkBinding::inflate),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val viewModel: MarkViewModel by viewModels()

    private var selectedMarker: Marker? = null

    override fun onLocationSetupSuccess() {
        super.onLocationSetupSuccess()
        fetchCurrentUser()
    }

    override fun setUp() {
        super.setUp()
        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.markMapFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener { latLng ->
            viewModel.onEvent(MarkEvent.UpdateSelectedLocation(latLng))
        }
        observer()
    }

    private fun observer() {
        launchCoroutine {
            viewModel.selectedLatLng.collect { latLng ->
                latLng?.let {
                    selectedMarker?.remove()
                    selectedMarker = map.addMarker(MarkerOptions().position(it))

                }
            }
        }

        launchCoroutine {
            viewModel.effect.collectLatest { effect->
                when (effect) {
                    is MarkEffect.NavigateBack ->{
                        navigateBack(effect.latLng)
                    }
                }
            }
        }
    }

    override fun setListeners() {
        binding.markLocationButton.setOnClickListener {
            viewModel.onEvent(MarkEvent.NavigateBack)
        }
    }

    private fun fetchCurrentUser() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        map.addCircle(
                            CircleOptions()
                                .center(latLng)
                                .radius(3.0)
                                .strokeColor(Color.BLUE)
                                .fillColor(ContextCompat.getColor(requireContext(), R.color.current_blue))                        )

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
                .addOnFailureListener {
                    binding.root.showSnackBar(getString(R.string.failed_to_get_location))
                }
        } catch (e: SecurityException) {
            binding.root.showSnackBar(getString(R.string.location_permission_denied))
        }
    }

    private fun navigateBack(latLng: LatLng?) {
        val resultBundle = Bundle().apply {
            latLng?.let {
                putParcelable(SELECTED_LOCATION_KEY, it)
            }
        }

        setFragmentResult(REQUEST_KEY, resultBundle)

        parentFragmentManager.popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "selected_location_key"
        const val SELECTED_LOCATION_KEY = "selected_location"
    }
}
