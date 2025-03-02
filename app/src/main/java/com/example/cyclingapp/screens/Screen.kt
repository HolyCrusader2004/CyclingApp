package com.example.cyclingapp.screens

sealed class Screen (val route: String){
    object SetupScreen: Screen("setupscreen")
    object HomeScreen: Screen("cyclinghomescreen")
    object StatisticScreen: Screen("statisticscreen")
    object SettingScreen: Screen("settingscreen")
    object AddCyclingScreen: Screen("addcyclingscreen")
}