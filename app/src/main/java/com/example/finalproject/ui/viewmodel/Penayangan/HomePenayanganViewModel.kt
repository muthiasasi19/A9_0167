package com.example.finalproject.ui.viewmodel.Penayangan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Penayangan
import com.example.finalproject.repository.PenayanganRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class HomePenayanganUiState {
    data class Success(val penayanganList: List<Penayangan>) : HomePenayanganUiState()
    object Error : HomePenayanganUiState()
    object Loading : HomePenayanganUiState()
}

class HomePenayanganViewModel(
    private val penayanganRepository: PenayanganRepository
) : ViewModel() {

    private val _penayanganList = MutableStateFlow<List<Penayangan>>(emptyList())
    val penayanganList: StateFlow<List<Penayangan>> = _penayanganList

    init {
        getPenayangan()
    }

    fun getPenayangan() {
        viewModelScope.launch {
            try {
                val penayangan = penayanganRepository.getAllPenayangan()
                _penayanganList.value = penayangan
                Log.d("HomePenayanganViewModel", "Data penayangan yang diambil: $penayangan")
            } catch (e: Exception) {
                Log.e("HomePenayanganViewModel", "Gagal mengambil data penayangan: ${e.message}", e)
            }
        }
    }

    fun deletePenayangan(id_penayangan: Int) {
        viewModelScope.launch {
            try {
                penayanganRepository.deletePenayangan(id_penayangan)
                getPenayangan() // Refresh data setelah menghapus
            } catch (e: Exception) {
                Log.e("HomePenayanganViewModel", "Gagal menghapus data penayangan: ${e.message}", e)
            }
        }
    }
}