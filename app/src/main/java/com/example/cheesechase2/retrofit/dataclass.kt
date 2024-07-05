package com.example.cheesechase2.retrofit

import android.graphics.Bitmap
import com.example.cheesechase2.hitHindrance

data class dataclass(
    var datafetched : Boolean = false,
    var text: String = "Fetching data from API",

    var obstacleLimit: Int? = 2,
    var obstacleCourse: List<String>? = listOf("M"),  //Change this to 20
    var hitHindrance: hitHindrance? = hitHindrance(4,"Auto jump upcoming obstacles for given seconds", 2),
    var randomWord: String? = "rishi",
    var theme: String? = "",

    var bitmapJerry: Bitmap? = null,
    var bitmapTom: Bitmap? =null,
    var bitmapObstacle: Bitmap? =null
)
