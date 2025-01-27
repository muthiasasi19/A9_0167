package com.example.finalproject.model


import kotlinx.serialization.Serializable


@Serializable
data class Penayangan(
    val id_penayangan: Int,
    val tanggal_penayangan: String,
    val harga_tiket: Double,
    val judul_film: String,
    val nama_studio: String,
    val id_film: Int? = null,
    val id_studio: Int? = null
)

