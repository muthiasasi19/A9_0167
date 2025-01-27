package com.example.finalproject.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalproject.repository.FilmRepository
import com.example.finalproject.repository.PenayanganRepository
import com.example.finalproject.repository.StudioRepository
import com.example.finalproject.ui.view.HomeScreen
import com.example.finalproject.ui.view.Penayangan.DetailPenayanganView
import com.example.finalproject.ui.view.Penayangan.HomePenayanganView
import com.example.finalproject.ui.view.Penayangan.InsertPenayanganView
import com.example.finalproject.ui.view.Penayangan.UpdatePenayanganView
import com.example.finalproject.ui.view.Tiket.HomeTiketView
import com.example.finalproject.ui.view.Tiket.InsertTiketView
import com.example.finalproject.ui.view.Tiket.UpdateTiketView
import com.example.finalproject.ui.view.film.DestinasiDetailFilm
import com.example.finalproject.ui.view.film.DestinasiEditFilm
import com.example.finalproject.ui.view.film.DestinasiEntryFilm
import com.example.finalproject.ui.view.film.DestinasiHomeFilm
import com.example.finalproject.ui.view.film.DetailFilmView
import com.example.finalproject.ui.view.film.HomeFilmView
import com.example.finalproject.ui.view.film.InsertFilmView
import com.example.finalproject.ui.view.film.UpdateFilmView
import com.example.finalproject.ui.view.studio.DestinasiEditStudio
import com.example.finalproject.ui.view.studio.DestinasiEntryStudio
import com.example.finalproject.ui.view.studio.DestinasiHomeStudio
import com.example.finalproject.ui.view.studio.HomeStudioView
import com.example.finalproject.ui.view.studio.InsertStudioView
import com.example.finalproject.ui.view.studio.UpdateStudioView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    penayanganRepository: PenayanganRepository,
    filmRepository: FilmRepository,
    studioRepository: StudioRepository
) {
    NavHost(
        navController = navController,
        startDestination = "home", // Layar awal adalah HomeScreen
        modifier = Modifier
    ) {
        // Home Screen
        composable("home") {
            HomeScreen(navController = navController) // Menampilkan HomeScreen
        }

        // Home Film Screen
        composable(route = DestinasiHomeFilm.route) {
            HomeFilmView(
                navigateToItemEntry = { navController.navigate(DestinasiEntryFilm.route) }, // Navigasi ke Entry Film
                navigateBack = { navController.navigate("home") }, // Kembali ke Home
                onDetailClick = { id_film ->
                    navController.navigate("${DestinasiDetailFilm.route}/$id_film") // Navigasi ke Detail Film dengan argumen id_film
                }
            )
        }

        // Entry Film Screen
        composable(route = DestinasiEntryFilm.route) {
            InsertFilmView(
                navigateBack = {
                    navController.navigate(DestinasiHomeFilm.route) {
                        popUpTo(DestinasiHomeFilm.route) { inclusive = true } // Kembali ke HomeFilm dan hapus back stack
                    }
                }
            )
        }

        // Detail Film Screen
        composable(
            route = DestinasiDetailFilm.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailFilm.id_film) {
                type = NavType.IntType // Argumen id_film bertipe Int
            })
        ) { backStackEntry ->
            val id_film = backStackEntry.arguments?.getInt(DestinasiDetailFilm.id_film)
            id_film?.let {
                DetailFilmView(
                    navigateBack = { navController.navigateUp() }, // Kembali ke layar sebelumnya
                    onEditClick = { id_film ->
                        navController.navigate("${DestinasiEditFilm.route}/$id_film") // Navigasi ke Edit Film dengan argumen id_film
                    }
                )
            }
        }

        // Edit Film Screen
        composable(
            route = DestinasiEditFilm.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditFilm.id_film) {
                type = NavType.IntType // Argumen id_film bertipe Int
            })
        ) { backStackEntry ->
            val id_film = backStackEntry.arguments?.getInt(DestinasiEditFilm.id_film)
            id_film?.let {
                UpdateFilmView(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = {
                        navController.navigate(DestinasiHomeFilm.route) {
                            popUpTo(DestinasiHomeFilm.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Home Studio Screen
        composable(route = DestinasiHomeStudio.route) {
            HomeStudioView(
                navController = navController, // Teruskan NavController ke HomeStudioView
                navigateToItemEntry = { navController.navigate(DestinasiEntryStudio.route) }, // Navigasi ke Entry Studio
                navigateBack = { navController.navigate("home") } // Kembali ke Home
            )
        }

        // Entry Studio Screen
        composable(route = DestinasiEntryStudio.route) {
            InsertStudioView(
                navigateBack = {
                    navController.navigate(DestinasiHomeStudio.route) {
                        popUpTo(DestinasiHomeStudio.route) { inclusive = true } // Kembali ke HomeStudio dan hapus back stack
                    }
                }
            )
        }

        // Edit Studio Screen
        composable(
            route = DestinasiEditStudio.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditStudio.id_studio) {
                type = NavType.IntType // Argumen id_studio bertipe Int
            })
        ) { backStackEntry ->
            val idStudio = backStackEntry.arguments?.getInt(DestinasiEditStudio.id_studio)
            idStudio?.let {
                UpdateStudioView(
                    navigateBack = { navController.popBackStack() }, // Kembali ke layar sebelumnya
                    onNavigateUp = {
                        navController.navigate(DestinasiHomeStudio.route) {
                            popUpTo(DestinasiHomeStudio.route) { inclusive = true } // Kembali ke HomeStudio dan hapus back stack
                        }
                    }
                )
            }
        }

        // Home Penayangan Screen
        composable(route = DestinasiHomePenayangan.route) {
            Log.d("PengelolaHalaman", "Navigating to HomePenayanganView")
            HomePenayanganView(
                navController = navController,
                navigateToItemEntry = { navController.navigate(DestinasiEntryPenayangan.route) },
                navigateBack = { navController.navigate("home") }
            )
        }

        // Detail Penayangan Screen
        composable(
            route = DestinasiDetailPenayangan.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailPenayangan.id_penayangan) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val idPenayangan = backStackEntry.arguments?.getInt(DestinasiDetailPenayangan.id_penayangan)
            Log.d("PengelolaHalaman", "Navigating to DetailPenayanganView with id: $idPenayangan")
            idPenayangan?.let {
                DetailPenayanganView(
                    navigateBack = { navController.navigateUp() },
                    onBeliTiketClick = { idPenayangan ->
                        // Navigasi ke halaman pembelian tiket dengan membawa id_penayangan
                        navController.navigate("${DestinasiEntryTiket.route}/$idPenayangan")
                    },
                    navController = navController
                )
            } ?: run {
                Log.e("PengelolaHalaman", "idPenayangan is null or invalid")
            }
        }

        // Entry Tiket Screen (dengan argumen id_penayangan dan hargaTiket)
        composable(
            route = DestinasiEntryTiket.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiEntryTiket.id_penayangan) { type = NavType.IntType },
                navArgument(DestinasiEntryTiket.hargaTiket) { type = NavType.FloatType } // Harga tiket bertipe Float
            )
        ) { backStackEntry ->
            val idPenayangan = backStackEntry.arguments?.getInt(DestinasiEntryTiket.id_penayangan)
            val hargaTiket = backStackEntry.arguments?.getFloat(DestinasiEntryTiket.hargaTiket)?.toDouble()

            if (idPenayangan != null && hargaTiket != null) {
                InsertTiketView(
                    navigateBack = {
                        navController.navigate(DestinasiHomeTiket.route) {
                            popUpTo(DestinasiHomeTiket.route) { inclusive = true }
                        }
                    },
                    idPenayangan = idPenayangan, // Kirim id_penayangan
                    hargaTiket = hargaTiket // Kirim hargaTiket
                )
            } else {
                // Handle error jika idPenayangan atau hargaTiket null
                Log.e("PengelolaHalaman", "idPenayangan or hargaTiket is null")
            }
        }

        // Entry Penayangan Screen
        composable(route = DestinasiEntryPenayangan.route) {
            InsertPenayanganView(
                navigateBack = {
                    navController.navigate(DestinasiHomePenayangan.route) {
                        popUpTo(DestinasiHomePenayangan.route) { inclusive = true }
                    }
                },
                filmRepository = filmRepository,
                studioRepository = studioRepository
            )
        }

        // Edit Penayangan Screen
        composable(
            route = DestinasiEditPenayangan.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditPenayangan.id_penayangan) {
                type = NavType.IntType // Argumen id_penayangan bertipe Int
            })
        ) { backStackEntry ->
            val idPenayangan = backStackEntry.arguments?.getInt(DestinasiEditPenayangan.id_penayangan)
            idPenayangan?.let {
                UpdatePenayanganView(
                    navigateBack = { navController.popBackStack() }, // Kembali ke layar sebelumnya
                    onNavigateUp = {
                        navController.navigate(DestinasiHomePenayangan.route) {
                            popUpTo(DestinasiHomePenayangan.route) { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }
        }

        // Home Tiket Screen
        composable(route = DestinasiHomeTiket.route) {
            HomeTiketView(
                navController = navController,
                navigateToItemEntry = { navController.navigate(DestinasiEntryTiket.route) },
                navigateBack = { navController.navigate("home") }
            )
        }

        // Edit Tiket Screen
        composable(
            route = DestinasiEditTiket.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditTiket.id_tiket) {
                type = NavType.IntType // Argumen id_tiket bertipe Int
            })
        ) { backStackEntry ->
            val idTiket = backStackEntry.arguments?.getInt(DestinasiEditTiket.id_tiket)
            idTiket?.let {
                UpdateTiketView(
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = {
                        navController.navigate(DestinasiHomeTiket.route) {
                            popUpTo(DestinasiHomeTiket.route) { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }
        }

        composable(
            route = DestinasiEntryTiket.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiEntryTiket.id_penayangan) { type = NavType.IntType },
                navArgument(DestinasiEntryTiket.hargaTiket) { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val idPenayangan = backStackEntry.arguments?.getInt(DestinasiEntryTiket.id_penayangan)
            val hargaTiket = backStackEntry.arguments?.getFloat(DestinasiEntryTiket.hargaTiket)?.toDouble()

            if (idPenayangan != null && hargaTiket != null) {
                InsertTiketView(
                    navigateBack = {
                        navController.navigate(DestinasiHomeTiket.route) {
                            popUpTo(DestinasiHomeTiket.route) { inclusive = true }
                        }
                    },
                    idPenayangan = idPenayangan,
                    hargaTiket = hargaTiket
                )
            } else {
                Log.e("PengelolaHalaman", "idPenayangan or hargaTiket is null")
            }
        }
    }
}