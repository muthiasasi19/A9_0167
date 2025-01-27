package com.example.finalproject.ui.viewmodel.film

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Film
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.ui.view.film.DestinasiDetailFilm
import kotlinx.coroutines.launch
import java.io.IOException


sealed class DetailFilmUiState {
    data class Success(val film: Film) : DetailFilmUiState()
    object Error : DetailFilmUiState()
    object Loading : DetailFilmUiState()
}

class DetailFilmViewModel(
    savedStateHandle: SavedStateHandle,
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val id_film: Int = checkNotNull(savedStateHandle[DestinasiDetailFilm.id_film])
    var detailFilmUiState: DetailFilmUiState by mutableStateOf(DetailFilmUiState.Loading)
        private set

    init {
        getFilmById()
    }

    fun getFilmById() {
        viewModelScope.launch {
            detailFilmUiState = DetailFilmUiState.Loading
            detailFilmUiState = try {
                val response = filmRepository.getFilmById(id_film)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        val film = apiResponse.data
                        if (film != null) {
                            DetailFilmUiState.Success(film)
                        } else {
                            DetailFilmUiState.Error
                        }
                    } else {
                        DetailFilmUiState.Error
                    }
                } else {
                    DetailFilmUiState.Error
                }
            } catch (e: IOException) {
                DetailFilmUiState.Error
            }
        }
    }

    fun refreshFilm() {
        getFilmById()
    }
}