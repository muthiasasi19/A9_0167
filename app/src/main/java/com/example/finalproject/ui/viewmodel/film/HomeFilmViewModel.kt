package com.example.finalproject.ui.viewmodel.film

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Film
import com.example.finalproject.repository.FilmRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeFilmUiState {
    data class Success(val films: List<Film>) : HomeFilmUiState()
    object Error : HomeFilmUiState()
    object Loading : HomeFilmUiState()
}

class HomeFilmViewModel(private val filmRepository: FilmRepository) : ViewModel() {

    var homeFilmUiState: HomeFilmUiState by mutableStateOf(HomeFilmUiState.Loading)
        private set

    init {
        getFilm()
    }

    fun getFilm() {
        viewModelScope.launch {
            homeFilmUiState = HomeFilmUiState.Loading
            homeFilmUiState = try {
                val response = filmRepository.getFilm()
                if (response.isSuccessful) {
                    val films = response.body()
                    if (films != null) {
                        HomeFilmUiState.Success(films)
                    } else {
                        HomeFilmUiState.Error
                    }
                } else {
                    HomeFilmUiState.Error
                }
            } catch (e: IOException) {
                HomeFilmUiState.Error
            } catch (e: HttpException) {
                HomeFilmUiState.Error
            }
        }
    }

    fun deleteFilm(id_film: Int) {
        viewModelScope.launch {
            try {
                filmRepository.deleteFilm(id_film)
                getFilm()
            } catch (e: IOException) {
                homeFilmUiState = HomeFilmUiState.Error
            } catch (e: HttpException) {
                homeFilmUiState = HomeFilmUiState.Error
            }
        }
    }
}