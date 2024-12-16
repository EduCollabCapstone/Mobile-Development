package com.capstone.educollab1.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: String,     // Menyimpan hari
    val subject: String, // Menyimpan subjek
    val time: String     // Menyimpan waktu
)
