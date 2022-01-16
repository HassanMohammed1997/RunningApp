package com.tutorial.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.tutorial.runningapp.R
import com.tutorial.runningapp.adapters.RunningAdapter
import com.tutorial.runningapp.enums.SortTypes
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

        requestLocationPermissions()
        setupRecyclerView()
        subscribeObservers()
        setupSortSpinner()
        setListenerForViews()
    }

    private fun setListenerForViews() {
        fab.setOnClickListener { findNavController().navigate(R.id.action_runFragment_to_trackingFragment) }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mainViewModel.sort(SortTypes.getSortType(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun subscribeObservers() {
        mainViewModel.runSortedByDate.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }

        mainViewModel.runs.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }
    }

    private fun setupSortSpinner() {
        when (mainViewModel.sortType) {
            SortTypes.DATE -> spFilter.setSelection(0)
            SortTypes.RUNNING_TIME -> spFilter.setSelection(1)
            SortTypes.AVG_SPEED -> spFilter.setSelection(2)
            SortTypes.DISTANCE -> spFilter.setSelection(3)
            SortTypes.CALORIES_BURNED -> spFilter.setSelection(4)
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