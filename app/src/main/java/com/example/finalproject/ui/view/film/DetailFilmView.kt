package com.example.finalproject.ui.view.film


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*


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
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$judul:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}