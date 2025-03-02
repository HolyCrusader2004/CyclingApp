package com.example.cyclingapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cyclingapp.ui.theme.CyclingAppTheme
import com.example.cyclingapp.viewModels.MainViewModel
import com.example.cyclingapp.viewModels.MapViewModel
import com.example.cyclingapp.viewModels.UserViewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userViewModel: UserViewModel = viewModel()
            val mainViewModel: MainViewModel = viewModel()
            val mapViewModel: MapViewModel = viewModel()
            CyclingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, userViewModel, mainViewModel, mapViewModel)
                }
            }
        }
    }
}
