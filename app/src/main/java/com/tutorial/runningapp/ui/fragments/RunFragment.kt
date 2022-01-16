package com.tutorial.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.tutorial.runningapp.R
import com.tutorial.runningapp.adapter.RunningAdapter
import com.tutorial.runningapp.ui.viewmodels.MainViewModel
import com.tutorial.runningapp.utils.LocationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val RC_LOCATION_PERMISSION = 1001

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {
    private val mainViewModel: MainViewModel by viewModels()
    private val runAdapter by lazy { RunningAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener { findNavController().navigate(R.id.action_runFragment_to_trackingFragment) }
        requestLocationPermissions()
        setupRecyclerView()

        mainViewModel.runSortedByDate.observe(viewLifecycleOwner){
            runAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        rvRuns.apply {
            adapter = runAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun requestLocationPermissions() {
        LocationUtils.requestLocationPermissions(this, RC_LOCATION_PERMISSION)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}