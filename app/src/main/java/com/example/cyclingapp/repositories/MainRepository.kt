package com.example.cyclingapp.repositories

import androidx.lifecycle.LiveData
import com.example.cyclingapp.roomDb.CyclingDAO
import com.example.cyclingapp.roomDb.CyclingRun

class MainRepository(
    val cycleDao: CyclingDAO
) {
    suspend fun insertCycleRun(cyclingRun: CyclingRun) = cycleDao.insertCyclingRun(cyclingRun)
    suspend fun deleteCycleRun(cyclingRun: CyclingRun) = cycleDao.deleteCyclingRun(cyclingRun)
    fun getSortedRunsByDate(): LiveData<List<CyclingRun>> = cycleDao.getSortedRunsByDate()
    fun getSortedRunsByDistance(): LiveData<List<CyclingRun>> = cycleDao.getSortedRunsByDistance()
    fun getSortedRunsBySpeed(): LiveData<List<CyclingRun>> = cycleDao.getSortedRunsBySpeed()
    fun getSortedRunsByTime(): LiveData<List<CyclingRun>> = cycleDao.getSortedRunsByTime()
    fun getTotalCalories(name: String): LiveData<Int> = cycleDao.getTotalCalories(name)
    fun getTotalDistance(name: String): LiveData<Int> = cycleDao.getTotalDistance(name)
    fun getTotalAvg(name: String): LiveData<Float> = cycleDao.getTotalAvg(name)
    fun getTotalTime(name: String): LiveData<Long> = cycleDao.getTotalTime(name)
}
