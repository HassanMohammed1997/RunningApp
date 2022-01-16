package com.tutorial.runningapp.utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment
import com.tutorial.runningapp.R
import com.tutorial.runningapp.service.Polyline
import pub.devrel.easypermissions.EasyPermissions

object LocationUtils {
    fun hasLocationPermission(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }


    fun requestLocationPermissions(
        fragment: Fragment,
        requestCode: Int,
    ) {
        if (hasLocationPermission(fragment.requireContext())) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                fragment,
                fragment.getString(R.string.msg_location_permission_rotional),
                requestCode,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        } else {
            EasyPermissions.requestPermissions(
                fragment,
                fragment.getString(R.string.msg_location_permission_rotional),
                requestCode,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }


    }

    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f

        for (i in 0..polyline.size - 2) {
            val position1 = polyline[i]
            val position2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                position1.latitude,
                position1.longitude,
                position2.latitude,
                position2.longitude,
                result
            )

            distance += result[0]
        }

        return distance
    }
}