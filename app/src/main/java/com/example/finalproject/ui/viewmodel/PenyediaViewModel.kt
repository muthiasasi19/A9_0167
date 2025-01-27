package com.example.finalproject.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.finalproject.BioskopApplication
import com.example.finalproject.BioskopContainer
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.repository.PenayanganRepository
import com.example.finalproject.repository.TiketRepository
import com.example.finalproject.ui.viewmodel.Penayangan.DetailPenayanganViewModel
import com.example.finalproject.ui.viewmodel.Penayangan.HomePenayanganViewModel
import com.example.finalproject.ui.viewmodel.Penayangan.InsertPenayanganViewModel
import com.example.finalproject.ui.viewmodel.Penayangan.UpdatePenayanganViewModel
import com.example.finalproject.ui.viewmodel.Tiket.HomeTiketViewModel
import com.example.finalproject.ui.viewmodel.Tiket.InsertTiketViewModel
import com.example.finalproject.ui.viewmodel.Tiket.UpdateTiketViewModel
import com.example.finalproject.ui.viewmodel.film.DetailFilmViewModel
import com.example.finalproject.ui.viewmodel.film.HomeFilmViewModel
import com.example.finalproject.ui.viewmodel.film.InsertFilmViewModel
import com.example.finalproject.ui.viewmodel.film.UpdateFilmViewModel
import com.example.finalproject.ui.viewmodel.studio.HomeStudioViewModel
import com.example.finalproject.ui.viewmodel.studio.InsertStudioViewModel
import com.example.finalproject.ui.viewmodel.studio.UpdateStudioViewModel


class PenyediaViewModelFactory(
    private val filmRepository: FilmRepository,
    private val studioRepository: StudioRepository,
    private val penayanganRepository: PenayanganRepository,
    private val tiketRepository: TiketRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeFilmViewModel::class.java) -> {
                HomeFilmViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(DetailFilmViewModel::class.java) -> {
                DetailFilmViewModel(savedStateHandle, filmRepository) as T
            }
            modelClass.isAssignableFrom(InsertFilmViewModel::class.java) -> {
                InsertFilmViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(UpdateFilmViewModel::class.java) -> {
                UpdateFilmViewModel(savedStateHandle, filmRepository) as T
            }
            modelClass.isAssignableFrom(HomeStudioViewModel::class.java) -> {
                HomeStudioViewModel(studioRepository) as T
            }
            modelClass.isAssignableFrom(InsertStudioViewModel::class.java) -> {
                InsertStudioViewModel(studioRepository) as T
            }
            modelClass.isAssignableFrom(UpdateStudioViewModel::class.java) -> {
                UpdateStudioViewModel(savedStateHandle, studioRepository) as T
            }
            modelClass.isAssignableFrom(HomePenayanganViewModel::class.java) -> {
                HomePenayanganViewModel(penayanganRepository) as T
            }
            modelClass.isAssignableFrom(InsertPenayanganViewModel::class.java) -> {
                InsertPenayanganViewModel(penayanganRepository, filmRepository, studioRepository) as T
            }
            modelClass.isAssignableFrom(UpdatePenayanganViewModel::class.java) -> {
                UpdatePenayanganViewModel(
                    savedStateHandle,
                    penayanganRepository,
                    filmRepository,
                    studioRepository
                ) as T
            }
            modelClass.isAssignableFrom(DetailPenayanganViewModel::class.java) -> {
                DetailPenayanganViewModel(savedStateHandle, penayanganRepository) as T
            }
            modelClass.isAssignableFrom(HomeTiketViewModel::class.java) -> {
                HomeTiketViewModel(tiketRepository) as T
            }
            modelClass.isAssignableFrom(UpdateTiketViewModel::class.java) -> {
                UpdateTiketViewModel(tiketRepository) as T
            }

            modelClass.isAssignableFrom(InsertTiketViewModel::class.java) -> {
                InsertTiketViewModel(tiketRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val filmRepository = BioskopContainer.filmRepository
                val studioRepository = BioskopContainer.studioRepository
                val penayanganRepository = BioskopContainer.penayanganRepository
                val tiketRepository = BioskopContainer.tiketRepository
                val savedStateHandle = extras.createSavedStateHandle()
                return PenyediaViewModelFactory(
                    filmRepository,
                    studioRepository,
                    penayanganRepository,
                    tiketRepository,
                    savedStateHandle
                ).create(modelClass)
            }
        }
    }
}


fun CreationExtras.bioskopApp(): BioskopApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BioskopApplication)