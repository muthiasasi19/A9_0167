package com.example.finalproject.ui.viewmodel


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.finalproject.BioskopContainer
import com.example.finalproject.ui.navigation.PengelolaHalaman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioskopApp(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Ambil instance dari BioskopContainer
    val filmRepository = BioskopContainer.filmRepository
    val penayanganRepository = BioskopContainer.penayanganRepository
    val studioRepository = BioskopContainer.studioRepository
    val tiketRepository = BioskopContainer.tiketRepository

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PengelolaHalaman(
                filmRepository = filmRepository,
                penayanganRepository = penayanganRepository,
                studioRepository = studioRepository
            )
        }
    }
}