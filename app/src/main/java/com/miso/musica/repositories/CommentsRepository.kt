package com.miso.musica.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.android.volley.VolleyError
import com.miso.musica.database.CommentsDao
import com.miso.musica.models.Comment
import com.miso.musica.network.CacheManager
import com.miso.musica.network.NetworkServiceAdapter

class CommentsRepository (val application: Application, private val commentsDao: CommentsDao){
    suspend fun refreshData(albumId: Int): List<Comment>{
        var cached = commentsDao.getComments(albumId)
        return if(cached.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getComments(albumId)
        } else cached
    }
    /**suspend fun refreshData(albumId: Int): List<Comment> {
        var potentialResp = CacheManager.getInstance(application.applicationContext).getComments(albumId)
        if(potentialResp.isEmpty()){
            Log.d("Cache decision", "get from network")
            var comments = NetworkServiceAdapter.getInstance(application).getComments(albumId)
            CacheManager.getInstance(application.applicationContext).addComments(albumId, comments)
            return comments
        }
        else{
            Log.d("Cache decision", "return ${potentialResp.size} elements from cache")
            return potentialResp
        }
    }**/
}