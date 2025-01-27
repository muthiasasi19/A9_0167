package com.example.finalproject

import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.NetworkFilmRepository
import com.example.finalproject.repository.NetworkStudioRepository
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.repository.PenayanganRepository
import com.example.finalproject.repository.NetworkPenayanganRepository
import com.example.finalproject.repository.NetworkTiketRepository
import com.example.finalproject.repository.TiketRepository
import com.example.finalproject.service.FilmService
import com.example.finalproject.service.StudioService
import com.example.finalproject.service.PenayanganService
import com.example.finalproject.service.TiketService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object BioskopContainer {
    private val baseUrl = "http://10.0.2.2:3001/api/" // untuk HP 192.168.1.10
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Service untuk Film
    private val filmService: FilmService by lazy { retrofit.create(FilmService::class.java) }

    // Repository untuk Film
    val filmRepository: FilmRepository by lazy { NetworkFilmRepository(filmService) }

    // Service untuk Studio
    private val studioService: StudioService by lazy { retrofit.create(StudioService::class.java) }

    // Repository untuk Studio
    val studioRepository: StudioRepository by lazy { NetworkStudioRepository(studioService) }

    // Service untuk Penayangan
    private val penayanganService: PenayanganService by lazy { retrofit.create(PenayanganService::class.java) }

    // Repository untuk Penayangan
    val penayanganRepository: PenayanganRepository by lazy {
        NetworkPenayanganRepository(
            penayanganService = penayanganService,
            filmRepository = filmRepository, // Berikan nilai untuk filmRepository
            studioRepository = studioRepository // Berikan nilai untuk studioRepository
        )
    }
    // Service untuk Tiket
    private val tiketService: TiketService by lazy { retrofit.create(TiketService::class.java) }

    // Repository untuk Tiket
    val tiketRepository: TiketRepository by lazy { NetworkTiketRepository(tiketService) }
}