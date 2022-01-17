package com.tutorial.runningapp.utils.extensions

import android.widget.EditText

fun EditText.extractText() = this.text?.trim().toString()