package com.example.finalproject.repository

import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Studio
import com.example.finalproject.service.StudioService
import retrofit2.Response
import java.io.IOException

interface StudioRepository {
    suspend fun getStudios(): Response<List<Studio>>
    suspend fun getStudioById(id_studio: Int): Response<ApiResponseSingle<Studio>>
    suspend fun insertStudio(studio: Studio)
    suspend fun updateStudio(id_studio: Int, studio: Studio): Boolean
    suspend fun deleteStudio(id_studio: Int)
}

class NetworkStudioRepository(
    private val studioService: StudioService
) : StudioRepository {
    override suspend fun getStudios(): Response<List<Studio>> {
        val response = studioService.getStudios()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                return Response.success(apiResponse.data)
            } else {
                throw IOException("Gagal mengambil data: ${response.message()}")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    override suspend fun getStudioById(id_studio: Int): Response<ApiResponseSingle<Studio>> {
        val response = studioService.getStudioById(id_studio)
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null) {
                return response
            } else {
                throw IOException("Data studio tidak ditemukan")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    override suspend fun insertStudio(studio: Studio) {
        val response = studioService.insertStudio(studio)
        if (!response.isSuccessful) {
            throw IOException("Gagal menyimpan data: ${response.message()}")
        }
    }

    override suspend fun updateStudio(id_studio: Int, studio: Studio): Boolean {
        return try {
            val response = studioService.updateStudio(id_studio, studio)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteStudio(id_studio: Int) {
        val response = studioService.deleteStudio(id_studio)
        if (!response.isSuccessful) {
            throw IOException("Gagal menghapus data: ${response.message()}")
        }
    }
}