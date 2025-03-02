package com.example.cyclingapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclingapp.TrackingLogic.TrackingUtility
import com.example.cyclingapp.viewModels.MainViewModel
import com.example.cyclingapp.viewModels.UserViewModel

@Composable
fun StatisticScreen(
    onNavigate: (String) -> Unit,
    viewModel: MainViewModel,
    userViewModel: UserViewModel
) {
    val scaffoldState = rememberScaffoldState()
    var selectedTab by remember { mutableStateOf(1) }

    val totalTimeRan = viewModel.getTotalTime(userViewModel.name.value).observeAsState().value ?: 0L
    val totalDistance = viewModel.getTotalDistance(userViewModel.name.value).observeAsState().value ?: 0
    val avgSpeed = viewModel.getTotalAvg(userViewModel.name.value).observeAsState().value ?: 0f
    val totalCalories = viewModel.getTotalCalories(userViewModel.name.value).observeAsState().value ?: 0

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text(text = "Statistics") })
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0; onNavigate("home") }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Statistics") },
                    label = { Text("Runs") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Your Statistics", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        StatsCard(title = "Total Time", value = TrackingUtility.getFormattedTime(totalTimeRan / 1000))
                        StatsCard(title = "Total Distance", value = "${totalDistance / 1000f} km")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatsCard(title = "Avg Speed", value = "$avgSpeed km/h")
                        StatsCard(title = "Calories Burned", value = "$totalCalories kcal")
                    }
                }
            }
        }
    )
}

@Composable
fun StatsCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(120.dp)
            .fillMaxHeight(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}