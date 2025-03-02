package com.example.cyclingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclingapp.DependencyInjection.AppModule
import com.example.cyclingapp.repositories.MainRepository
import com.example.cyclingapp.roomDb.CyclingRun
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository = AppModule.cyclingRepo
) : ViewModel() {

   fun deleteRun(run: CyclingRun) {
        viewModelScope.launch {
            mainRepository.deleteCycleRun(run)
        }
    }

    fun getSortedRunsByDistance(): LiveData<List<CyclingRun>> {
        return mainRepository.getSortedRunsByDistance()
    }

    fun getSortedRunsBySpeed(): LiveData<List<CyclingRun>> {
        return mainRepository.getSortedRunsBySpeed()
    }

    fun getSortedRunsByTime(): LiveData<List<CyclingRun>> {
        return mainRepository.getSortedRunsByTime()
    }

    fun getSortedRunsByDate(): LiveData<List<CyclingRun>> {
        return mainRepository.getSortedRunsByDate()
    }

    fun getTotalCalories(name: String): LiveData<Int>{
        return mainRepository.getTotalCalories(name)
    }

    fun getTotalDistance(name: String): LiveData<Int>{
        return mainRepository.getTotalDistance(name)
    }

    fun getTotalAvg(name: String): LiveData<Float>{
        return mainRepository.getTotalAvg(name)
    }
    fun getTotalTime(name: String): LiveData<Long>{
        return mainRepository.getTotalTime(name)
    }
}
