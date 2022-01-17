package com.tutorial.runningapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tutorial.runningapp.R

class CancelTrackingDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: () -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.title_cancel_run)
            .setMessage(R.string.msg_cancel_run)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                yesListener?.let { yes -> yes() }
            }.setNeutralButton(android.R.string.cancel) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}