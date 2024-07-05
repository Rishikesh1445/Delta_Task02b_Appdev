package com.example.cheesechase2

import android.graphics.Bitmap
import android.media.Image
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow

data class GameStates(
    var highscore:Int =0,
    var cheeseScore:Int = 0,
    var gamePause:Boolean = false,
    var gameOver:Boolean = false,
    var touchMode : Boolean = true,
    var gyroMode : Boolean = false,
    var bitmaps : List<Bitmap?> = listOf(null, null, null),
    val theme: Boolean = true //night is true
) {
    enum class Track {
        Left, Middle, Right
    }
}

data class Obstacle(
    var speed: Dp = 0.4.dp,
    var yPosition1 : Dp = 1500.dp,
    var yPosition2 : Dp = 1500.dp,
    var yPosition3 : Dp = 1500.dp,
    var yPosition4 : Dp = 1500.dp,
    var wordPositiony : Dp = 1500.dp,

    var xPosition1 : Dp = 1500.dp,
    var xPosition2 : Dp = 1500.dp,
    var xPosition3 : Dp = 1500.dp,
    var xPosition4 : Dp = 1500.dp,
    var wordPositionx : Dp = 1500.dp,

    var image1: Bitmap? = null,
    var image2: Bitmap? = null,
    var image3: Bitmap? = null,
    var image4: Bitmap? = null,
    var char: String = ""
)

data class Jerry(
    var jerryJump:Boolean = false,
    var jerryTouched:Int =0,
    var jerryPositionx: Dp = 100.dp,
    var jerryPositiony: Dp = 950.dp,
    var jerrySize:Dp=180.dp,
    var jerryTrack : GameStates.Track = GameStates.Track.Middle
)

data class Tom(
    var tomJump:Boolean = false,
    var tomSize:Dp=180.dp,
    var tomPositionx: Dp = 100.dp,
    var tomPositiony: Dp = 1100.dp,
    var tomPositionyTiming:Int = 2000,
)

data class Powerup(
    var heart:Boolean = false,
    var heartTime: Int = 0,
    var autoJump: Boolean = false,
    var autoJumpTime : Int = 0,
)

data class GyroscopeData(
    val x: Float,
    val y: Float,
    val z: Float
)

interface Gyroscope {
    fun getGyroscopeData(): Flow<GyroscopeData>
}
