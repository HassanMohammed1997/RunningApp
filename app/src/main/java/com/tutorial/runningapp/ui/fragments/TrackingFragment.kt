package com.tutorial.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tutorial.runningapp.R
import com.tutorial.runningapp.data.db.RunEntity
import com.tutorial.runningapp.service.LocationTrackingService
import com.tutorial.runningapp.service.Polyline
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import com.tutorial.runningapp.ui.viewmodels.MainViewModel
import com.tutorial.runningapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private val mainViewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentElapsedTime = 0L
    private var weight = 60f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        LocationTrackingService.tickerInMs.observe(viewLifecycleOwner) {
            currentElapsedTime = it
            tvTimer.text = TimestampMillisecondsFormatter.format(currentElapsedTime)
        }

        btnFinishRun.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (position in polyline) {
                bounds.include(position)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.0f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bitmap ->
            var totalDistanceInMeter = 0
            for (polyline in pathPoints) {
                totalDistanceInMeter += LocationUtils.calculatePolylineLength(polyline).toInt()
            }

            val avgSpeedInKm =
                round((totalDistanceInMeter.toKm()) / (currentElapsedTime.toHour()) * 10) / 10f
            val timestamp = System.currentTimeMillis()
            val totalCaloriesBurned = ((totalDistanceInMeter / 1000f) * weight).toInt()

            val run = RunEntity(
                bitmap,
                timestamp,
                avgSpeedInKm,
                totalDistanceInMeter,
                currentElapsedTime,
                totalCaloriesBurned
            )
            mainViewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                getString(R.string.msg_run_saved_successfully),
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tracking, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_stop_running).isVisible = (currentElapsedTime > 0 && isTracking)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_stop_running) {
            openCancelRunDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
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
    }

    private fun toggleRun() {
        if (isTracking)
            sendCommandToLocationService(Constants.ACTION_PAUSE_SERVICE)
        else
            sendCommandToLocationService(Constants.ACTION_START_OR_RESUME_SERVICE)
        invalidateOptionMenu()
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

        invalidateOptionMenu()
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

    private fun openCancelRunDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setIcon(R.drawable.baseline_close_black_24dp)
            .setTitle(R.string.title_cancel_run)
            .setMessage(R.string.msg_cancel_run)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                stopRun()
            }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }.create().show()
    }

    private fun stopRun() {
        sendCommandToLocationService(Constants.ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }


}