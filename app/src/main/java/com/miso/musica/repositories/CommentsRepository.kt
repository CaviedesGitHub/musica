package com.miso.musica.repositories

import android.app.Application
import android.util.Log
import com.android.volley.VolleyError
import com.miso.musica.models.Comment
import com.miso.musica.network.CacheManager
import com.miso.musica.network.NetworkServiceAdapter

class CommentsRepository (val application: Application){
    suspend fun refreshData(albumId: Int): List<Comment> {
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
    }
}