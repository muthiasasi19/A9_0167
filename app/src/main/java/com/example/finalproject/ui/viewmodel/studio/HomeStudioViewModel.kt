package com.example.finalproject.ui.viewmodel.studio


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.model.Studio
import com.example.finalproject.repository.StudioRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeStudioUiState {
    data class Success(val studios: List<Studio>) : HomeStudioUiState()
    object Error : HomeStudioUiState()
    object Loading : HomeStudioUiState()
}

class HomeStudioViewModel(private val studioRepository: StudioRepository) : ViewModel() {

    var homeStudioUiState: HomeStudioUiState by mutableStateOf(HomeStudioUiState.Loading)
        private set

    init {
        getStudios()
    }

    fun getStudios() {
        viewModelScope.launch {
            homeStudioUiState = HomeStudioUiState.Loading
            homeStudioUiState = try {
                // mengambil Response<List<Studio>> dari repository
                val response = studioRepository.getStudios()

                // untuk memeriksa apakah response berhasil dan data tidak null
                if (response.isSuccessful) {
                    val studios = response.body()
                    if (studios != null) {
                        HomeStudioUiState.Success(studios)
                    } else {
                        HomeStudioUiState.Error // untuk meng- jika data null
                    }
                } else {
                    HomeStudioUiState.Error // untuk meng-Handle jika response tidak berhasil
                }
            } catch (e: IOException) {
                HomeStudioUiState.Error
            } catch (e: HttpException) {
                HomeStudioUiState.Error
            }
        }
    }

    fun deleteStudio(id_studio: Int) {
        viewModelScope.launch {
            try {
                studioRepository.deleteStudio(id_studio)
                getStudios() // Refresh data setelah menghapus
            } catch (e: IOException) {
                homeStudioUiState = HomeStudioUiState.Error
            } catch (e: HttpException) {
                homeStudioUiState = HomeStudioUiState.Error
            }
        }
    }
}

