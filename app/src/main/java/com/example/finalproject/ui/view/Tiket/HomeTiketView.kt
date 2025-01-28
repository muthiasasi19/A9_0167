package com.example.finalproject.ui.view.Tiket


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalproject.model.Tiket
import com.example.finalproject.ui.navigation.DestinasiEditTiket
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.Tiket.HomeTiketViewModel

object DestinasiHomeTiket : DestinasiNavigasi {
    override val route = "home_tiket"
    override val titleRes = "Home Tiket"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTiketView(
    navController: NavController,
    navigateToItemEntry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeTiketViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // State untuk mengontrol visibilitas dialog konfirmasi
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var tiketToDelete by remember { mutableStateOf<Tiket?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getTiket()
    }

    val tiketList = viewModel.tiketList.collectAsState().value

    //  untuk konfirmasi penghapusan
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                tiketToDelete = null
            },
            title = { Text("Hapus Tiket") },
            text = { Text("Apakah Anda yakin ingin menghapus tiket ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        tiketToDelete?.let {
                            viewModel.deleteTiket(it.id_tiket)
                        }
                        showDeleteConfirmation = false
                        tiketToDelete = null
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        tiketToDelete = null
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
                title = { Text(DestinasiHomeTiket.titleRes) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getTiket() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        HomeTiketStatus(
            tiketList = tiketList,
            retryAction = { viewModel.getTiket() },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onEditClick = { tiket ->
                navController.navigate("${DestinasiEditTiket.route}/${tiket.id_tiket}")
            },
            onDeleteClick = { tiket ->
                tiketToDelete = tiket
                showDeleteConfirmation = true
            }
        )
    }
}

@Composable
fun HomeTiketStatus(
    tiketList: List<Tiket>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (Tiket) -> Unit,
    onDeleteClick: (Tiket) -> Unit = {}
) {
    if (tiketList.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Tidak ada data Tiket")
        }
    } else {
        TiketLayout(
            tiketList = tiketList,
            modifier = modifier.fillMaxSize(),
            onEditClick = onEditClick,
            onDeleteClick = { onDeleteClick(it) }
        )
    }
}


@Composable
fun TiketLayout(
    tiketList: List<Tiket>,
    modifier: Modifier = Modifier,
    onEditClick: (Tiket) -> Unit,
    onDeleteClick: (Tiket) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
        .fillMaxSize(),
    contentPadding = PaddingValues(
        top = 16.dp,
        bottom = 80.dp,
        start = 16.dp,
        end = 16.dp
    ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tiketList) { tiket ->
            TiketCard(
                tiket = tiket,
                modifier = Modifier.fillMaxWidth(),
                onEditClick = { onEditClick(tiket) },
                onDeleteClick = { onDeleteClick(tiket) }
            )
        }
    }
}


@Composable
fun TiketCard(
    tiket: Tiket,
    modifier: Modifier = Modifier,
    onEditClick: (Tiket) -> Unit,
    onDeleteClick: (Tiket) -> Unit = {}
) {
    val cardColor = Color(0xFF23395D)
    val iconColor = Color.White

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "ID Tiket: ${tiket.id_tiket}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Text(
                text = "Jumlah Tiket: ${tiket.jumlah_tiket}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Text(
                text = "Total Harga: Rp${tiket.total_harga}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Text(
                text = "Status Pembayaran: ${tiket.status_pembayaran}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEditClick(tiket) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = iconColor)
                }
                IconButton(onClick = { onDeleteClick(tiket) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = iconColor)
                }
            }
        }
    }
}