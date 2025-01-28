package com.example.finalproject.ui.view.film


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.model.Film
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.film.DetailFilmUiState
import com.example.finalproject.ui.viewmodel.film.DetailFilmViewModel

object DestinasiDetailFilm : DestinasiNavigasi {
    override val route = "detail_film"
    override val titleRes = "Detail Film"
    const val id_film = "id_film"
    val routeWithArgs = "$route/{$id_film}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFilmView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    detailFilmViewModel: DetailFilmViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Menggunakan LaunchedEffect supaya bisa refresh setelah kembali dari halaman edit
    LaunchedEffect(Unit) {
        detailFilmViewModel.refreshFilm()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = DestinasiDetailFilm.titleRes) },
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
                    val id_film = (detailFilmViewModel.detailFilmUiState as? DetailFilmUiState.Success)?.film?.id_film
                    if (id_film != null) onEditClick(id_film)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Film",
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(top = 70.dp)
        ) {
            DetailFilmStatus(
                filmUiState = detailFilmViewModel.detailFilmUiState,
                retryAction = { detailFilmViewModel.getFilmById() },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailFilmStatus(
    filmUiState: DetailFilmUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (filmUiState) {
        is DetailFilmUiState.Success -> {
            DetailFilmCard(
                film = filmUiState.film,
                modifier = modifier.padding(16.dp)
            )
        }

        is DetailFilmUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailFilmUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.")
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
fun DetailFilmCard(
    film: Film,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComponentDetailFilm(judul = "ID Film", isinya = film.id_film.toString())
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailFilm(judul = "Judul Film", isinya = film.judul_film)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailFilm(judul = "Durasi", isinya = "${film.durasi} menit")
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailFilm(judul = "Deskripsi", isinya = film.deskripsi)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailFilm(judul = "Genre", isinya = film.genre)
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailFilm(judul = "Rating Usia", isinya = film.rating_usia)
        }
    }
}

@Composable
fun ComponentDetailFilm(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, // Konten di tengah horizontal
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = judul,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Text(
            text = isinya,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}