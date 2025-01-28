package com.example.finalproject.model

import kotlinx.serialization.Serializable

@Serializable
data class Tiket(
    val id_tiket: Int = 0, // karena di tabelnya auto increment
    val jumlah_tiket: Int,
    val total_harga: Double,
    val status_pembayaran: String,
    val id_penayangan:  Int? = null

)