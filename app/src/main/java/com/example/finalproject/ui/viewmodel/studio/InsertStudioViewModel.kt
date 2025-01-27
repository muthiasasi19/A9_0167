package com.example.finalproject.ui.viewmodel.studio


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finalproject.model.Studio
import com.example.finalproject.repository.StudioRepository

class InsertStudioViewModel(private val studioRepository: StudioRepository) : ViewModel() {

    var uiState by mutableStateOf(InsertStudioUiState())
        private set

    fun updateInsertStudioState(insertStudioUiEvent: InsertStudioUiEvent) {
        uiState = InsertStudioUiState(insertStudioUiEvent = insertStudioUiEvent)
    }

    suspend fun insertStudio(): Boolean {
        return try {
            studioRepository.insertStudio(uiState.insertStudioUiEvent.toStudio())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

fun InsertStudioUiEvent.toStudio(): Studio = Studio(
    id_studio = id_studio.toIntOrNull() ?: 0,
    nama_studio = nama_studio,
    kapasitas = kapasitas.toIntOrNull() ?: 0
)

data class InsertStudioUiState(
    val insertStudioUiEvent: InsertStudioUiEvent = InsertStudioUiEvent()
)

data class InsertStudioUiEvent(
    val id_studio: String = "",
    val nama_studio: String = "",
    val kapasitas: String = ""
)