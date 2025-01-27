package com.example.finalproject.repository


import android.util.Log
import com.example.finalproject.model.ApiResponseSingle
import com.example.finalproject.model.Tiket
import com.example.finalproject.service.TiketService
import retrofit2.Response
import java.io.IOException

interface TiketRepository {
    suspend fun getAllTiket(): List<Tiket>
    suspend fun getTiketById(id_tiket: Int): Response<ApiResponseSingle<Tiket>>
    suspend fun insertTiket(tiket: Tiket)
    suspend fun updateTiket(id_tiket: Int, tiket: Tiket)
    suspend fun deleteTiket(id_tiket: Int)
}

// Implementasi TiketRepository
class NetworkTiketRepository(
    private val tiketService: TiketService
) : TiketRepository {

    override suspend fun getAllTiket(): List<Tiket> {
        val response = tiketService.getAllTiket()
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                Log.d("NetworkTiketRepository", "Data tiket yang diambil: ${apiResponse.data}")
                return apiResponse.data // Ambil data dari ApiResponse
            } else {
                throw IOException("Gagal mengambil data: ${response.message()}")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    override suspend fun getTiketById(id_tiket: Int): Response<ApiResponseSingle<Tiket>> {
        val response = tiketService.getTiketById(id_tiket)
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.status) {
                return response
            } else {
                throw IOException("Data tiket tidak ditemukan")
            }
        } else {
            throw IOException("Gagal mengambil data: ${response.message()}")
        }
    }

    //untuk tambah data tiket
    override suspend fun insertTiket(tiket: Tiket) {
        // Log data yang dikirim ke API
        Log.d("NetworkTiketRepository", "Data yang dikirim ke API: $tiket")

        // Kirim request ke API
        val response = tiketService.insertTiket(tiket)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("NetworkTiketRepository", "Gagal menyimpan data: ${response.code()} - $errorBody")
            throw IOException("Gagal menyimpan data: ${response.code()} - $errorBody")
        }
    }

    override suspend fun updateTiket(id_tiket: Int, tiket: Tiket) {
        val response = tiketService.updateTiket(id_tiket, tiket)
        if (!response.isSuccessful) {
            throw IOException("Gagal mengupdate data: ${response.message()}")
        }
    }

    override suspend fun deleteTiket(id_tiket: Int) {
        val response = tiketService.deleteTiket(id_tiket)
        if (!response.isSuccessful) {
            throw IOException("Gagal menghapus data: ${response.message()}")
        }
    }
}


