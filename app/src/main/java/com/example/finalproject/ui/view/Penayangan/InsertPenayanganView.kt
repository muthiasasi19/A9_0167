package com.example.finalproject.ui.view.Penayangan


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.model.Film
import com.example.finalproject.model.Studio
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.Penayangan.InsertPenayanganViewModel
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import kotlinx.coroutines.launch

object DestinasiEntryPenayangan : DestinasiNavigasi {
    override val route = "entry_penayangan"
    override val titleRes = "Entry Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPenayanganView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPenayanganViewModel = viewModel(factory = PenyediaViewModelFactory.Factory),
    filmRepository: FilmRepository,       // Repository untuk film
    studioRepository: StudioRepository    // Repository untuk studio
) {
    val coroutineScope = rememberCoroutineScope()
    var filmList by remember { mutableStateOf<List<Film>>(emptyList()) }
    var studioList by remember { mutableStateOf<List<Studio>>(emptyList()) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Ambil daftar film dan studio saat pertama kali masuk ke halaman
    LaunchedEffect(Unit) {
        val filmResponse = filmRepository.getFilm() // Ambil daftar film
        if (filmResponse.isSuccessful) {
            filmList = filmResponse.body() ?: emptyList() // Ekstrak data dari Response
        }

        val studioResponse = studioRepository.getStudios() // Ambil daftar studio
        if (studioResponse.isSuccessful) {
            studioList = studioResponse.body() ?: emptyList() // Ekstrak data dari Response
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEntryPenayangan.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        EntryPenayanganBody(
            insertPenayanganUiState = viewModel.uiState,
            onPenayanganValueChange = viewModel::updateInsertPenayanganState,
            onSaveClick = {
                coroutineScope.launch {
                    val isSuccess = viewModel.insertPenayangan()
                    if (isSuccess) {
                        snackbarHostState.showSnackbar("Data berhasil disimpan")
                        navigateBack()
                    } else {
                        snackbarHostState.showSnackbar("Gagal menyimpan data")
                    }
                }
            },
            filmList = filmList,
            studioList = studioList,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun EntryPenayanganBody(
    insertPenayanganUiState: InsertPenayanganViewModel.InsertPenayanganUiState,
    onPenayanganValueChange: (InsertPenayanganViewModel.InsertPenayanganUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    filmList: List<Film>,       // Daftar film
    studioList: List<Studio>,   // Daftar studio
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormPenayanganInput(
            insertPenayanganUiEvent = insertPenayanganUiState.insertPenayanganUiEvent,
            onValueChange = onPenayanganValueChange,
            filmList = filmList,
            studioList = studioList,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPenayanganInput(
    insertPenayanganUiEvent: InsertPenayanganViewModel.InsertPenayanganUiEvent,
    onValueChange: (InsertPenayanganViewModel.InsertPenayanganUiEvent) -> Unit,
    filmList: List<Film>,
    studioList: List<Studio>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expandedFilm by remember { mutableStateOf(false) }
    var expandedStudio by remember { mutableStateOf(false) }
    var chosenFilm by remember { mutableStateOf("") }
    var chosenStudio by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = insertPenayanganUiEvent.id_penayangan,
            onValueChange = {
                onValueChange(insertPenayanganUiEvent.copy(id_penayangan = it))
            },
            label = { Text("ID Penayangan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = insertPenayanganUiEvent.id_penayangan.isEmpty() // Validasi kalau data kosong
        )

        // Dropdown untuk memilih judul_Film
        ExposedDropdownMenuBox(
            expanded = expandedFilm,
            onExpandedChange = { expandedFilm = !expandedFilm }
        ) {
            OutlinedTextField(
                value = chosenFilm,
                onValueChange = { },
                label = { Text("Pilih Film") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFilm)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                isError = insertPenayanganUiEvent.id_film.isEmpty()
            )

            ExposedDropdownMenu(
                expanded = expandedFilm,
                onDismissRequest = { expandedFilm = false }
            ) {
                filmList.forEach { film ->
                    DropdownMenuItem(
                        onClick = {
                            chosenFilm = film.judul_film
                            expandedFilm = false
                            onValueChange(insertPenayanganUiEvent.copy(id_film = film.id_film.toString()))
                        },
                        text = { Text(text = film.judul_film) }
                    )
                }
            }
        }

// Dropdown untuk memilih nama_Studio
        ExposedDropdownMenuBox(
            expanded = expandedStudio,
            onExpandedChange = { expandedStudio = !expandedStudio }
        ) {
            OutlinedTextField(
                value = chosenStudio,
                onValueChange = { },
                label = { Text("Pilih Studio") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStudio)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                isError = insertPenayanganUiEvent.id_studio.isEmpty()
            )

            ExposedDropdownMenu(
                expanded = expandedStudio,
                onDismissRequest = { expandedStudio = false }
            ) {
                studioList.forEach { studio ->
                    DropdownMenuItem(
                        onClick = {
                            chosenStudio = studio.nama_studio
                            expandedStudio = false
                            onValueChange(insertPenayanganUiEvent.copy(id_studio = studio.id_studio.toString()))
                        },
                        text = { Text(text = studio.nama_studio) }
                    )
                }
            }
        }


        OutlinedTextField(
            value = insertPenayanganUiEvent.tanggal_penayangan,
            onValueChange = { onValueChange(insertPenayanganUiEvent.copy(tanggal_penayangan = it)) },
            label = { Text("Tanggal Penayangan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertPenayanganUiEvent.harga_tiket,
            onValueChange = { onValueChange(insertPenayanganUiEvent.copy(harga_tiket = it)) },
            label = { Text("Harga Tiket") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}