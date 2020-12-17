package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Asteroid::class],version = 1,exportSchema = false)
abstract class AsteroidDb:RoomDatabase() {
    abstract val dao:AsteroidDao
}

private lateinit var INSTANCE:AsteroidDb

fun getDatabase(context: Context):AsteroidDb{
    synchronized(AsteroidDb::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE=Room.databaseBuilder(context.applicationContext,AsteroidDb::class.java,"AsteroidDb")
                    .fallbackToDestructiveMigration()
                    .build()
        }
        return INSTANCE
    }
}