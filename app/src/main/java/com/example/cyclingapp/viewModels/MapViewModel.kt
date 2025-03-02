package com.example.cyclingapp.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclingapp.DependencyInjection.AppModule
import com.example.cyclingapp.TrackingLogic.Polyline
import com.example.cyclingapp.TrackingLogic.TrackingService
import com.example.cyclingapp.TrackingLogic.TrackingUtility
import com.example.cyclingapp.repositories.MainRepository
import com.example.cyclingapp.roomDb.CyclingRun
import com.example.cyclingapp.screens.sendCommandToService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.round

class MapViewModel( private val mainRepository: MainRepository = AppModule.cyclingRepo): ViewModel() {
    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> get() = _isTracking

    private val _pathPoints = MutableLiveData<MutableList<Polyline>>()
    val pathPoints: LiveData<MutableList<Polyline>> get() = _pathPoints

    init {
        _isTracking.value = TrackingService.isTracking.value ?: false
        _pathPoints.value = TrackingService.pathPoints.value ?: mutableListOf()
        TrackingService.pathPoints.observeForever{
            newpoints ->
            _pathPoints.value = newpoints
            Log.d("new points", "${_pathPoints.value}")
        }
    }

    fun updateTracking(isTracking: Boolean){
        _isTracking.value = isTracking
    }

    fun zoomToSeeWholeRoute(map: GoogleMap){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints.value!!){
            for (pos in polyline){
                bounds.include(pos)
            }
        }
        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                75,
            )
        )
    }

    fun endRunAndSave(bmp: Bitmap, currentTime: Long, userViewModel: UserViewModel, context: Context){
            var distance = 0
            for(polyline in pathPoints.value!!){
                distance += TrackingUtility.calculateDistance(polyline).toInt()
            }
            val avgSpeed = round((distance / 1000f) / (currentTime * 1000 / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distance / 1000f) * userViewModel.weight.value.toFloat()).toInt()
            val run = CyclingRun(bmp, userViewModel.name.value, dateTimeStamp, avgSpeed, distance, currentTime * 1000, caloriesBurned)
            viewModelScope.launch {
                mainRepository.insertCycleRun(run)
            }
            cancelRun(context)
    }


    fun toggleRun(context: Context) {
        if (isTracking.value == true) {
            sendCommandToService(TrackingUtility.ACTION_PAUSE, context = context)
        } else {
            Log.d("debug", "am ajuns aici")
            sendCommandToService(TrackingUtility.ACTION_START_OR_RESUME, context = context)
        }
    }

    fun cancelRun(context: Context){
        _isTracking.value = false
        sendCommandToService(TrackingUtility.ACTION_STOP, context)
    }
}