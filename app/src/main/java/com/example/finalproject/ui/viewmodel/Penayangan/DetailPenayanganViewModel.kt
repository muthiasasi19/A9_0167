package com.example.finalproject.ui.viewmodel.Penayangan


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Film
import com.example.finalproject.model.Penayangan
import com.example.finalproject.model.Studio
import com.example.finalproject.repository.PenayanganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailPenayanganUiState {
    data class Success(
        val penayangan: Penayangan,
        val film: Film,
        val studio: Studio
    ) : DetailPenayanganUiState()

    object Loading : DetailPenayanganUiState()
    object Error : DetailPenayanganUiState()
}

class DetailPenayanganViewModel(
    savedStateHandle: SavedStateHandle,
    private val penayanganRepository: PenayanganRepository
) : ViewModel() {

    private val _penayangan = MutableStateFlow<Penayangan?>(null)
    val penayangan: StateFlow<Penayangan?> = _penayangan

    init {
        val idPenayangan = savedStateHandle.get<Int>("id_penayangan") ?: 0
        getPenayanganDetail(idPenayangan)
    }

    fun getPenayanganDetail(idPenayangan: Int) {
        viewModelScope.launch {
            try {
                val response = penayanganRepository.getPenayanganById(idPenayangan)
                if (response.isSuccessful) {
                    _penayangan.value = response.body()?.data
                }
            } catch (e: Exception) {
                // untuk meng-Handle error
            }
        }
    }

    fun refreshPenayangan(idPenayangan: Int) {
        getPenayanganDetail(idPenayangan)
    }
}