package com.example.cyclingapp.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CyclingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCyclingRun(cyclingRun: CyclingRun)

    @Delete
    suspend fun deleteCyclingRun(cyclingRun: CyclingRun)

    @Query("SELECT * FROM cycling_runs order by timestamp desc")
    fun getSortedRunsByDate(): LiveData<List<CyclingRun>>

    @Query("SELECT * FROM cycling_runs order by distance desc")
    fun getSortedRunsByDistance(): LiveData<List<CyclingRun>>

    @Query("SELECT * FROM cycling_runs order by avgSpeedInKmh desc")
    fun getSortedRunsBySpeed(): LiveData<List<CyclingRun>>

    @Query("SELECT * FROM cycling_runs order by timeinmillis desc")
    fun getSortedRunsByTime(): LiveData<List<CyclingRun>>

    @Query("SELECT sum(caloriesBurned) FROM cycling_runs where name = :username")
    fun getTotalCalories(username: String): LiveData<Int>

    @Query("SELECT sum(distance) FROM cycling_runs where name = :username")
    fun getTotalDistance(username: String): LiveData<Int>

    @Query("SELECT sum(timeinmillis) FROM cycling_runs where name = :username")
    fun getTotalTime(username: String): LiveData<Long>

    @Query("SELECT avg(avgSpeedInKmh) FROM cycling_runs where name = :username")
    fun getTotalAvg(username: String): LiveData<Float>
}