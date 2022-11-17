package com.miso.musica.repositories

import android.app.Application
import com.miso.musica.models.Musician
import com.miso.musica.network.NetworkServiceAdapter

class MusicianDetRepository(val application: Application) {
    suspend fun refreshData(musicianId: Int):Musician{
        return NetworkServiceAdapter.getInstance(application).getMusician(musicianId)
    }
}