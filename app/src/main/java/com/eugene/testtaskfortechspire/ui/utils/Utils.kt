package com.eugene.testtaskfortechspire.ui.utils

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment


fun Fragment.toast(text: String, lenght: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), text, lenght).show()
}

fun Fragment.string(strId: Int): String {
    return getString(strId)
}

fun View.string(strId: Int): String {
    return getString(context, strId)
}

fun View.color(strId: Int): Int {
    return ContextCompat.getColor(context, strId)
}