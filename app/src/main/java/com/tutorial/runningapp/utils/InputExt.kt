package com.tutorial.runningapp.utils

import android.widget.EditText

fun EditText.extractText() = this.text?.trim().toString()