package com.example.finalproject.ui.view.Tiket


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
import androidx.navigation.NavController
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.Tiket.InsertTiketViewModel
import kotlinx.coroutines.launch

object DestinasiEntryTiket : DestinasiNavigasi {
    override val route = "entry_tiket"
    override val titleRes = "Entry Tiket"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTiketView(
    navigateBack: () -> Unit,
    idPenayangan: Int,
    hargaTiket: Double, // Terima harga tiket dari argumen navigasi
    modifier: Modifier = Modifier,
    viewModel: InsertTiketViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Set id_penayangan dan harga tiket di ViewModel saat pertama kali masuk ke halaman
    LaunchedEffect(idPenayangan, hargaTiket) {
        viewModel.updateInsertTiketState(
            viewModel.uiState.insertTiketUiEvent.copy(
                id_penayangan = idPenayangan.toString()
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEntryTiket.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        EntryTiketBody(
            insertTiketUiState = viewModel.uiState,
            onTiketValueChange = { event ->
                // Hitung total harga otomatis saat jumlah tiket diubah
                if (event.jumlah_tiket != viewModel.uiState.insertTiketUiEvent.jumlah_tiket) {
                    viewModel.calculateTotalHarga(event.jumlah_tiket, hargaTiket)
                } else {
                    viewModel.updateInsertTiketState(event)
                }
            },
            onSaveClick = {
                coroutineScope.launch {
                    val isSuccess = viewModel.insertTiket()
                    if (isSuccess) {
                        snackbarHostState.showSnackbar("Data berhasil disimpan")
                        navigateBack()
                    } else {
                        snackbarHostState.showSnackbar("Gagal menyimpan data")
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
fun EntryTiketBody(
    insertTiketUiState: InsertTiketViewModel.InsertTiketUiState,
    onTiketValueChange: (InsertTiketViewModel.InsertTiketUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormTiketInput(
            insertTiketUiEvent = insertTiketUiState.insertTiketUiEvent,
            onValueChange = onTiketValueChange,
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
fun FormTiketInput(
    insertTiketUiEvent: InsertTiketViewModel.InsertTiketUiEvent,
    onValueChange: (InsertTiketViewModel.InsertTiketUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input untuk Jumlah Tiket
        OutlinedTextField(
            value = insertTiketUiEvent.jumlah_tiket,
            onValueChange = { onValueChange(insertTiketUiEvent.copy(jumlah_tiket = it)) },
            label = { Text("Jumlah Tiket") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = insertTiketUiEvent.jumlah_tiket.isEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, // Keyboard angka
                imeAction = ImeAction.Done
            )
        )

        // Input untuk Total Harga (Read-only)
        OutlinedTextField(
            value = insertTiketUiEvent.total_harga,
            onValueChange = { }, // Tidak bisa diubah
            label = { Text("Total Harga") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // Non-editable
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Input untuk Status Pembayaran
        OutlinedTextField(
            value = insertTiketUiEvent.status_pembayaran,
            onValueChange = { onValueChange(insertTiketUiEvent.copy(status_pembayaran = it)) },
            label = { Text("Status Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = insertTiketUiEvent.status_pembayaran.isEmpty()
        )
    }
}