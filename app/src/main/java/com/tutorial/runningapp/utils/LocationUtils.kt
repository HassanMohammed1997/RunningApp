package com.tutorial.runningapp.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.tutorial.runningapp.R
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
}