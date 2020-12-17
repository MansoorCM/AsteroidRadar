package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeowsApi
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _NavigateToDetail = MutableLiveData<Asteroid>()
    val NavigateToDetail: LiveData<Asteroid>
        get() = _NavigateToDetail

    fun NavigateHelper(asteroid: Asteroid) {
        _NavigateToDetail.value = asteroid
    }

    fun onNavigateToDetailComplete() {
        _NavigateToDetail.value = null
    }

    val database = getDatabase(application.applicationContext)
    val repository = MainRepository(database)
    var asteroidList = repository.asteroidList

    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay>
        get() = _picture

    init {
        viewModelScope.launch {
            repository.refreshDatabase()
            getPictureOfDay()
            repository.deleteFromDatabase()
        }
    }

    suspend fun getPictureOfDay() {
        try {
            _picture.value = NeowsApi.retrofitService.getPicture(Constants.API_KEY).await()
        } catch (exception: Exception) {
            Log.i("MainViewModel", exception.message.toString())
        }

    }

    class MainViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}