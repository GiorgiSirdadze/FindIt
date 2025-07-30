package com.example.findit.presentation.screen.location

import android.annotation.SuppressLint
import android.util.Log.d
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.findit.R
import com.example.findit.databinding.FragmentLocationBinding
import com.example.findit.presentation.base.BaseMapFragment
import com.example.findit.presentation.extension.getBitmapDescriptorFromVector
import com.example.findit.presentation.extension.getIconRes
import com.example.findit.presentation.extension.launchCoroutine
import com.example.findit.presentation.extension.showSnackBar
import com.example.findit.presentation.map.PostClusterItem
import com.example.findit.presentation.map.PostClusterRenderer
import com.example.findit.presentation.screen.view_location.ViewLocationEvent
import com.example.findit.presentation.screen.view_location.ViewLocationFragmentArgs
import com.example.findit.presentation.screen.view_location.ViewLocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LocationFragment : BaseMapFragment<FragmentLocationBinding>(FragmentLocationBinding::inflate),
    OnMapReadyCallback {


    private val viewModel: LocationViewModel by viewModels()

    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<PostClusterItem>


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
            childFragmentManager.findFragmentById(R.id.mapLocationsFragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.onEvent(LocationEvent.GetPosts)
        setUpClustering(map)
        observers()
    }


    private fun observers() {
        launchCoroutine {
            viewModel.state.collect { state ->
                if (::map.isInitialized && state.currentUserLocation != null) {

                    val userLocation = state.currentUserLocation
                    map.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title(getString(R.string.your_location))
                            .icon(requireContext().getBitmapDescriptorFromVector(R.drawable.me_icon, 120, 120))
                    )
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                }
                if (::map.isInitialized && state.posts.isNotEmpty()) {
                    val clusterItems = state.posts.map { post ->
                        PostClusterItem(
                            postId = post.postId,
                            postType = post.postType,
                            latLng = post.location,
                            title = post.userFullName,
                            snippet = post.description
                        )
                    }
                    showPostsOnMap(clusterItems)
                }
            }
        }
        launchCoroutine {
            viewModel.event.collectLatest { effect ->
                when (effect) {
                    is LocationEffect.OpenBottomSheet -> {
                        val action = LocationFragmentDirections.actionLocationFragmentToMarkerBottomSheet(effect.postId, effect.userName, effect.description)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }


    private fun showPostsOnMap(posts: List<PostClusterItem>) {
        clusterManager.clearItems()
        clusterManager.addItems(posts)
        clusterManager.cluster()
    }


    private fun fetchCurrentUser() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        viewModel.onEvent(LocationEvent.SetCurrentUserLocation(latLng))
                    }
                }
                .addOnFailureListener {
                    binding.root.showSnackBar(getString(R.string.failed_to_get_location))
                }
        } catch (e: SecurityException) {
            binding.root.showSnackBar(getString(R.string.location_permission_denied))
        }
    }



    @SuppressLint("PotentialBehaviorOverride")
    private fun setUpClustering(map: GoogleMap) {
        clusterManager = ClusterManager(requireContext(), map)

        val renderer = PostClusterRenderer(requireContext(), map, clusterManager)
        clusterManager.renderer = renderer

        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { item ->
            viewModel.onEvent(LocationEvent.OpenBottomSheet(item.postId, item.title, item.snippet))
            true
        }
    }



}