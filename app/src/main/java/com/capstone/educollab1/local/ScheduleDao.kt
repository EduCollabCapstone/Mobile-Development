package com.capstone.educollab1.local

import androidx.room.*

@Dao
interface ScheduleDao {

    @Insert
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM schedules WHERE id = :scheduleId")
    suspend fun getScheduleById(scheduleId: Int): ScheduleEntity?

    @Query("DELETE FROM schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Int)

    @Query("SELECT * FROM schedules WHERE day = :day ORDER BY time ASC")
    suspend fun getSchedulesByDay(day: String): List<ScheduleEntity> // Filter by day
}
