package com.example.finalproject.ui.view.Tiket


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.Tiket.UpdateTiketViewModel
import kotlinx.coroutines.launch

object DestinasiEditTiket : DestinasiNavigasi {
    override val route = "edit_tiket"
    override val titleRes = "Edit Tiket"
    const val id_tiket = "id_tiket"
    val routeWithArgs = "$route/{$id_tiket}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTiketView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateTiketViewModel = viewModel(factory = PenyediaViewModelFactory.Factory),
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    // Ambil id_tiket dari argumen navigasi
    val idTiket = navController.currentBackStackEntry?.arguments?.getInt("id_tiket") ?: 0

    // Muat data tiket saat pertama kali masuk ke halaman
    LaunchedEffect(Unit) {
        viewModel.loadTiket(idTiket)
    }

    Scaffold(
        modifier = modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEditTiket.titleRes) },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (showError) {
                Text(
                    text = "Gagal menyimpan data. Silakan coba lagi.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            EntryTiketBody(
                updateTiketUiState = viewModel.uiState,
                onTiketValueChange = viewModel::updateUpdateTiketState,
                onSaveClick = {
                    coroutineScope.launch {
                        val isSuccess = viewModel.updateTiket()
                        if (isSuccess) {
                            navigateBack()
                        } else {
                            showError = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryTiketBody(
    updateTiketUiState: UpdateTiketViewModel.UpdateTiketUiState,
    onTiketValueChange: (UpdateTiketViewModel.UpdateTiketUiEvent) -> Unit,
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
            updateTiketUiEvent = updateTiketUiState.updateTiketUiEvent,
            onValueChange = onTiketValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan Perubahan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTiketInput(
    updateTiketUiEvent: UpdateTiketViewModel.UpdateTiketUiEvent,
    onValueChange: (UpdateTiketViewModel.UpdateTiketUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Input untuk ID Penayangan (Read-only)
        OutlinedTextField(
            value = updateTiketUiEvent.id_penayangan,
            onValueChange = { }, // Ga perlu melakukan apa-apa karena read-only
            label = { Text("ID Penayangan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // Non-editable
            singleLine = true
        )

        // Input untuk Jumlah Tiket (Read-only)
        OutlinedTextField(
            value = updateTiketUiEvent.jumlah_tiket,
            onValueChange = { }, // read-only
            label = { Text("Jumlah Tiket") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // Non-editable
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Input untuk Total Harga (Read-only)
        OutlinedTextField(
            value = updateTiketUiEvent.total_harga,
            onValueChange = { }, // Tidak perlu melakukan apa-apa karena read-only
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

        // Input untuk Status Pembayaran (Editable)
        OutlinedTextField(
            value = updateTiketUiEvent.status_pembayaran,
            onValueChange = { onValueChange(updateTiketUiEvent.copy(status_pembayaran = it)) },
            label = { Text("Status Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = updateTiketUiEvent.status_pembayaran.isEmpty()
        )
    }
}