package com.example.findit.presentation.map


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.example.findit.R
import com.example.findit.presentation.extension.getBitmapDescriptorFromVector
import com.example.findit.presentation.extension.getIconRes
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class PostClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<PostClusterItem>
) : DefaultClusterRenderer<PostClusterItem>(context, map, clusterManager) {

    private val iconGenerator = IconGenerator(context)
    private val clusterSizeCache = HashMap<Int, Bitmap>()
    init {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(Color.BLUE)
        shape.minimumWidth
        shape.setStroke(2, Color.WHITE)

        iconGenerator.setBackground(shape)
        iconGenerator.setTextAppearance(R.style.ClusterTextStyle)
    }

    override fun onBeforeClusterItemRendered(item: PostClusterItem, markerOptions: MarkerOptions) {
        val icon = context.getBitmapDescriptorFromVector(item.postType.getIconRes())
        markerOptions
            .title(item.title)
            .snippet(item.snippet)
            .icon(icon)
    }

    override fun onBeforeClusterRendered(cluster: Cluster<PostClusterItem>, markerOptions: MarkerOptions) {
        val marker = getClusterBitmap(cluster.size)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(marker))
    }


    private fun getClusterBitmap(count: Int): Bitmap {
        return clusterSizeCache[count] ?: run {
            val bitmap = iconGenerator.makeIcon(count.toString())
            clusterSizeCache[count] = bitmap
            bitmap
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<PostClusterItem>): Boolean {
        return cluster.size > 1
    }
}