package com.capstone.educollab1.ui.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Login User
    @POST("/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Register User
    @POST("/register")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<UserResponse>

    // Menambahkan Jadwal
    @POST("/add-schedule")
    suspend fun addSchedule(
        @Header("Authorization") token: String,
        @Body scheduleRequest: ScheduleRequest
    ): Response<ScheduleResponse>

    // Melihat Jadwal berdasarkan Username
    @GET("/view-schedule/{username}")
    suspend fun viewSchedule(
        @Path("username") username: String
    ): Response<List<ScheduleResponse>>

    // Memperbarui Jadwal berdasarkan schedule_id
    @PUT("/edit-schedule/{schedule_id}")
    suspend fun updateSchedule(
        @Path("schedule_id") scheduleId: String,
        @Header("Authorization") token: String,
        @Body scheduleRequest: ScheduleRequest
    ): Response<ScheduleResponse>

    // Menghapus Jadwal berdasarkan schedule_id
    @DELETE("del-schedule/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Int
    ): Response<Void>

    @GET("/view-schedule/{username}/{day}")
    suspend fun viewScheduleByDay(
        @Path("username") username: String,
        @Path("day") day: String
    ): Response<List<ScheduleResponse>>
}

