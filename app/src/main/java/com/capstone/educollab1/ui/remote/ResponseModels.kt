package com.capstone.educollab1.ui.remote


data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val message: String,
    val token: String,
)

data class LoginResponse(
    val message: String,
    val token: String,
)

data class ScheduleResponse(
    val scheduleId: Int? = null,
    val username: String? = null,
    val day: String,
    val subject: String,
    val period: String
)

data class PredictionResponse(
    val prediction: String
)

data class SchedulesResponse(
    val schedules: List<ScheduleResponse>?
)

data class SentimentResponse(
    val predictions: List<String>
)

data class ResponseAbsences(
    val responseAbsences: List<ResponseAbsencesItem?>? = null
)

data class ResponseAbsencesItem(
    val date: String? = null,
    val studentName: String? = null,
    val jsonMemberClass: String? = null,
    val status: String? = null
)



