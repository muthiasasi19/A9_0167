package com.example.finalproject.ui.view.Penayangan




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
import com.example.finalproject.model.Film
import com.example.finalproject.model.Studio
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.Penayangan.InsertPenayanganViewModel
import com.example.finalproject.ui.viewmodel.Penayangan.UpdatePenayanganViewModel
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import kotlinx.coroutines.launch

object DestinasiEditPenayangan : DestinasiNavigasi {
    override val route = "edit_penayangan"
    override val titleRes = "Edit Penayangan"
    const val id_penayangan = "id_penayangan"
    val routeWithArgs = "$route/{$id_penayangan}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePenayanganView(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePenayanganViewModel = viewModel(factory = PenyediaViewModelFactory.Factory),
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPenayangan()
        viewModel.loadFilmList()
        viewModel.loadStudioList()
    }

    Scaffold(
        modifier = modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiEditPenayangan.titleRes) },
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

            EntryPenayanganBody(
                insertPenayanganUiState = viewModel.uiState,
                onPenayanganValueChange = viewModel::updateInsertPenayanganState,
                onSaveClick = {
                    coroutineScope.launch {
                        val isSuccess = viewModel.updatePenayangan()
                        if (isSuccess) {
                            navigateBack()
                        } else {
                            showError = true
                        }
                    }
                },
                filmList = viewModel.filmList,
                studioList = viewModel.studioList,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}