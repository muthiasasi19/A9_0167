package com.example.finalproject.ui.view.studio


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
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.studio.InsertStudioUiEvent
import com.example.finalproject.ui.viewmodel.studio.InsertStudioUiState
import com.example.finalproject.ui.viewmodel.studio.InsertStudioViewModel
import kotlinx.coroutines.launch

object DestinasiEntryStudio : DestinasiNavigasi {
    override val route = "entry_studio"
    override val titleRes = "Entry Studio"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertStudioView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertStudioViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
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
                title = { Text(DestinasiEntryStudio.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 70.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            EntryStudioBody(
                insertStudioUiState = viewModel.uiState,
                onStudioValueChange = viewModel::updateInsertStudioState,
                onSaveClick = {
                    coroutineScope.launch {
                        val isSuccess = viewModel.insertStudio()
                        if (isSuccess) {
                            showSuccessMessage = true
                            navigateBack() // Kembali ke halaman home setelah berhasil
                        } else {
                            showErrorMessage = true
                        }
                    }
                }
            )

            // menampilkan pesan sukses
            if (showSuccessMessage) {
                Text(
                    text = "Data berhasil disimpan!",
                    color = Color.Green,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Tampilkan pesan error
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
fun EntryStudioBody(
    insertStudioUiState: InsertStudioUiState,
    onStudioValueChange: (InsertStudioUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormStudioInput(
            insertStudioUiEvent = insertStudioUiState.insertStudioUiEvent,
            onValueChange = onStudioValueChange,
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

@Composable
fun FormStudioInput(
    insertStudioUiEvent: InsertStudioUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertStudioUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = insertStudioUiEvent.id_studio,
            onValueChange = { onValueChange(insertStudioUiEvent.copy(id_studio = it)) },
            label = { Text("ID Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertStudioUiEvent.nama_studio,
            onValueChange = { onValueChange(insertStudioUiEvent.copy(nama_studio = it)) },
            label = { Text("Nama Studio") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertStudioUiEvent.kapasitas,
            onValueChange = { onValueChange(insertStudioUiEvent.copy(kapasitas = it)) },
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
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