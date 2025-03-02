package com.example.cyclingapp.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.cyclingapp.viewModels.MainViewModel
import com.example.cyclingapp.MainActivity
import com.example.cyclingapp.TrackingLogic.LocationUtils
import com.example.cyclingapp.roomDb.CyclingRun
import com.example.cyclingapp.viewModels.UserViewModel

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CycleScreen(viewModel: MainViewModel, userViewModel: UserViewModel, onNavigate: (String) -> Unit, context: Context, locationUtils: LocationUtils) {
    val scaffoldState = rememberScaffoldState()
    var selectedTab by remember { mutableStateOf(0) }

    var selectedSortOption by remember { mutableStateOf("Date") }
    val sortOptions = listOf("Date", "Distance", "Duration", "AvgSpeed")
    var dropdownExpanded by remember { mutableStateOf(false) }

    val runs by when (selectedSortOption) {
        "Date" -> viewModel.getSortedRunsByDate().observeAsState(emptyList())
        "Distance" -> viewModel.getSortedRunsByDistance().observeAsState(emptyList())
        "Duration" -> viewModel.getSortedRunsByTime().observeAsState(emptyList())
        "AvgSpeed" -> viewModel.getSortedRunsBySpeed().observeAsState(emptyList())
        else -> viewModel.getSortedRunsByDate().observeAsState(emptyList())
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions() ,
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                Toast.makeText(context,
                    "You have permission to proceed", Toast.LENGTH_LONG)
                    .show()
            }else{
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if(rationaleRequired){
                    Toast.makeText(context,
                        "Location Permission is required for this feature to work", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(context,
                        "Location Permission is required. Please enable it in the Android Settings",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome, ${userViewModel.name.value}!") },
                actions = {
                    Box(modifier = Modifier.padding(end = 16.dp)){
                        Button(onClick = { dropdownExpanded = true }) {
                            Text(text = "Sort by: $selectedSortOption")
                        }
                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            sortOptions.forEach { option ->
                                DropdownMenuItem(onClick = {
                                    if(!selectedSortOption.equals(option)){
                                        selectedSortOption = option
                                        when(selectedSortOption){
                                            "Date" -> viewModel.getSortedRunsByDate()
                                            "Distance" -> viewModel.getSortedRunsByDistance()
                                            "Duration" -> viewModel.getSortedRunsByTime()
                                            "AvgSpeed" -> viewModel.getSortedRunsBySpeed()
                                        }
                                    }
                                    dropdownExpanded = false
                                }) {
                                    Text(text = option)
                                }
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (locationUtils.hasLocationPermission(context)){
                    Toast.makeText(context,"You have permissions", Toast.LENGTH_LONG).show()
                    onNavigate("addRide")
                }else{
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION))
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0 }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Statistics") },
                    label = { Text("Runs") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; onNavigate("statistics") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2; onNavigate("settings") }
                )
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(runs, key = {cycleRun -> cycleRun.id}){
                    cyclerun ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                                viewModel.deleteRun(cyclerun)
                            }
                            true
                        }
                    )
                    SwipeToDismiss(state = dismissState,
                        background = {
                            val color by animateColorAsState(
                                if(dismissState.dismissDirection == DismissDirection.EndToStart){
                                    Color.Red
                                }else{
                                    Color.Transparent
                                }, label = ""
                            )
                            val alignment = Alignment.CenterEnd
                            Box (modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp), contentAlignment = alignment){
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                            }
                        },
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { FractionalThreshold(0.5f) },
                        dismissContent = {
                            RunItem(run = cyclerun)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun RunItem(run: CyclingRun) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            ,
        elevation = 4.dp
    ) {
        Column (modifier = Modifier.fillMaxWidth()){
            run.image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Run Image",
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Cyclist: ${run.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Distance: ${run.distance} m", fontSize = 16.sp)
                Text(text = "Avg Speed: ${run.avgSpeedInKmh} km/h", fontSize = 16.sp)
                Text(text = "Calories: ${run.caloriesBurned} kcal", fontSize = 16.sp)
            }
        }
    }
}