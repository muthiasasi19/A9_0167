package com.example.finalproject.repository

import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Film
import com.example.finalproject.service.FilmService
import retrofit2.Response
import java.io.IOException


interface FilmRepository {
    suspend fun getFilm(): Response<List<Film>>
    suspend fun getFilmById(id_film: Int): Response<ApiResponseSingle<Film>>
    suspend fun insertFilm(film: Film)
    suspend fun updateFilm(id_film: Int, film: Film)
    suspend fun deleteFilm(id_film: Int)
}

class NetworkFilmRepository(
    private val filmService: FilmService
) : FilmRepository {
    override suspend fun getFilm(): Response<List<Film>> {
        val response = filmService.getFilm()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                // Mengembalikan Respon yang berisi data
                return Response.success(apiResponse.data)
            } else {
                throw IOException("Gagal mengambil data: ${response.message()}")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    override suspend fun getFilmById(id_film: Int): Response<ApiResponseSingle<Film>> {
        val response = filmService.getFilmById(id_film)
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                return response
            } else {
                throw IOException("Data film tidak ditemukan")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    override suspend fun insertFilm(film: Film) {
        val response = filmService.insertFilm(film)
        if (!response.isSuccessful) {
            throw IOException("Gagal menyimpan data: ${response.message()}")
        }
    }

    override suspend fun updateFilm(id_film: Int, film: Film) {
        val response = filmService.updateFilm(id_film, film)
        if (!response.isSuccessful) {
            throw IOException("Gagal mengupdate data: ${response.message()}")
        }
    }

    override suspend fun deleteFilm(id_film: Int) {
        val response = filmService.deleteFilm(id_film)
        if (!response.isSuccessful) {
            throw IOException("Gagal menghapus data: ${response.message()}")
        }
    }
}