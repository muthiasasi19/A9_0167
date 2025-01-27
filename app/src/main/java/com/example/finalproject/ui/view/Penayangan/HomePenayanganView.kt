package com.example.finalproject.ui.view.Penayangan

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalproject.model.Penayangan
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.Penayangan.HomePenayanganViewModel
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory

object DestinasiHomePenayangan : DestinasiNavigasi {
    override val route = "home_penayangan"
    override val titleRes = "Home Penayangan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePenayanganView(
    navController: NavController,
    navigateToItemEntry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = { idPenayangan ->
        Log.d("HomePenayanganView", "Card clicked, navigating to detail with id: $idPenayangan")
        navController.navigate("${DestinasiDetailPenayangan.route}/$idPenayangan")
    },
    viewModel: HomePenayanganViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // State untuk mengontrol visibilitas dialog konfirmasi
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var penayanganToDelete by remember { mutableStateOf<Penayangan?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getPenayangan()
    }

    val penayanganList = viewModel.penayanganList.collectAsState().value

    // AlertDialog untuk konfirmasi penghapusan
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                penayanganToDelete = null
            },
            title = { Text("Hapus Penayangan") },
            text = { Text("Apakah Anda yakin ingin menghapus penayangan ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        penayanganToDelete?.let {
                            viewModel.deletePenayangan(it.id_penayangan)
                        }
                        showDeleteConfirmation = false
                        penayanganToDelete = null
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        penayanganToDelete = null
                    }
                ) {
                    Text("Tidak")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiHomePenayangan.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getPenayangan() }) {
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Penayangan")
            }
        },
    ) { innerPadding ->
        HomePenayanganStatus(
            penayanganList = penayanganList,
            retryAction = { viewModel.getPenayangan() },
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 16.dp),
            onDetailClick = onDetailClick,
            onEditClick = { penayangan ->
                navController.navigate("${DestinasiEditPenayangan.route}/${penayangan.id_penayangan}")
            },
            onDeleteClick = { penayangan ->
                penayanganToDelete = penayangan
                showDeleteConfirmation = true
            }
        )
    }
}

@Composable
fun HomePenayanganStatus(
    penayanganList: List<Penayangan>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (Penayangan) -> Unit,
    onDeleteClick: (Penayangan) -> Unit = {},
    onDetailClick: (Int) -> Unit
) {
    if (penayanganList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Tidak ada data Penayangan")
        }
    } else {
        PenayanganLayout(
            penayanganList = penayanganList,
            modifier = modifier.fillMaxWidth(),
            onDetailClick = { onDetailClick(it.id_penayangan) },
            onEditClick = onEditClick,
            onDeleteClick = { onDeleteClick(it) }
        )
    }
}

@Composable
fun PenayanganLayout(
    penayanganList: List<Penayangan>,
    modifier: Modifier = Modifier,
    onDetailClick: (Penayangan) -> Unit,
    onEditClick: (Penayangan) -> Unit,
    onDeleteClick: (Penayangan) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 80.dp,
            start = 20.dp,
            end = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(penayanganList) { penayangan ->
            PenayanganCard(
                penayangan = penayangan,
                modifier = Modifier.fillMaxWidth(),
                onEditClick = { onEditClick(penayangan) },
                onDeleteClick = { onDeleteClick(penayangan) },
                onDetailClick = { onDetailClick(penayangan) }
            )
        }
    }
}

@Composable
fun PenayanganCard(
    penayangan: Penayangan,
    modifier: Modifier = Modifier,
    onEditClick: (Penayangan) -> Unit,
    onDeleteClick: (Penayangan) -> Unit = {},
    onDetailClick: (Penayangan) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onDetailClick(penayangan) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Film: ${penayangan.judul_film}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Studio: ${penayangan.nama_studio}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Tanggal: ${penayangan.tanggal_penayangan}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Harga: Rp${penayangan.harga_tiket}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEditClick(penayangan) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDeleteClick(penayangan) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}