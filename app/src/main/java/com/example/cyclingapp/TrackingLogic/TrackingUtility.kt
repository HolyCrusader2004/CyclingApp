package com.example.cyclingapp.TrackingLogic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import java.util.concurrent.TimeUnit

object TrackingUtility {
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    const val ACTION_START_OR_RESUME = "ACTION_START_OR_RESUME"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_STOP = "ACTION_STOP"
    const val ACTION_SHOW_TRACKING = "ACTION_SHOW_TRACKING"

    fun getFormattedTime(seconds: Long): String {
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val sec = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, sec)
    }

    fun calculateDistance(polyline: Polyline): Float{
        var distance = 0f
        for(i in 0..polyline.size-2){
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]
            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return  distance
    }

}


