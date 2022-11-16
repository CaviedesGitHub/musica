package com.miso.musica.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.VolleyError
import com.miso.musica.database.CollectorsDao
import com.miso.musica.models.Collector
import com.miso.musica.network.NetworkServiceAdapter

class CollectorsRepository (val application: Application, private val collectorsDao: CollectorsDao){
    suspend fun refreshData(): List<Collector>{
        var cached = collectorsDao.getCollectors()
        return if(cached.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getCollectors()
        } else cached
    }
}