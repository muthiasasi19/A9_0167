package com.example.finalproject.ui.view.studio


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.studio.UpdateStudioViewModel
import kotlinx.coroutines.launch

object DestinasiEditStudio : DestinasiNavigasi {
    override val route = "edit_studio"
    override val titleRes = "Edit Studio"
    const val id_studio = "id_studio"
    val routeWithArgs = "$route/{$id_studio}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStudioView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateStudioViewModel = viewModel(factory = PenyediaViewModelFactory.Factory),
    navController: NavController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) } // State untuk menampilkan pesan error

    // memuat ulang data studio saat pertama kali masuk ke halaman
    LaunchedEffect(Unit) {
        viewModel.loadStudio()
    }

    Scaffold(
        modifier = modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEditStudio.titleRes) },
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
                .padding(top = 70.dp)
        ) {
            // menampilkan pesan error jika penyimpanan gagal
            if (showError) {
                Text(
                    text = "Gagal menyimpan data. Silakan coba lagi.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            EntryStudioBody(
                insertStudioUiState = viewModel.uiState,
                onStudioValueChange = viewModel::updateInsertStudioState,
                onSaveClick = {
                    coroutineScope.launch {
                        val isSuccess = viewModel.updateStudio()
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