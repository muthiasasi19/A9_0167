package com.example.finalproject.ui.viewmodel.Tiket


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finalproject.model.Tiket
import com.example.finalproject.repository.TiketRepository

class InsertTiketViewModel(
    private val tiketRepository: TiketRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertTiketUiState())
        private set

    // Fungsi untuk mengupdate state
    fun updateInsertTiketState(insertTiketUiEvent: InsertTiketUiEvent) {
        uiState = InsertTiketUiState(insertTiketUiEvent = insertTiketUiEvent)
    }

    // Fungsi untuk menghitung total harga otomatis
    fun calculateTotalHarga(jumlahTiket: String, hargaTiket: Double) {
        val jumlah = jumlahTiket.toIntOrNull() ?: 0
        val totalHarga = jumlah * hargaTiket
        updateInsertTiketState(
            uiState.insertTiketUiEvent.copy(
                jumlah_tiket = jumlahTiket,
                total_harga = totalHarga.toString() // Update total harga otomatis
            )
        )
    }

    suspend fun insertTiket(): Boolean {
        return try {
            // Validasi data
            if (uiState.insertTiketUiEvent.jumlah_tiket.isEmpty() ||
                uiState.insertTiketUiEvent.status_pembayaran.isEmpty()
            ) {
                throw IllegalArgumentException("Jumlah tiket dan status pembayaran harus diisi")
            }

            // Buat objek Tiket
            val tiket = uiState.insertTiketUiEvent.toTiket()

            // Log data yang akan dikirim ke API
            Log.d("InsertTiketViewModel", "Data yang dikirim ke API: $tiket")

            // Simpan data tiket
            tiketRepository.insertTiket(tiket)
            true // Berhasil
        } catch (e: Exception) {
            Log.e("InsertTiketViewModel", "Gagal menyimpan data: ${e.message}", e)
            false // Gagal
        }
    }

    // State untuk InsertTiket
    data class InsertTiketUiState(
        val insertTiketUiEvent: InsertTiketUiEvent = InsertTiketUiEvent(),
    )

    // Event untuk InsertTiket
    data class InsertTiketUiEvent(
        val jumlah_tiket: String = "",
        val total_harga: String = "",
        val status_pembayaran: String = "",
        val id_penayangan: String = ""
    ) {
        fun toTiket(): Tiket {
            return Tiket(
                id_tiket = 0, // Tidak perlu mengirim id_tiket
                jumlah_tiket = jumlah_tiket.toIntOrNull() ?: 0,
                total_harga = total_harga.toDoubleOrNull() ?: 0.0,
                status_pembayaran = status_pembayaran,
                id_penayangan = id_penayangan.toIntOrNull() ?: 0
            )
        }
    }
}