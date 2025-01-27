package com.example.finalproject


import android.app.Application

class BioskopApplication : Application() {
    // Tidak perlu membuat instance BioskopContainer karena sudah berupa object
    val container: BioskopContainer
        get() = BioskopContainer // Langsung akses object BioskopContainer

    override fun onCreate() {
        super.onCreate()
        // Tidak perlu inisialisasi karena BioskopContainer sudah berupa object
    }
}