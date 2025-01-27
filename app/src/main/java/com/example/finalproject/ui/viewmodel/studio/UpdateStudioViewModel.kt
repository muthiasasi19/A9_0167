package com.example.finalproject.ui.viewmodel.studio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.ui.navigation.DestinasiEditStudio
import kotlinx.coroutines.launch

class UpdateStudioViewModel(
    savedStateHandle: SavedStateHandle,
    private val studioRepository: StudioRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertStudioUiState())
        private set

    // Ambil id_studio dari argument navigasi
    val idStudio: Int = checkNotNull(savedStateHandle[DestinasiEditStudio.id_studio]).toString().toIntOrNull()
        ?: throw IllegalArgumentException("Invalid id_studio")

    // Fungsi untuk memuat data studio saat halaman dibuka
    fun loadStudio() {
        viewModelScope.launch {
            try {

                val response = studioRepository.getStudioById(idStudio)

                // untuk memeriksa apakah response berhasil
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        val studio = apiResponse.data
                        if (studio != null) {
                            uiState = InsertStudioUiState(
                                insertStudioUiEvent = InsertStudioUiEvent(
                                    id_studio = studio.id_studio.toString(),
                                    nama_studio = studio.nama_studio,
                                    kapasitas = studio.kapasitas.toString()
                                )
                            )
                        } else {
                            println("Data studio null.")
                        }
                    } else {
                        println("API response tidak valid: ${apiResponse?.message}")
                    }
                } else {
                    println("Gagal mengambil data: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Terjadi kesalahan saat mengambil data studio: ${e.message}")
            }
        }
    }

    // Fungsi untuk memperbarui state saat input berubah
    fun updateInsertStudioState(insertStudioUiEvent: InsertStudioUiEvent) {
        uiState = InsertStudioUiState(insertStudioUiEvent = insertStudioUiEvent)
    }

    // Fungsi untuk menyimpan perubahan data studio
    suspend fun updateStudio(): Boolean {
        return try {
            val studio = uiState.insertStudioUiEvent.toStudio()
            studioRepository.updateStudio(studio.id_studio, studio)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}