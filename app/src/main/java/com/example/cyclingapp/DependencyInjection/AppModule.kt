package com.example.cyclingapp.DependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.cyclingapp.repositories.MainRepository
import com.example.cyclingapp.roomDb.CyclingDb

object AppModule {

    lateinit var database: CyclingDb

    val cyclingRepo by lazy {
        MainRepository(cycleDao = database.getCycleDao())
    }

    fun provideDb(context: Context) {
        database = Room.databaseBuilder(context, CyclingDb::class.java, "cycling_db").build()
    }

}