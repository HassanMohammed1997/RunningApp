package com.tutorial.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tutorial.runningapp.R
import com.tutorial.runningapp.utils.Constants
import com.tutorial.runningapp.utils.extensions.extractText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserDataFromSharedPref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangedToSharedPref()
            if (success) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.msg_data_saved_successfully),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.msg_enter_all_fields),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadUserDataFromSharedPref() {
        val name = sharedPref.getString(Constants.KEY_NAME, "")
        val weight = sharedPref.getFloat(Constants.KEY_WEIGHT, 80f)
        etName.setText(name)
        etWeight.setText(weight.toString())
    }

    private fun applyChangedToSharedPref(): Boolean {
        val name = etName.extractText()
        val weight = etWeight.extractText()
        if (name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit {
            putString(Constants.KEY_NAME, name)
            putFloat(Constants.KEY_WEIGHT, weight.toFloat())
            apply()
        }

        val toolbarText = getString(R.string.lets_go, name)
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}