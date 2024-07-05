package com.example.cheesechase2.retrofit

import com.example.cheesechase2.hitHindrance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface jerryAPI {
    @GET("/obstacleLimit")
    suspend fun getobstacleLimit(): Response<obstacleLimit>

    @GET("/image")
    fun getImage(@Query("character") character: String): Call<ResponseBody>
    //If suspend function used, image not recieved

    @GET("/hitHindrance")
    suspend fun hitHinderance(): Response<hitHindrance>

    @POST("/obstacleCourse")
    suspend fun obstacleCourse(@Body extent: obstacleCourseExtent): Response<obstacleCourse>

    @POST("/randomWord")
    suspend fun randomWord(@Body length: randomWordLength): Response<randomWord>

    @POST("/theme")
    suspend fun theme(@Body request: themeRequest): Response<theme>
}