package com.example.finalproject.repository

import android.util.Log
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Penayangan
import java.io.IOException
import com.example.finalproject.service.PenayanganService
import retrofit2.Response


interface PenayanganRepository  {
    suspend fun getAllPenayangan(): List<Penayangan>
    suspend fun getPenayanganById(id_penayangan: Int): Response<ApiResponseSingle<Penayangan>>
    suspend fun insertPenayangan(penayangan: Penayangan)
    suspend fun updatePenayangan(id_penayangan: Int, penayangan: Penayangan)
    suspend fun deletePenayangan(id_penayangan: Int)
}

class NetworkPenayanganRepository(
    private val penayanganService: PenayanganService,
    private val filmRepository: FilmRepository,
    private val studioRepository: StudioRepository
) : PenayanganRepository {

    override suspend fun getAllPenayangan(): List<Penayangan> {
        val response = penayanganService.getAllPenayangan()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                Log.d("NetworkPenayanganRepository", "Data penayangan yang diambil: ${apiResponse.data}")
                return apiResponse.data // Ambil data dari ApiResponse
            } else {
                throw IOException("Gagal mengambil data: ${response.message()}")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }


    override suspend fun getPenayanganById(id_penayangan: Int): Response<ApiResponseSingle<Penayangan>> {
        val response = penayanganService.getPenayanganById(id_penayangan)
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                return response
            } else {
                throw IOException("Data penayangan tidak ditemukan")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }


    override suspend fun insertPenayangan(penayangan: Penayangan) {
        // Log data yang akan dikirim ke API
        Log.d("NetworkPenayanganRepository", "Data yang dikirim ke API: $penayangan")

        val response = penayanganService.insertPenayangan(penayangan)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("NetworkPenayanganRepository", "Gagal menyimpan data: ${response.code()} - $errorBody")
            throw IOException("Gagal menyimpan data: ${response.code()} - $errorBody")
        }
    }

    override suspend fun updatePenayangan(id_penayangan: Int, penayangan: Penayangan) {
        val response = penayanganService.updatePenayangan(id_penayangan, penayangan)
        if (!response.isSuccessful) {
            throw IOException("Gagal mengupdate data: ${response.message()}")
        }
    }

    override suspend fun deletePenayangan(id_penayangan: Int) {
        val response = penayanganService.deletePenayangan(id_penayangan)
        if (!response.isSuccessful) {
            throw IOException("Gagal menghapus data: ${response.message()}")
        }
    }
}