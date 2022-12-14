package com.miso.musica.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.miso.musica.models.Collector
import com.miso.musica.network.NetworkServiceAdapter

class CollectorViewModel(application: Application) :  AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>>
        get() = _collectors

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        NetworkServiceAdapter.getInstance(getApplication()).getCollectors({
            _collectors.postValue(it)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        },{
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}