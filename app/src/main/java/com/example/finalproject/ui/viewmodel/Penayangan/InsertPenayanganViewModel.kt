package com.example.finalproject.ui.viewmodel.Penayangan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finalproject.model.Penayangan
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.PenayanganRepository
import com.example.finalproject.repository.StudioRepository

class InsertPenayanganViewModel(
    private val penayanganRepository: PenayanganRepository,
    private val filmRepository: FilmRepository,
    private val studioRepository: StudioRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertPenayanganUiState())
        private set

    fun updateInsertPenayanganState(insertPenayanganUiEvent: InsertPenayanganUiEvent) {
        uiState = InsertPenayanganUiState(insertPenayanganUiEvent = insertPenayanganUiEvent)
    }

    suspend fun insertPenayangan(): Boolean {
        return try {
            // Validasi data
            if (uiState.insertPenayanganUiEvent.id_penayangan.isEmpty() ||
                uiState.insertPenayanganUiEvent.id_film.isEmpty() ||
                uiState.insertPenayanganUiEvent.id_studio.isEmpty() ||
                uiState.insertPenayanganUiEvent.tanggal_penayangan.isEmpty() ||
                uiState.insertPenayanganUiEvent.harga_tiket.isEmpty()
            ) {
                throw IllegalArgumentException("Semua field harus diisi")
            }

            // Ambil judul film berdasarkan id_film
            val filmResponse = filmRepository.getFilmById(uiState.insertPenayanganUiEvent.id_film.toInt())
            val judulFilm = filmResponse.body()?.data?.judul_film ?: throw IllegalStateException("Judul film tidak ditemukan")
            Log.d("InsertPenayanganViewModel", "Film Response: ${filmResponse.body()}")

            // Ambil nama studio berdasarkan id_studio
            val studioResponse = studioRepository.getStudioById(uiState.insertPenayanganUiEvent.id_studio.toInt())
            val namaStudio = studioResponse.body()?.data?.nama_studio ?: throw IllegalStateException("Nama studio tidak ditemukan")
            Log.d("InsertPenayanganViewModel", "Studio Response: ${studioResponse.body()}")

            // Buat objek Penayangan
            val penayangan = uiState.insertPenayanganUiEvent.toPenayangan(judulFilm, namaStudio)

            // Log data yang akan dikirim ke API
            Log.d("InsertPenayanganViewModel", "Data yang dikirim ke API: $penayangan")

            // Simpan data penayangan
            penayanganRepository.insertPenayangan(penayangan)
            true
        } catch (e: Exception) {
            Log.e("InsertPenayanganViewModel", "Gagal menyimpan data: ${e.message}", e)
            false
        }
    }

    // State untuk InsertPenayangan
    data class InsertPenayanganUiState(
        val insertPenayanganUiEvent: InsertPenayanganUiEvent = InsertPenayanganUiEvent(),
    )

    // Event untuk InsertPenayangan
    data class InsertPenayanganUiEvent(
        val id_penayangan: String = "",
        val id_film: String = "",
        val id_studio: String = "",
        val tanggal_penayangan: String = "",
        val harga_tiket: String = "",
    ) {
        fun toPenayangan(judulFilm: String, namaStudio: String): Penayangan {
            return Penayangan(
                id_penayangan = id_penayangan.toIntOrNull() ?: 0,
                judul_film = judulFilm,
                nama_studio = namaStudio,
                tanggal_penayangan = tanggal_penayangan,
                harga_tiket = harga_tiket.toDoubleOrNull() ?: 0.0,
                id_film = id_film.toIntOrNull() ?: 0,
                id_studio = id_studio.toIntOrNull() ?: 0
            )
        }
    }
}
