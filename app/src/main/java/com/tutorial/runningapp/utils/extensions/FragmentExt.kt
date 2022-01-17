package com.tutorial.runningapp.utils.extensions

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.invalidateOptionMenu() = requireActivity().invalidateOptionsMenu()

fun Fragment.getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(requireContext(), colorRes)