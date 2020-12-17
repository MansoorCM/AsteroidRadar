package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(vararg asteroidList: Asteroid)

    @Query("SELECT * FROM asteroid WHERE date(closeApproachDate)>=date() " +
            "ORDER BY date(closeApproachDate) DESC")
    fun getAll(): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE date(closeApproachDate)<date()")
    fun deletePrevious()

}