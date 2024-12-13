package com.capstone.educollab1.ui.remote

data class UserRequest(
    val username: String,
    val password: String,
    val email: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class ScheduleRequest(
    val scheduleId: Int? = null,
    val username: String? = null,
    val day: String,
    val subject: String,
    val period: String // Menggabungkan startTime dan endTime ke dalam satu field period
)

data class PredictionRequest(
    val Hours_Studied: Float,
    val Sleep_Hours: Float,
    val Previous_Scores: Float,
    val Sample_Question_Papers_Practiced: Float,
    val Extracurricular_Activities: String // "Yes" atau "No"
)
