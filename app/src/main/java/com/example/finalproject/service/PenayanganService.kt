package com.example.finalproject.service


import com.example.finalproject.model.ApiResponse
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Penayangan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PenayanganService {
    @GET("penayangan")
    suspend fun getAllPenayangan(): Response<ApiResponse<List<Penayangan>>>

    @GET("penayangan/{id_penayangan}")
    suspend fun getPenayanganById(@Path("id_penayangan") id_penayangan: Int): Response<ApiResponseSingle<Penayangan>>

    @POST("penayangan/input")
    suspend fun insertPenayangan(@Body penayangan: Penayangan): Response<Unit>

    @PUT("penayangan/{id_penayangan}")
    suspend fun updatePenayangan(@Path("id_penayangan") id_penayangan: Int, @Body penayangan: Penayangan): Response<Unit>

    @DELETE("penayangan/{id_penayangan}")
    suspend fun deletePenayangan(@Path("id_penayangan") id_penayangan: Int): Response<Unit>
}