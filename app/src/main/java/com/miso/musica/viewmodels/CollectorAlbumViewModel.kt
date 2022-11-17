package com.miso.musica.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.bumptech.glide.Glide.init
import com.miso.musica.database.VinylRoomDatabase
import com.miso.musica.models.CollectorAlbum
import com.miso.musica.models.Comment
import com.miso.musica.repositories.CollectorAlbumRepository
import com.miso.musica.repositories.CommentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorAlbumViewModel(application: Application, collectorId: Int) :  AndroidViewModel(application) {
    private val collectoralbumRepository = CollectorAlbumRepository(application)

    private val _collectorAlbums = MutableLiveData<List<CollectorAlbum>>()
    val collectorAlbums: LiveData<List<CollectorAlbum>>
        get() = _collectorAlbums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = collectorId


    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    var data = collectoralbumRepository.refreshData(id)
                    _collectorAlbums.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        }
        catch (e:Exception){
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val collectorId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorAlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorAlbumViewModel(app, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}