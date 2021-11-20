package com.tutorial.runningapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tutorial.runningapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setup.*

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvContinue.setOnClickListener { findNavController().navigate(R.id.action_setupFragment_to_runFragment) }
    }
}