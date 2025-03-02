package com.example.cyclingapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    val name = mutableStateOf("Default Name")

    val weight = mutableStateOf("70")

    fun updateUserData(newName: String, newWeight: String) {
        name.value = newName
        weight.value = newWeight
    }
}