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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


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
                    modifier = modifier.fillMaxWidth(),
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
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp, start = 20.dp, end = 20.dp), // Tambahkan padding atas
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
                    text = film.judul_film,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteClick(film) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
                Text(
                    text = "ID: ${film.id_film}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Durasi: ${film.durasi} menit",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Genre: ${film.genre}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}