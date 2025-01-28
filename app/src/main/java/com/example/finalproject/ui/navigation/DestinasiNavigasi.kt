package com.example.finalproject.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

// Halaman Home Film
object DestinasiHomeFilm : DestinasiNavigasi {
    override val route = "home_film"
    override val titleRes = "Home Film"
}

// Halaman Detail Film (yg membutuhkan argumen id_film)
object DestinasiDetailFilm : DestinasiNavigasi {
    override val route = "detail_film"
    override val titleRes = "Detail Film"
    const val id_film = "id_film"
    val routeWithArgs = "$route/{$id_film}"
}

// Halaman Tambah Film
object DestinasiEntryFilm : DestinasiNavigasi {
    override val route = "entry_film"
    override val titleRes = "Tambah Film"
}

// Halaman Edit Film (yg membutuhkan argumen id_film)
object DestinasiEditFilm : DestinasiNavigasi {
    override val route = "film_edit"
    override val titleRes = "Edit Film"
    const val id_film = "id_film"
    val routeWithArgs = "$route/{$id_film}"
}

// Halaman Home Studio
object DestinasiHomeStudio : DestinasiNavigasi {
    override val route = "home_studio"
    override val titleRes = "Home Studio"
}



// Halaman Tambah Studio
object DestinasiEntryStudio : DestinasiNavigasi {
    override val route = "entry_studio"
    override val titleRes = "Tambah Studio"
}

// Halaman Edit Studio (yg membutuhkan argumen id_studio)
object DestinasiEditStudio : DestinasiNavigasi {
    override val route = "edit_studio"
    override val titleRes = "Edit Studio"
    const val id_studio = "id_studio"
    val routeWithArgs = "$route/{$id_studio}"
}

// Halaman Home Penayangan
object DestinasiHomePenayangan : DestinasiNavigasi {
    override val route = "home_penayangan"
    override val titleRes = "Home Penayangan"
}



// Halaman Tambah Penayangan
object DestinasiEntryPenayangan : DestinasiNavigasi {
    override val route = "entry_penayangan"
    override val titleRes = "Tambah Penayangan"
}

// Halaman Edit Penayangan (yg membutuhkan argumen id_penayangan)
object DestinasiEditPenayangan : DestinasiNavigasi {
    override val route = "edit_penayangan"
    override val titleRes = "Edit Penayangan"
    const val id_penayangan = "id_penayangan"
    val routeWithArgs = "$route/{$id_penayangan}"
}

// Halaman Detail Penayangan (yg membutuhkan argumen id_penayangan)
object DestinasiDetailPenayangan : DestinasiNavigasi {
    override val route = "detail_penayangan"
    override val titleRes = "Detail Penayangan"
    const val id_penayangan = "id_penayangan"
    val routeWithArgs = "$route/{$id_penayangan}"
}

// Halaman Home Tiket
object DestinasiHomeTiket : DestinasiNavigasi {
    override val route = "home_tiket"
    override val titleRes = "Home Tiket"
}

// Halaman Tambah Tiket
object DestinasiEntryTiket : DestinasiNavigasi {
    override val route = "entry_tiket"
    override val titleRes = "Entry Tiket"
    const val id_penayangan = "id_penayangan"
    const val hargaTiket = "hargaTiket"
    val routeWithArgs = "$route/{$id_penayangan}?$hargaTiket={$hargaTiket}"
}



// Halaman Edit Tiket (yg membutuhkan argumen id_tiket)
object DestinasiEditTiket : DestinasiNavigasi {
    override val route = "edit_tiket"
    override val titleRes = "Edit Tiket"
    const val id_tiket = "id_tiket"
    val routeWithArgs = "$route/{$id_tiket}"
}