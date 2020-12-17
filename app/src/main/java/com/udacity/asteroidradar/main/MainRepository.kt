package com.udacity.asteroidradar.main

import android.util.Log
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeowsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainRepository(val database: AsteroidDb) {

    suspend fun refreshDatabase() {
        withContext(Dispatchers.IO) {
            val (startDate, endDate) = getStartAndEndDate()
            try {
                val data = NeowsApi.retrofitService.getData(
                    startDate,
                    endDate, Constants.API_KEY
                ).await()
                val asteroidList = parseAsteroidsJsonResult(JSONObject(data))
                database.dao.insertAsteroid(*asteroidList.toTypedArray())
            } catch (exception: Exception) {
                Log.i("MainRepository", exception.message.toString())
            }

        }
    }

    //get startdate and enddate strings with startdate as today and a difference of 7days.
    private fun getStartAndEndDate(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        var currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val startDate = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        currentTime = calendar.time
        val endDate = dateFormat.format(currentTime)
        return Pair(startDate, endDate)
    }

    suspend fun deleteFromDatabase(){
        withContext(Dispatchers.IO){
            database.dao.deletePrevious()
        }
    }

    val asteroidList = database.dao.getAll()
}