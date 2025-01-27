package com.example.finalproject.model


import kotlinx.serialization.Serializable

/*
 * Model untuk respons API yang mengembalikan daftar film.
 * Digunakan untuk endpoint seperti /film/.
 */

@Serializable
data class ApiResponse<T>(     //T adalah singkatan dari "Type"
    val status: Boolean,      // Status respons (true/false)
    val message: String,      // Pesan dari server
    val data: T               // Data (bisa berupa objek atau list)
)


/*
 * Model untuk respons API yang mengembalikan satu objek Film.
 * Digunakan untuk endpoint seperti /film/{id_film}.
 */

@Serializable
data class ApiResponseSingle<T>( //T itu singkatan dari "Type"
    val status: Boolean,      // Status respons (true/false)
    val message: String,      // Pesan dari server
    val data: T               // Data (objek tunggal)
)