package com.example.cyclingapp.screens

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclingapp.TrackingLogic.TrackingService
import com.example.cyclingapp.TrackingLogic.TrackingUtility
import com.example.cyclingapp.viewModels.MapViewModel
import com.example.cyclingapp.viewModels.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AddCycleRunScreen(context: Context, mapViewModel: MapViewModel, onCancelRun: () -> Unit, userViewModel: UserViewModel) {

    var showDialogSave by remember { mutableStateOf(false) }
    var elapsedSaveTime by remember { mutableStateOf(0L) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(2f)) {
            GoogleMapView(mapViewModel, showDialogSave, onDismissSaveDialog = { showDialogSave = false }, onRedirect = onCancelRun, context, elapsedSaveTime, userViewModel)
        }
        Box(modifier = Modifier.weight(1f)) {
            TimerSection(
                context = context,
                mapViewModel = mapViewModel,
                onCancelRun = onCancelRun,
                onSaveRun = {elapsedTime ->
                    elapsedSaveTime = elapsedTime
                    showDialogSave = true }
            )
        }
    }
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun GoogleMapView(mainViewModel: MapViewModel, showDialogSave: Boolean, onDismissSaveDialog: () -> Unit, onRedirect: () -> Unit, context: Context, elapsedTime: Long, userViewModel: UserViewModel) {
    val pathPoints = mainViewModel.pathPoints.observeAsState(emptyList())
    val isRunning = mainViewModel.isTracking.observeAsState(false)
    var saveRun by remember { mutableStateOf(false) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        MapEffect(key1 = isRunning.value) {
            map ->
            googleMap = map
            while (isRunning.value) {
                for (pathLine in pathPoints.value) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(pathLine)
                            .color(Color.Red.toArgb())
                            .width(8f)
                    )
                }
                if (pathPoints.value.isNotEmpty() && pathPoints.value.last().isNotEmpty()) {
                    val lastLocation = pathPoints.value.last().last()
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(lastLocation, 15f),
                        durationMs = 1000
                    )
                }
                kotlinx.coroutines.delay(4000L)
            }
        }
    }

    LaunchedEffect(saveRun) {
        if (saveRun) {
            saveRun = false
            googleMap?.let { map ->
                mainViewModel.zoomToSeeWholeRoute(map)
                mainViewModel.toggleRun(context)
                mainViewModel.updateTracking(false)
                Handler(Looper.getMainLooper()).post {
                    map.snapshot { bmp ->
                        if (bmp != null) {
                            mainViewModel.endRunAndSave(bmp, elapsedTime, userViewModel, context)
                        }
                        onRedirect()
                    }
                }
            }
        }
    }

    if (showDialogSave){
        AlertDialog(onDismissRequest = {onDismissSaveDialog()},
            title = { Text("Save Run?") },
            text = { Text("Are you sure you want to save this run?") },
            confirmButton = {
                Button(onClick = {
                    onDismissSaveDialog()
                    saveRun = true
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { onDismissSaveDialog()}) {
                    Text("No")
                }
            }
        )
    }
}


@Composable
fun TimerSection(context: Context, mapViewModel: MapViewModel, onCancelRun: () -> Unit, onSaveRun: (elapsedTime : Long) -> Unit) {
    val isRunning by mapViewModel.isTracking.observeAsState(false)
    var elapsedTime by remember { mutableStateOf(0L) }
    var hasStarted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            kotlinx.coroutines.delay(1000L)
            elapsedTime++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = TrackingUtility.getFormattedTime(elapsedTime), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                hasStarted = true
                mapViewModel.toggleRun(context)
                mapViewModel.updateTracking(!isRunning)
            }) {
                Text(text = if (isRunning) "Stop" else "Start")
            }

            if (hasStarted) {
                Button(onClick = {
                    showDialog = true
                }) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    onSaveRun(elapsedTime)
                }) {
                    Text(text = "Save run")
                }
            }
        }
    }

    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog = false},
            title = { Text("Cancel Run?") },
            text = { Text("Are you sure you want to cancel the run? This will reset the timer.") },
            confirmButton = {
                Button(onClick = {
                    hasStarted = false
                    showDialog = false
                    mapViewModel.cancelRun(context)
                    onCancelRun()
                }) {
                    Text("Yes, Cancel")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

fun sendCommandToService(action: String, context: Context) =
    Intent(context, TrackingService::class.java).also {
        it.action = action
        context.startService(it)
    }
