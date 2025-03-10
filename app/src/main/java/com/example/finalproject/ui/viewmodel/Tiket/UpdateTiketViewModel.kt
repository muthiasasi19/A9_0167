package com.example.finalproject.ui.viewmodel.Tiket


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Tiket
import com.example.finalproject.repository.TiketRepository
import kotlinx.coroutines.launch
import java.io.IOException

class UpdateTiketViewModel(
    private val tiketRepository: TiketRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdateTiketUiState())
        private set

    // Fungsi untuk menghitung total harga otomatis
    fun calculateTotalHarga(jumlahTiket: String, hargaTiket: Double) {
        val jumlah = jumlahTiket.toIntOrNull() ?: 0
        val totalHarga = jumlah * hargaTiket
        updateUpdateTiketState(
            uiState.updateTiketUiEvent.copy(
                jumlah_tiket = jumlahTiket,
                total_harga = totalHarga.toString() // Update total harga otomatis
            )
        )
    }

    // Fungsi untuk mengupdate state
    fun updateUpdateTiketState(updateTiketUiEvent: UpdateTiketUiEvent) {
        uiState = UpdateTiketUiState(updateTiketUiEvent = updateTiketUiEvent)
    }

    // Fungsi untuk memuat data tiket
    fun loadTiket(idTiket: Int) {
        viewModelScope.launch {
            try {
                val response = tiketRepository.getTiketById(idTiket)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        val tiket = apiResponse.data
                        uiState = UpdateTiketUiState(updateTiketUiEvent = UpdateTiketUiEvent.fromTiket(tiket))
                    } else {
                        throw IOException("Data tiket tidak ditemukan atau status false")
                    }
                } else {
                    throw IOException("Gagal mengambil data: ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e("UpdateTiketViewModel", "Gagal memuat data tiket: ${e.message}", e)
            }
        }
    }

    // Fungsi untuk menyimpan perubahan tiket
    suspend fun updateTiket(): Boolean {
        return try {
            // Validasi data
            if (uiState.updateTiketUiEvent.jumlah_tiket.isEmpty() ||
                uiState.updateTiketUiEvent.total_harga.isEmpty() ||
                uiState.updateTiketUiEvent.status_pembayaran.isEmpty()
            ) {
                throw IllegalArgumentException("Semua field harus diisi")
            }

            // Buat objek Tiket
            val tiket = uiState.updateTiketUiEvent.toTiket()

            // Log data yang akan dikirim ke API
            Log.d("UpdateTiketViewModel", "Data yang dikirim ke API: $tiket")

            // Update data tiket
            tiketRepository.updateTiket(tiket.id_tiket, tiket)
            true // Berhasil
        } catch (e: Exception) {
            Log.e("UpdateTiketViewModel", "Gagal menyimpan data: ${e.message}", e)
            false // Gagal
        }
    }

    // State untuk UpdateTiket
    data class UpdateTiketUiState(
        val updateTiketUiEvent: UpdateTiketUiEvent = UpdateTiketUiEvent(),
    )

    // Event untuk UpdateTiket
    data class UpdateTiketUiEvent(
        val id_tiket: Int = 0,
        val jumlah_tiket: String = "",
        val total_harga: String = "",
        val status_pembayaran: String = "",
        val id_penayangan: String = ""
    ) {
        // Fungsi untuk mengkonversi UiEvent ke objek Tiket
        fun toTiket(): Tiket {
            return Tiket(
                id_tiket = id_tiket,
                jumlah_tiket = jumlah_tiket.toIntOrNull() ?: 0,
                total_harga = total_harga.toDoubleOrNull() ?: 0.0,
                status_pembayaran = status_pembayaran,
                id_penayangan = id_penayangan.toIntOrNull() ?: 0
            )
        }

        companion object {

            fun fromTiket(tiket: Tiket): UpdateTiketUiEvent {
                return UpdateTiketUiEvent(
                    id_tiket = tiket.id_tiket,
                    jumlah_tiket = tiket.jumlah_tiket.toString(),
                    total_harga = tiket.total_harga.toString(),
                    status_pembayaran = tiket.status_pembayaran,
                    id_penayangan = tiket.id_penayangan?.toString() ?: ""
                )
            }
        }
    }
}