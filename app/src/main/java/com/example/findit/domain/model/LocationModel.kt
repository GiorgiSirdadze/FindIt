package com.example.findit.domain.model

import androidx.annotation.Keep

@Keep
data class LocationModel(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
