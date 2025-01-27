
package com.example.finalproject.ui.view.studio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalproject.model.Studio
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.view.film.OnError
import com.example.finalproject.ui.view.film.OnLoading
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.studio.HomeStudioUiState
import com.example.finalproject.ui.viewmodel.studio.HomeStudioViewModel

object DestinasiHomeStudio : DestinasiNavigasi {
    override val route = "home_studio"
    override val titleRes = "Home Studio"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStudioView(
    navController: NavController,
    navigateToItemEntry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = {},
    viewModel: HomeStudioViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // untuk memuat ulang data saat pertama kali masuk ke halaman
    LaunchedEffect(Unit) {
        viewModel.getStudios()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiHomeStudio.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getStudios() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Studio")
            }
        },
    ) { innerPadding ->
        HomeStudioStatus(
            homeStudioUiState = viewModel.homeStudioUiState,
            retryAction = { viewModel.getStudios() },
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 70.dp),
            onDetailClick = onDetailClick,
            onEditClick = { studio ->
                navController.navigate("${DestinasiEditStudio.route}/${studio.id_studio}")
            },
            onDeleteClick = {
                viewModel.deleteStudio(it.id_studio)
                viewModel.getStudios()
            }
        )
    }
}

@Composable
fun HomeStudioStatus(
    homeStudioUiState: HomeStudioUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (Studio) -> Unit,
    onDeleteClick: (Studio) -> Unit = {},
    onDetailClick: (Int) -> Unit
) {
    when (homeStudioUiState) {
        is HomeStudioUiState.Success -> {
            if (homeStudioUiState.studios.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Studio")
                }
            } else {
                StudioLayout(
                    studios = homeStudioUiState.studios,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.id_studio) },
                    onEditClick = onEditClick,
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeStudioUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeStudioUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun StudioLayout(
    studios: List<Studio>,
    modifier: Modifier = Modifier,
    onDetailClick: (Studio) -> Unit,
    onEditClick: (Studio) -> Unit,
    onDeleteClick: (Studio) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp), // Tambahkan padding atas
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(studios) { studio ->
            StudioCard(
                studio = studio,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(studio) },
                onEditClick = { onEditClick(studio) },
                onDeleteClick = { onDeleteClick(studio) }
            )
        }
    }
}

@Composable
fun StudioCard(
    studio: Studio,
    modifier: Modifier = Modifier,
    onEditClick: (Studio) -> Unit,
    onDeleteClick: (Studio) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = studio.nama_studio,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onEditClick(studio) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
                IconButton(onClick = { onDeleteClick(studio) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                    )
                }
                Text(
                    text = "ID: ${studio.id_studio}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Kapasitas: ${studio.kapasitas} kursi",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
