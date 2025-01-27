package com.example.finalproject.ui.viewmodel.film


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Film
import com.example.finalproject.repository.FilmRepository
import kotlinx.coroutines.launch

class InsertFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {

    var uiState by mutableStateOf(InsertFilmUiState())
        private set

    fun updateInsertFilmState(insertFilmUiEvent: InsertFilmUiEvent) {
        uiState = InsertFilmUiState(insertFilmUiEvent = insertFilmUiEvent)
    }

    // Fungsi untuk menyimpan data film
    suspend fun insertFilm(): Boolean {
        return try {
            // Konversi InsertFilmUiEvent ke Film
            val film = uiState.insertFilmUiEvent.toFilm()

            // Panggil filmRepository.insertFilm
            filmRepository.insertFilm(film)

            true // Berhasil
        } catch (e: Exception) {
            e.printStackTrace()
            false // Gagal
        }
    }
}

// Fungsi-fungsi ini harus bersifat public
fun InsertFilmUiEvent.toFilm(): Film = Film(
    id_film = id_film.toIntOrNull() ?: 0,
    judul_film = judul_film,
    durasi = durasi.toIntOrNull() ?: 0,
    deskripsi = deskripsi,
    genre = genre,
    rating_usia = rating_usia
)

fun Film.toUiStateFilm(): InsertFilmUiState = InsertFilmUiState(
    insertFilmUiEvent = toInsertFilmUiEvent()
)

fun Film.toInsertFilmUiEvent(): InsertFilmUiEvent = InsertFilmUiEvent(
    id_film = id_film.toString(),
    judul_film = judul_film,
    durasi = durasi.toString(),
    deskripsi = deskripsi,
    genre = genre,
    rating_usia = rating_usia
)

data class InsertFilmUiState(
    val insertFilmUiEvent: InsertFilmUiEvent = InsertFilmUiEvent()
)

data class InsertFilmUiEvent(
    val id_film: String = "",
    val judul_film: String = "",
    val durasi: String = "",
    val deskripsi: String = "",
    val genre: String = "",
    val rating_usia: String = ""
)