package com.miso.musica.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.android.volley.VolleyError
import com.miso.musica.database.AlbumsDao
import com.miso.musica.models.Album
import com.miso.musica.network.NetworkServiceAdapter

class AlbumsRepository (val application: Application, private val albumsDao: AlbumsDao){
    suspend fun refreshData(): List<Album>{
        Log.d("AlbumRepository","Inicio")
        var cached = albumsDao.getAlbums()
        Log.d("AlbumRepository","Despues de consultar cache")
        return if(cached.isNullOrEmpty()){
            Log.d("AlbumRepository","Cache Vacio")
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            Log.d("AlbumRepository","Despues de cm")
            if(false){ //( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getAlbums()
        } else cached
    }
}