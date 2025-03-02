package com.example.cyclingapp.TrackingLogic

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService :LifecycleService() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isFirstRun = true
    private var serviceKilled = false

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.observe(
            this,
            Observer {
                updateLocationStatus(it)
            }
        )
    }

    private fun killService(){
        serviceKilled = true
        isFirstRun =  true
        pauseService()
        postInitialValues()
        stopSelf()
    }

    private fun pauseService(){
        isTracking.postValue(false)
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateLocationStatus(isTracking: Boolean){
        if (isTracking){
            if(TrackingUtility.hasLocationPermission(this)){
                val request = LocationRequest().apply {
                    interval = 5000L
                    fastestInterval = 2000L
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value == true){
                result?.locations?.let {
                    locations ->
                    for (location in locations){
                        addPathPoint(location)
                        Log.d("new location","${location.longitude} - ${location.latitude}" )
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            val updatedPathPoints = pathPoints.value ?: mutableListOf()
            if (updatedPathPoints.isEmpty()) {
                updatedPathPoints.add(mutableListOf())
            }
            updatedPathPoints.last().add(pos)
            pathPoints.postValue(updatedPathPoints.toMutableList())
            Log.d("TrackingService", "Added point: $pos")
            Log.d("TrackingService", "Updated pathPoints: $updatedPathPoints")
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                TrackingUtility.ACTION_START_OR_RESUME -> {
                    if (isFirstRun){
                        addEmptyPolyline()
                        isTracking.postValue(true)
                        isFirstRun = false
                    }else{
                        addEmptyPolyline()
                        isTracking.postValue(true)
                    }
                }

                TrackingUtility.ACTION_PAUSE -> {
                    pauseService()
                }

                TrackingUtility.ACTION_STOP -> {
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}