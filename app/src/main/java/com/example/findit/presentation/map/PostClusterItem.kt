package com.example.findit.presentation.map

import com.example.findit.domain.model.PostType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PostClusterItem(
    val postId: String = "",
    private val latLng: LatLng,
    private val title: String,
    private val snippet: String = "",
    val postType: PostType = PostType.LOST
) : ClusterItem {
    override fun getPosition(): LatLng = latLng
    override fun getTitle(): String = title
    override fun getSnippet(): String = snippet
    override fun getZIndex(): Float? = null
}
