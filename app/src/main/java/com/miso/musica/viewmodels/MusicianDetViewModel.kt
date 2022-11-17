package com.miso.musica.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.miso.musica.models.Musician
import com.miso.musica.repositories.MusicianDetRepository
import com.miso.musica.repositories.MusiciansRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MusicianDetViewModel(application: Application, musicianId: Int) :  AndroidViewModel(application) {
    private val musicianDetRepository = MusicianDetRepository(application)

    private val _musician = MutableLiveData<Musician>()
    val musician: LiveData<Musician>
        get() = _musician

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = musicianId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch (Dispatchers.Default){
                withContext(Dispatchers.IO){
                    var data = musicianDetRepository.refreshData(id)
                    _musician.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        }
        catch (e: Exception){
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val musicianId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MusicianDetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MusicianDetViewModel(app, musicianId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}