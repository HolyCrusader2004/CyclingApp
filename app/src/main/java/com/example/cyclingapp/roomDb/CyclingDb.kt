package com.example.cyclingapp.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CyclingRun::class],
    version = 1
)

@TypeConverters(ImageConverter::class)
abstract class CyclingDb: RoomDatabase() {
    abstract fun getCycleDao(): CyclingDAO
}