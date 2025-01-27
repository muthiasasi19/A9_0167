package com.example.finalproject.service

import com.example.finalproject.model.ApiResponse
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Tiket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TiketService {
    @GET("tiket")
    suspend fun getAllTiket(): Response<ApiResponse<List<Tiket>>>

    @GET("tiket/{id_tiket}")
    suspend fun getTiketById(@Path("id_tiket") id_tiket: Int): Response<ApiResponseSingle<Tiket>>

    @POST("tiket/input")
    suspend fun insertTiket(@Body tiket: Tiket): Response<ApiResponseSingle<Tiket>>

    @PUT("tiket/{id_tiket}")
    suspend fun updateTiket(@Path("id_tiket") id_tiket: Int, @Body tiket: Tiket): Response<ApiResponseSingle<Tiket>>

    @DELETE("tiket/{id_tiket}")
    suspend fun deleteTiket(@Path("id_tiket") id_tiket: Int): Response<Void>
}