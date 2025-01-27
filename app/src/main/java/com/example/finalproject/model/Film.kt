package com.example.finalproject.model

import kotlinx.serialization.Serializable

//Kenapa harus pake ApiResponse?
@Serializable
data class Film(
    val id_film: Int,         // ID film
    val judul_film: String,   // Judul film
    val durasi: Int,          // Durasi film (dalam menit)
    val deskripsi: String,    // Deskripsi film
    val genre: String,        // Genre film
    val rating_usia: String   // Rating usia film (contoh:  "13+", "17+")
)


