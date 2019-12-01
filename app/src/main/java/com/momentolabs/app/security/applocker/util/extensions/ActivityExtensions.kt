package com.momentolabs.app.security.applocker.util.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.widget.EditText


fun Activity.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, HIDE_IMPLICIT_ONLY)
}

fun Activity.hideKeyboard(editText: EditText) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}