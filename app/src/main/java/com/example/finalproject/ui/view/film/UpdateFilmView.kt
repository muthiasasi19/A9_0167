package com.example.finalproject.ui.view.film

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.film.UpdateFilmViewModel
import kotlinx.coroutines.launch

object DestinasiEditFilm : DestinasiNavigasi {
    override val route = "film_edit"
    override val titleRes = "Edit Film"
    const val id_film = "id_film"
    val routeWithArgs = "$route/{$id_film}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateFilmView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateFilmViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEditFilm.titleRes) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        EntryFilmBody(
            insertFilmUiState = viewModel.uiState,
            onFilmValueChange = viewModel::updateInsertFilmState,
            onSaveClick = {
                coroutineScope.launch {
                    val isSuccess = viewModel.updateFilm()
                    if (isSuccess) {
                        navigateBack() // Kembali ke halaman detail
                    } else {
                        // Menampilkan pesan error jika update gagal
                        println("Gagal mengupdate film.")
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 70.dp)
        )
    }
}


