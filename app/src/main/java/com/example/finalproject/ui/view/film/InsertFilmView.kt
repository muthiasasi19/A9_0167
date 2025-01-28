package com.example.finalproject.ui.view.film


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.film.InsertFilmUiEvent
import com.example.finalproject.ui.viewmodel.film.InsertFilmUiState
import com.example.finalproject.ui.viewmodel.film.InsertFilmViewModel
import kotlinx.coroutines.launch

object DestinasiEntryFilm : DestinasiNavigasi {
    override val route = "entry_film"
    override val titleRes = "Entry Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertFilmView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertFilmViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEntryFilm.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            EntryFilmBody(
                insertFilmUiState = viewModel.uiState,
                onFilmValueChange = viewModel::updateInsertFilmState,
                onSaveClick = {
                    coroutineScope.launch {
                        val isSuccess = viewModel.insertFilm()
                        if (isSuccess) {
                            showSuccessMessage = true
                            navigateBack() // Stelah berhasil maka akan kembali ke halaman home
                        } else {
                            showErrorMessage = true
                        }
                    }
                }
            )

            // Untuk menampilkan pesan sukses
            if (showSuccessMessage) {
                Text(
                    text = "Data berhasil disimpan!",
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Untuk menampilkan pesan error
            if (showErrorMessage) {
                Text(
                    text = "Gagal menyimpan data. Silakan coba lagi.",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun EntryFilmBody(
    insertFilmUiState: InsertFilmUiState,
    onFilmValueChange: (InsertFilmUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormFilmInput(
            insertFilmUiEvent = insertFilmUiState.insertFilmUiEvent,
            onValueChange = onFilmValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF23395D)
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@Composable
fun FormFilmInput(
    insertFilmUiEvent: InsertFilmUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertFilmUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        OutlinedTextField(
            value = insertFilmUiEvent.id_film,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(id_film = it)) },
            label = { Text("ID Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        OutlinedTextField(
            value = insertFilmUiEvent.judul_film,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(judul_film = it)) },
            label = { Text("Judul Film") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertFilmUiEvent.durasi,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(durasi = it)) },
            label = { Text("Durasi (menit)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = insertFilmUiEvent.deskripsi,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(deskripsi = it)) },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertFilmUiEvent.genre,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(genre = it)) },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertFilmUiEvent.rating_usia,
            onValueChange = { onValueChange(insertFilmUiEvent.copy(rating_usia = it)) },
            label = { Text("Rating Usia") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(8.dp)
            )
        }
        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(5.dp)
        )
    }
}