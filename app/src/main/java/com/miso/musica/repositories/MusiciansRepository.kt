package com.miso.musica.repositories

import android.app.Application
import com.miso.musica.models.Musician
import com.miso.musica.network.NetworkServiceAdapter

class MusiciansRepository(val application: Application) {
    suspend fun refreshData():List<Musician>{
        return NetworkServiceAdapter.getInstance(application).getMusicians()
    }
}