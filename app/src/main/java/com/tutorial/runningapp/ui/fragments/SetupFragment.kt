package com.tutorial.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tutorial.runningapp.R
import com.tutorial.runningapp.utils.Constants
import com.tutorial.runningapp.utils.extensions.extractText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {
    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpened = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isFirstAppOpened) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success)
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            else {
                Snackbar.make(requireView(), getString(R.string.msg_enter_all_fields), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = etName.extractText()
        val weight = etWeight.extractText()
        if (name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit {
            putBoolean(Constants.KEY_FIRST_TIME_TOGGLE, false)
            putString(Constants.KEY_NAME, name)
            putFloat(Constants.KEY_WEIGHT, weight.toFloat())
            apply()
        }

        val toolbarTitle = getString(R.string.lets_go, name)
        requireActivity().tvToolbarTitle.text = toolbarTitle
        return true


    }
}