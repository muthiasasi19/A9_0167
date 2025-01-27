package com.example.finalproject.model

import kotlinx.serialization.Serializable


@Serializable
data class Studio(
    val id_studio: Int,
    val nama_studio: String,
    val kapasitas: Int
)
