package com.example.finalproject.service

import com.example.finalproject.model.ApiResponse
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Studio
import retrofit2.Response
import retrofit2.http.*

interface StudioService {
    @GET("studio")
    suspend fun getStudios(): Response<ApiResponse<List<Studio>>>

    @GET("studio/{id_studio}")
    suspend fun getStudioById(@Path("id_studio") id_studio: Int): Response<ApiResponseSingle<Studio>>

    @POST("studio/input")
    suspend fun insertStudio(@Body studio: Studio): Response<Unit>

    @PUT("studio/{id_studio}")
    suspend fun updateStudio(@Path("id_studio") id_studio: Int, @Body studio: Studio): Response<Unit>

    @DELETE("studio/{id_studio}")
    suspend fun deleteStudio(@Path("id_studio") id_studio: Int): Response<Unit>
}