package com.eugene.testtaskfortechspire.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PointUiModel(
    val x: Float,
    val y: Float
) : Parcelable
