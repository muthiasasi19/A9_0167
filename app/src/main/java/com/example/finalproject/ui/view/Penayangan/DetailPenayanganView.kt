package com.example.finalproject.ui.view.Penayangan

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.model.Penayangan
import com.example.finalproject.ui.navigation.DestinasiEntryTiket
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.Penayangan.DetailPenayanganViewModel

object DestinasiDetailPenayangan : DestinasiNavigasi {
    override val route = "detail_penayangan"
    override val titleRes = "Detail Penayangan"
    const val id_penayangan = "id_penayangan"
    val routeWithArgs = "$route/{$id_penayangan}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPenayanganView(
    navigateBack: () -> Unit,
    onBeliTiketClick: (Int) -> Unit,
    navController: NavController,
    viewModel: DetailPenayanganViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // mengambil id_penayangan dari argument navigasi
    val idPenayangan = navController.currentBackStackEntry?.arguments?.getInt(DestinasiDetailPenayangan.id_penayangan)
    Log.d("DetailPenayanganView", "Received idPenayangan: $idPenayangan")

    // untuk memuat data penayangan berdasarkan id_penayangan
    LaunchedEffect(idPenayangan) {
        if (idPenayangan != null) {
            Log.d("DetailPenayanganView", "Fetching penayangan detail for id: $idPenayangan")
            viewModel.getPenayanganDetail(idPenayangan)
        } else {
            Log.e("DetailPenayanganView", "idPenayangan is null")
        }
    }

    // menampilkan data penayangan
    val penayangan = viewModel.penayangan.collectAsState().value

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = DestinasiDetailPenayangan.titleRes) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    penayangan?.let {
                        navController.navigate("${DestinasiEntryTiket.route}/${it.id_penayangan}?hargaTiket=${it.harga_tiket}")
                    }                },
                containerColor =  Color(0xFF23395D),
                contentColor = Color.White
            ) {
                Text(text = "Beli Tiket")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            // Menambahkan gambar sebagai background
            Image(
                painter = painterResource(id = R.drawable.background1),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.6f),
                contentScale = ContentScale.Crop
            )

            // Menampilkan DetailPenayanganCard di atas gambar
            if (penayangan != null) {
                Log.d("DetailPenayanganView", "Penayangan data loaded: $penayangan")
                DetailPenayanganCard(
                    penayangan = penayangan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                Log.d("DetailPenayanganView", "Loading or error state")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DetailPenayanganStatus(
    penayangan: Penayangan?,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        penayangan != null -> {
            // menampilkan data penayangan jika tersedia
            DetailPenayanganCard(
                penayangan = penayangan,
                modifier = modifier.padding(16.dp)
            )
        }
        else -> {
            // menampilkan loading atau error jika data tidak tersedia
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan atau data tidak ditemukan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = "Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailPenayanganCard(
    penayangan: Penayangan,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor =  Color(0xFF23395D),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComponentDetailPenayangan(judul = "ID Penayangan", isinya = penayangan.id_penayangan.toString())
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPenayangan(judul = "Judul Film", isinya = penayangan.judul_film)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPenayangan(judul = "Studio", isinya = penayangan.nama_studio)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPenayangan(judul = "Tanggal Penayangan", isinya = penayangan.tanggal_penayangan)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPenayangan(judul = "Harga Tiket", isinya = "Rp${penayangan.harga_tiket}")
        }
    }
}

@Composable
fun ComponentDetailPenayangan(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$judul:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = isinya,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}