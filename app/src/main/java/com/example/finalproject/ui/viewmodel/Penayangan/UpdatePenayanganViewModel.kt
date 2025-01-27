package com.example.finalproject.ui.viewmodel.Penayangan


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Film
import com.example.finalproject.model.Studio
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.PenayanganRepository
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.ui.view.Penayangan.DestinasiEditPenayangan
import kotlinx.coroutines.launch

class UpdatePenayanganViewModel(
    savedStateHandle: SavedStateHandle,
    private val penayanganRepository: PenayanganRepository,
    private val filmRepository: FilmRepository,
    private val studioRepository: StudioRepository
) : ViewModel() {

    var uiState by mutableStateOf(InsertPenayanganViewModel.InsertPenayanganUiState())
        private set

    var filmList by mutableStateOf<List<Film>>(emptyList())
        private set
    var studioList by mutableStateOf<List<Studio>>(emptyList())
        private set

    val idPenayangan: Int =
        checkNotNull(savedStateHandle[DestinasiEditPenayangan.id_penayangan]).toString()
            .toIntOrNull()
            ?: throw IllegalArgumentException("Invalid id_penayangan")

    init {
        loadPenayangan()
        loadFilmList()
        loadStudioList()
    }

    fun loadPenayangan() {
        viewModelScope.launch {
            try {
                val response = penayanganRepository.getPenayanganById(idPenayangan)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        val penayangan = apiResponse.data
                        uiState = InsertPenayanganViewModel.InsertPenayanganUiState(
                            insertPenayanganUiEvent = InsertPenayanganViewModel.InsertPenayanganUiEvent(
                                id_penayangan = penayangan.id_penayangan.toString(),
                                id_film = penayangan.judul_film,
                                id_studio = penayangan.nama_studio, //.toString(),
                                tanggal_penayangan = penayangan.tanggal_penayangan,
                                harga_tiket = penayangan.harga_tiket.toString()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadFilmList() {
        viewModelScope.launch {
            try {
                val filmResponse = filmRepository.getFilm()
                if (filmResponse.isSuccessful) {
                    filmList = filmResponse.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadStudioList() {
        viewModelScope.launch {
            try {
                val studioResponse = studioRepository.getStudios()
                if (studioResponse.isSuccessful) {
                    studioList = studioResponse.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateInsertPenayanganState(insertPenayanganUiEvent: InsertPenayanganViewModel.InsertPenayanganUiEvent) {
        uiState = InsertPenayanganViewModel.InsertPenayanganUiState(insertPenayanganUiEvent = insertPenayanganUiEvent)
    }

    suspend fun updatePenayangan(): Boolean {
        return try {
            if (uiState.insertPenayanganUiEvent.id_penayangan.isEmpty() ||
                uiState.insertPenayanganUiEvent.id_film.isEmpty() ||
                uiState.insertPenayanganUiEvent.id_studio.isEmpty() ||
                uiState.insertPenayanganUiEvent.tanggal_penayangan.isEmpty() ||
                uiState.insertPenayanganUiEvent.harga_tiket.isEmpty()
            ) {
                throw IllegalArgumentException("Semua field harus diisi")
            }

            val filmResponse = filmRepository.getFilmById(uiState.insertPenayanganUiEvent.id_film.toInt())
            val judulFilm = filmResponse.body()?.data?.judul_film ?: throw IllegalStateException("Judul film tidak ditemukan")

            val studioResponse = studioRepository.getStudioById(uiState.insertPenayanganUiEvent.id_studio.toInt())
            val namaStudio = studioResponse.body()?.data?.nama_studio ?: throw IllegalStateException("Nama studio tidak ditemukan")

            val penayangan = uiState.insertPenayanganUiEvent.toPenayangan(judulFilm, namaStudio)
            penayanganRepository.updatePenayangan(penayangan.id_penayangan, penayangan)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}