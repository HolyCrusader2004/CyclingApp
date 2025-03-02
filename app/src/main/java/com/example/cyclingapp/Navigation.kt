package com.example.cyclingapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cyclingapp.TrackingLogic.LocationUtils
import com.example.cyclingapp.screens.AddCycleRunScreen
import com.example.cyclingapp.screens.CycleScreen
import com.example.cyclingapp.screens.Screen
import com.example.cyclingapp.screens.SettingsScreen
import com.example.cyclingapp.screens.SetupScreen
import com.example.cyclingapp.screens.StatisticScreen
import com.example.cyclingapp.viewModels.MainViewModel
import com.example.cyclingapp.viewModels.MapViewModel
import com.example.cyclingapp.viewModels.UserViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NavigationGraph(navController: NavController, userViewModel: UserViewModel, mainViewModel: MainViewModel, mapViewModel: MapViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils()

    NavHost(navController = navController as NavHostController,
        startDestination = Screen.SetupScreen.route){
        composable(Screen.SetupScreen.route){
            SetupScreen{
                name, weight ->
                userViewModel.updateUserData(name, weight)
                navController.navigate(Screen.HomeScreen.route)
            }
        }
        composable(Screen.HomeScreen.route){
            CycleScreen(viewModel = mainViewModel, userViewModel = userViewModel, onNavigate = {
                page -> when (page) {
                "settings" -> {
                    navController.navigate(Screen.SettingScreen.route)
                }

                "statistics" -> {
                    navController.navigate(Screen.StatisticScreen.route)
                }

                "home" -> {
                    navController.navigate(Screen.HomeScreen.route)
                }

                "addRide" -> {
                    navController.navigate(Screen.AddCyclingScreen.route)
                }
            }
            }, context = context, locationUtils = locationUtils)
        }
        composable(Screen.StatisticScreen.route){
            StatisticScreen(viewModel = mainViewModel, userViewModel = userViewModel, onNavigate ={
                page -> when (page){
                "settings" -> {
                    navController.navigate(Screen.SettingScreen.route)
                }
                "statistics" -> {
                    navController.navigate(Screen.StatisticScreen.route)
                }
                "home" -> {
                    navController.navigate(Screen.HomeScreen.route)
                }
            }
            })
        }
        composable(Screen.SettingScreen.route){
            SettingsScreen(userViewModel = userViewModel,
                onNavigate = {
                    page -> when (page){
                    "settings" -> {
                        navController.navigate(Screen.SettingScreen.route)
                    }
                    "statistics" -> {
                        navController.navigate(Screen.StatisticScreen.route)
                    }
                    "home" -> {
                        navController.navigate(Screen.HomeScreen.route)
                    }
                }
            })
        }
        composable(Screen.AddCyclingScreen.route){
            AddCycleRunScreen(context = context,
                mapViewModel = mapViewModel,
                onCancelRun = {
                    navController.navigate(Screen.HomeScreen.route)
                },
                userViewModel = userViewModel)
        }
    }
}