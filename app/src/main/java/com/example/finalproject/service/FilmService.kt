package com.example.finalproject.service


import com.example.finalproject.model.ApiResponse
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Film
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface FilmService {
    @GET("film")
    suspend fun getFilm(): Response<ApiResponse<List<Film>>>

    @GET("film/{id_film}")
    suspend fun getFilmById(@Path("id_film") id_film: Int): Response<ApiResponseSingle<Film>>

    @POST("film/input")
    suspend fun insertFilm(@Body film: Film): Response<Unit>

    @PUT("film/{id_film}")
    suspend fun updateFilm(@Path("id_film") id_film: Int, @Body film: Film): Response<Unit>

    @DELETE("film/{id_film}")
    suspend fun deleteFilm(@Path("id_film") id_film: Int): Response<Unit>
}