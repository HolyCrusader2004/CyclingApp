package com.example.cyclingapp.roomDb

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cycling_runs")
data class CyclingRun(
    var image:Bitmap? = null,
    var name:String = "",
    var timestamp:Long = 0L,
    var avgSpeedInKmh: Float = 0f,
    var distance: Int = 0,
    var timeinmillis: Long = 0L,
    var caloriesBurned: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L
}
