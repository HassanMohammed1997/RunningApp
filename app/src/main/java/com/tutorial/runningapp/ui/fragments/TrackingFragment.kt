package com.tutorial.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.tutorial.runningapp.R
import com.tutorial.runningapp.service.LocationTrackingService
import com.tutorial.runningapp.service.Polyline
import com.tutorial.runningapp.ui.viewmodels.MainViewModel
import com.tutorial.runningapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import timber.log.Timber

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private val mainViewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { googleMap ->
            map = googleMap
            addAllPolylines()
        }

        subscribeObservers()

        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        LocationTrackingService.ticker.observe(viewLifecycleOwner) {
            tvTimer.text = it
        }

        btnFinishRun.setOnClickListener {
            sendCommandToLocationService(Constants.ACTION_STOP_SERVICE)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()

            val polylineOptions = PolylineOptions()
                .color(Constants.POLYLINE_COLOR)
                .width(Constants.POLYLINE_WIDTH)
                .add(preLastLatLng, lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun moveCameraToLatLng() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }
    }

    private fun subscribeObservers() {
        LocationTrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        LocationTrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToLatLng()
        }

        LocationTrackingService.ticker.observe(viewLifecycleOwner){
            Timber.d("Ticker: $it")
        }
    }

    private fun toggleRun() {
        if (isTracking)
            sendCommandToLocationService(Constants.ACTION_PAUSE_SERVICE)
        else
            sendCommandToLocationService(Constants.ACTION_START_OR_RESUME_SERVICE)
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggleRun.text = getString(R.string.title_start)
            btnFinishRun.isVisible = true
        } else {
            btnToggleRun.text = getString(R.string.title_stop)
            btnFinishRun.isVisible = false
        }


    }

    private fun addAllPolylines() {
        for (pathPoint in pathPoints) {
            val polylineOptions = PolylineOptions()
                .width(Constants.POLYLINE_WIDTH)
                .color(Constants.POLYLINE_COLOR)
                .addAll(pathPoint)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToLocationService(action: String) =
        Intent(requireContext(), LocationTrackingService::class.java)
            .also {
                it.action = action

                requireContext().startService(it)
            }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

}