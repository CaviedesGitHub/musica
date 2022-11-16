package com.miso.musica.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.miso.musica.models.Album
import com.miso.musica.network.NetworkServiceAdapter

class AlbumsRepository (val application: Application){
    suspend fun refreshData(): List<Album> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }
}