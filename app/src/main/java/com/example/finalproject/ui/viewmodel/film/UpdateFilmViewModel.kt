package com.example.finalproject.ui.viewmodel.film


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.ui.view.film.DestinasiEditFilm
import kotlinx.coroutines.launch

class UpdateFilmViewModel(
    savedStateHandle: SavedStateHandle,
    private val filmRepository: FilmRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertFilmUiState())
        private set

    private val id_film: Int = checkNotNull(savedStateHandle[DestinasiEditFilm.id_film])

    init {
        viewModelScope.launch {
            try {
                // Ambil Response<ApiResponseSingle<Film>> dari repository
                val response = filmRepository.getFilmById(id_film)

                // Untuk memeriksa apakah response berhasil dan data tidak null
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        val film = apiResponse.data
                        if (film != null) {
                            uiState = film.toUiStateFilm()
                        } else {
                            println("Data film null.")
                        }
                    } else {
                        println("API response tidak valid: ${apiResponse?.message}")
                    }
                } else {
                    println("Gagal mengambil data: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Terjadi kesalahan saat mengambil data film: ${e.message}")
            }
        }
    }

    fun updateInsertFilmState(insertFilmUiEvent: InsertFilmUiEvent) {
        uiState = InsertFilmUiState(insertFilmUiEvent = insertFilmUiEvent)
    }

    suspend fun updateFilm(): Boolean {
        return try {
            val film = uiState.insertFilmUiEvent.toFilm()
            filmRepository.updateFilm(id_film, film)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}