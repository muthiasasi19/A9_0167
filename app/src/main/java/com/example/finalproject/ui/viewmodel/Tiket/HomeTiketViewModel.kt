package com.example.finalproject.ui.viewmodel.Tiket


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Tiket
import com.example.finalproject.repository.TiketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Sealed class untuk state management
sealed class HomeTiketUiState {
    data class Success(val tiketList: List<Tiket>) : HomeTiketUiState()
    object Error : HomeTiketUiState()
    object Loading : HomeTiketUiState()
}

class HomeTiketViewModel(
    private val tiketRepository: TiketRepository
) : ViewModel() { // Kelas HomeTiketViewModel adalah subclass dari ViewModel

    // State untuk menyimpan daftar tiket
    private val _tiketList = MutableStateFlow<List<Tiket>>(emptyList())
    val tiketList: StateFlow<List<Tiket>> = _tiketList

    // State untuk UI state management
    var uiState by mutableStateOf<HomeTiketUiState>(HomeTiketUiState.Loading)
        private set

    init {
        getTiket()
    }

    // Fungsi untuk mengambil data tiket dari repository
    fun getTiket() {
        viewModelScope.launch {
            uiState = HomeTiketUiState.Loading
            try {
                // Ambil data tiket langsung dari repository
                val tiketList = tiketRepository.getAllTiket()
                _tiketList.value = tiketList
                uiState = HomeTiketUiState.Success(tiketList)
                Log.d("HomeTiketViewModel", "Data tiket yang diambil: $tiketList")
            } catch (e: IOException) {
                Log.e("HomeTiketViewModel", "Gagal mengambil data tiket: ${e.message}", e)
                uiState = HomeTiketUiState.Error
            } catch (e: HttpException) {
                Log.e("HomeTiketViewModel", "Gagal mengambil data tiket: ${e.message}", e)
                uiState = HomeTiketUiState.Error
            } catch (e: Exception) {
                Log.e("HomeTiketViewModel", "Gagal mengambil data tiket: ${e.message}", e)
                uiState = HomeTiketUiState.Error
            }
        }
    }

    // Fungsi untuk menghapus tiket berdasarkan ID
    fun deleteTiket(id_tiket: Int) {
        viewModelScope.launch {
            try {
                tiketRepository.deleteTiket(id_tiket)
                getTiket() // Refresh data setelah menghapus
            } catch (e: Exception) {
                Log.e("HomeTiketViewModel", "Gagal menghapus data tiket: ${e.message}", e)
            }
        }
    }
}