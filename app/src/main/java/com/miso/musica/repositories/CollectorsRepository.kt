package com.miso.musica.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.miso.musica.models.Collector
import com.miso.musica.network.NetworkServiceAdapter

class CollectorsRepository (val application: Application){
    suspend fun refreshData(): List<Collector> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getCollectors()
    }
}