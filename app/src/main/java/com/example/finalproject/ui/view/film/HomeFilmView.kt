package com.example.finalproject.ui.view.film



import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.R
import com.example.finalproject.model.Film
import com.example.finalproject.ui.navigation.DestinasiNavigasi
import com.example.finalproject.ui.viewmodel.PenyediaViewModelFactory
import com.example.finalproject.ui.viewmodel.film.HomeFilmUiState
import com.example.finalproject.ui.viewmodel.film.HomeFilmViewModel

object DestinasiHomeFilm : DestinasiNavigasi {
    override val route = "home_film"
    override val titleRes = "Home Film"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFilmView(
    navigateToItemEntry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit = {},
    viewModel: HomeFilmViewModel = viewModel(factory = PenyediaViewModelFactory.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // State untuk mengontrol dialog konfirmasi
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var filmToDelete by remember { mutableStateOf<Film?>(null) }

    // Muat ulang data saat pertama kali masuk ke halaman
    LaunchedEffect(Unit) {
        viewModel.getFilm()
    }

    // Tampilkan dialog konfirmasi jika diperlukan
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                filmToDelete = null
            },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus film ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        filmToDelete?.let { film ->
                            viewModel.deleteFilm(film.id_film)
                            viewModel.getFilm()
                        }
                        showDeleteConfirmation = false
                        filmToDelete = null
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        filmToDelete = null
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
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

        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(DestinasiHomeFilm.titleRes) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.getFilm() }) {
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
                    modifier = Modifier.padding(18.dp),
                    containerColor = Color(0xFF23395D)  ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Film",
                        tint = Color.White
                    )
                }
            },
        ) { innerPadding ->
            HomeFilmStatus(
                homeFilmUiState = viewModel.homeFilmUiState,
                retryAction = { viewModel.getFilm() },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                onDetailClick = onDetailClick,
                onDeleteClick = { film ->
                    filmToDelete = film
                    showDeleteConfirmation = true
                }
            )
        }
    }
}

@Composable
fun HomeFilmStatus(
    homeFilmUiState: HomeFilmUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Film) -> Unit = {},
    onDetailClick: (Int) -> Unit
) {
    when (homeFilmUiState) {
        is HomeFilmUiState.Success -> {
            if (homeFilmUiState.films.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data Film")
                }
            } else {
                FilmLayout(
                    films = homeFilmUiState.films,
                    modifier = modifier.fillMaxSize(),
                    onDetailClick = { onDetailClick(it.id_film) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeFilmUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeFilmUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(20.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = ""
    )
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = "",
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun FilmLayout(
    films: List<Film>,
    modifier: Modifier = Modifier,
    onDetailClick: (Film) -> Unit,
    onDeleteClick: (Film) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(films) { film ->
            FilmCard(
                film = film,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(film) },
                onDeleteClick = { onDeleteClick(film) }
            )
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    modifier: Modifier = Modifier,
    onDeleteClick: (Film) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF23395D))
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
                    text = film.judul_film,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteClick(film) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    text = "ID: ${film.id_film}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Text(
                text = "Durasi: ${film.durasi} menit",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Genre: ${film.genre}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}