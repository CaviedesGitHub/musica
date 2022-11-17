package com.miso.musica.repositories

import android.app.Application
import com.miso.musica.models.CollectorAlbum
import com.miso.musica.network.NetworkServiceAdapter

class CollectorAlbumRepository(val application: Application) {
    suspend fun refreshData(collectorId: Int):List<CollectorAlbum>{
    return NetworkServiceAdapter.getInstance(application).getAlbumsCollector(collectorId)
    }
}