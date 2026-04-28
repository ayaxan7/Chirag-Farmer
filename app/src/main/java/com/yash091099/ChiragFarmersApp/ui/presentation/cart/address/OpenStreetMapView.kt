package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.yash091099.ChiragFarmersApp.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(
    modifier: Modifier = Modifier,
    currentLocation: GeoPoint?,
    onMapLongClick: (GeoPoint) -> Unit = {},
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(20.0)
                val drawable = ContextCompat.getDrawable(context, R.drawable.osmpin)
                val scaledDrawable = drawable?.apply {
                    setBounds(0, 0, 2, 2) // width, height in px
                }
                // Set initial location or default to Delhi
                val startLocation = currentLocation ?: GeoPoint(28.6448, 77.2167)
                controller.setCenter(startLocation)

                // Add marker for current location
                if (currentLocation != null) {
                    val marker = Marker(this).apply {
                        icon = scaledDrawable
                        position = currentLocation
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(marker)
                }

                // Handle map click for placing marker
                setOnTouchListener { v, event ->
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> {
                            val projection = (v as MapView).projection
                            val point = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                            onMapLongClick(point)
                            v.performClick()
                        }
                    }
                    false
                }
            }
        },
        update = { mapView ->
            // Update map when location changes
            if (currentLocation != null) {
                mapView.overlays.clear()
                val marker = Marker(mapView).apply {
                    icon = mapView.context.getDrawable(R.drawable.osmpin)
                    position = currentLocation
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                mapView.overlays.add(marker)
                mapView.controller.setCenter(currentLocation)
                mapView.invalidate()
            }
        }
    )
}


