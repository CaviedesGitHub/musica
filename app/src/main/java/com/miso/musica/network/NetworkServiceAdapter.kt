package com.miso.musica.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.miso.musica.models.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import java.text.SimpleDateFormat

class NetworkServiceAdapter constructor(context: Context) {
    val formatter = SimpleDateFormat("yyyy.MM.dd")  //"yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    companion object{
        const val BASE_URL= "https://back-vinyls-populated.herokuapp.com/"
        //const val BASE_URL= "https://vinyl-miso.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    suspend fun getAlbums()= suspendCoroutine<List<Album>>{ cont ->
        requestQueue.add(getRequest("albums",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description")))
                }
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }
    suspend fun getAlbumsCollector(collectorId:Int) = suspendCoroutine<List<CollectorAlbum>>{ cont->
        requestQueue.add(getRequest("collectors/$collectorId/albums",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<CollectorAlbum>()
                var item:JSONObject? = null
                var itemAlbum:JSONObject? = null
                var album:Album?=null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    itemAlbum=item.getJSONObject("album")
                    Log.d("Response", item.toString())
                    album = Album(albumId = itemAlbum.getInt("id"), name = itemAlbum.getString("name"), cover = itemAlbum.getString("cover"), recordLabel = itemAlbum.getString("recordLabel"), releaseDate = itemAlbum.getString("releaseDate"), genre = itemAlbum.getString("genre"), description = itemAlbum.getString("description"))
                    list.add(i, CollectorAlbum(id = item.getInt("id"), price = item.getDouble("price"), status = item.getString("status"), album=album))
                }
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }
    suspend fun getCollectors() = suspendCoroutine<List<Collector>>{ cont->
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) { //inicializado como variable de retorno
                    val item = resp.getJSONObject(i)
                    val collector = Collector(collectorId = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email"))
                    list.add(collector) //se agrega a medida que se procesa la respuesta
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getMusicians() = suspendCoroutine<List<Musician>>{ cont->
        requestQueue.add(getRequest("musicians",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Musician>()
                for (i in 0 until resp.length()) { //inicializado como variable de retorno
                    val item = resp.getJSONObject(i)
                    val listAlbums = mutableListOf<Album>()
                    val lstAlbums=item.getJSONArray("albums")
                    for (j in 0 until lstAlbums.length()) {
                        val itemAlbum = lstAlbums.getJSONObject(j)
                        listAlbums.add(j, Album(albumId = itemAlbum.getInt("id"),name = itemAlbum.getString("name"), cover = itemAlbum.getString("cover"), recordLabel = itemAlbum.getString("recordLabel"), releaseDate = itemAlbum.getString("releaseDate"), genre = itemAlbum.getString("genre"), description = itemAlbum.getString("description")))
                    }
                    list.add(Musician(musicianId = item.getInt("id"), name = item.getString("name"), image = item.getString("image"), description = item.getString("description"), birthDate = item.getString("birthDate"), albums = listAlbums)) //se agrega a medida que se procesa la respuesta
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getMusician(musicianId: Int) = suspendCoroutine<Musician>{ cont->
        requestQueue.add(getRequest("musicians/$musicianId",
            { response ->
                val resp=JSONObject(response)
                val listAlbums = mutableListOf<Album>()
                val lstAlbums=resp.getJSONArray("albums")
                for (j in 0 until lstAlbums.length()) {
                    val itemAlbum = lstAlbums.getJSONObject(j)
                    listAlbums.add(j, Album(albumId = itemAlbum.getInt("id"),name = itemAlbum.getString("name"), cover = itemAlbum.getString("cover"), recordLabel = itemAlbum.getString("recordLabel"), releaseDate = itemAlbum.getString("releaseDate"), genre = itemAlbum.getString("genre"), description = itemAlbum.getString("description")))
                }
                val musician=Musician(musicianId = resp.getInt("id"), name = resp.getString("name"), image = resp.getString("image"), description = resp.getString("description"), birthDate = resp.getString("birthDate"), albums = listAlbums)
                cont.resume(musician)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getComments(albumId:Int) = suspendCoroutine<List<Comment>>{ cont->
        requestQueue.add(getRequest("albums/$albumId/comments",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Comment>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    Log.d("Response", item.toString())
                    list.add(i, Comment(albumId = albumId, rating = item.getInt("rating").toString(), description = item.getString("description")))
                }
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }
    fun postComment(body: JSONObject, albumId: Int,  onComplete:(resp:JSONObject)->Unit , onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums/$albumId/comments",
            body,
            Response.Listener<JSONObject> { response ->
                onComplete(response)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}