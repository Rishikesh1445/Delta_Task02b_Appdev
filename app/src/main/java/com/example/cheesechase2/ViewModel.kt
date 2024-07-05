package com.example.cheesechase2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheesechase2.retrofit.RetrofitInstance
import com.example.cheesechase2.retrofit.dataclass
import com.example.cheesechase2.retrofit.obstacleCourseExtent
import com.example.cheesechase2.retrofit.randomWordLength
import com.example.cheesechase2.retrofit.themeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.stream.IntStream.range
import kotlin.random.Random

class GameViewModel: ViewModel(){
    private val _state = mutableStateOf(dataclass())
    val state: State<dataclass> = _state

    private val _obstacle = mutableStateOf(Obstacle())
    val obstacle: State<Obstacle> = _obstacle
    private var xPosition = mutableListOf<Dp>()

    private val _statee = mutableStateOf(GameStates())
    val statee: State<GameStates> = _statee

    private val _jerry = mutableStateOf(Jerry())
    val jerry: State<Jerry> = _jerry

    private val _tom = mutableStateOf(Tom())
    val tom: State<Tom> = _tom

    private val _powerup = mutableStateOf(Powerup())
    val powerup: State<Powerup> = _powerup

    init{fetchData()}
    private fun fetchData(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val limit_fromAPI = RetrofitInstance.api.getobstacleLimit().body()?.obstacleLimit

                val hitHindrance_fromAPI = RetrofitInstance.api.hitHinderance().body()

                Log.d("HINDRANCE" , hitHindrance_fromAPI.toString())

                val randomWordLength = randomWordLength(5)
                val randomWord_fromAPI = RetrofitInstance.api.randomWord(randomWordLength).body()?.word

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val themeRequest = themeRequest(dateFormat.format(Date()), timeFormat.format(Date()))
                val theme_fromAPI = RetrofitInstance.api.theme(themeRequest).body()?.theme

                Log.d("THEMEE" , theme_fromAPI!!)
                _state.value = _state.value.copy(obstacleLimit=limit_fromAPI,
                    hitHindrance = hitHindrance_fromAPI , randomWord = randomWord_fromAPI, theme = theme_fromAPI)

                if(state.value.theme=="day"){
                    _statee.value = _statee.value.copy(theme = false)
                }

                RetrofitInstance.api.getImage("jerry").enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val inputStream = response.body()!!.byteStream()
                                val bitmap_fromAPI = BitmapFactory.decodeStream(inputStream)

                                _state.value = _state.value.copy(bitmapJerry = bitmap_fromAPI)
                            } else {
                                Log.e("Error", "Failed to download jerry image")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Error", "Network call failed", t)
                        }
                    }
                )
                RetrofitInstance.api.getImage("tom").enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val inputStream = response.body()!!.byteStream()
                                val bitmap_fromAPI = BitmapFactory.decodeStream(inputStream)

                                _state.value = _state.value.copy(bitmapTom = bitmap_fromAPI)
                            } else {
                                Log.e("Error", "Failed to download tom image")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Error", "Network call failed", t)
                        }
                    }
                )
                RetrofitInstance.api.getImage("obstacle").enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val inputStream = response.body()!!.byteStream()
                                val bitmap_fromAPI = BitmapFactory.decodeStream(inputStream)

                                _state.value = _state.value.copy(bitmapObstacle = bitmap_fromAPI)
                            } else {
                                Log.e("Error", "Failed to download obstacle image")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Error", "Network call failed", t)
                        }
                    }
                )

                var i = 0
                repeat(20){xPosition.add(-it.dp)}
                val extent = obstacleCourseExtent(20)
                val obstacleCourse_fromAPI = RetrofitInstance.api.obstacleCourse(extent).body()?.obstacleCourse
                _state.value = _state.value.copy(obstacleCourse = obstacleCourse_fromAPI)

                for(path in state.value.obstacleCourse!!) {
                    when (path) {
                        "L" -> { xPosition[i] = 5.dp; i += 1}
                        "M" -> { xPosition[i] = 120.dp; i += 1 }
                        "R" -> { xPosition[i] = 240.dp; i += 1 }
                        "B" -> { xPosition[i] = (-200).dp; i += 1 }
                    }
                }

                Log.d("Track" , "First")
                Log.d("Track" , obstacleCourse_fromAPI.toString())
                _state.value = _state.value.copy(text = "Fetched successfully")
                delay(1500)
                _state.value = _state.value.copy(datafetched = true)
            }catch (e:Exception){
                _state.value = _state.value.copy(text = "Internet Connection Failed. \nLoading Game with default values")
                delay(4000)
                _state.value = _state.value.copy(datafetched = true)
                e.message?.let { Log.d("error" , it) }
            }
        }
    }

    fun gameOverFunction(){
        if(jerry.value.jerryTouched==state.value.obstacleLimit || tom.value.tomPositiony < 591.dp){
            _statee.value = _statee.value.copy(gamePause = true, gameOver = true)
        }
    }
    fun useCheese(){
        if(statee.value.cheeseScore>0) {
            _statee.value = _statee.value.copy(gamePause = false, gameOver = false,cheeseScore = statee.value.cheeseScore-1)
            _jerry.value = _jerry.value.copy(jerryTouched = 0)
            _tom.value = _tom.value.copy(tomPositiony = 1000.dp)
        }
    }
    fun playAgain(){
        _jerry.value = _jerry.value.copy(jerryTouched = 0)
        _tom.value = _tom.value.copy(tomPositiony = 1000.dp)
        viewModelScope.launch {
            val limit_fromAPI = RetrofitInstance.api.getobstacleLimit().body()?.obstacleLimit
            _state.value = _state.value.copy(obstacleLimit = limit_fromAPI)
            Log.d("OBSTACLEN" , " new ${state.value.obstacleLimit} , ${limit_fromAPI}")
        }
        index = 0
        indexx = 0
        randomWordBoolean= false
        _obstacle.value = Obstacle()
        _statee.value = _statee.value.copy(gamePause = false, gameOver = false , highscore = 0, cheeseScore = 0)
        _powerup.value = _powerup.value.copy( heartTime = 0)
        changeTrack(GameStates.Track.Middle)
    }

    @Composable
    fun getBitmapFromResource(context: Context, trap: Int, cheese:Int, heart:Int) {
        LaunchedEffect(Unit) {
            val trap = BitmapFactory.decodeResource(context.resources, trap)
            val cheese = BitmapFactory.decodeResource(context.resources, cheese)
            val heart = BitmapFactory.decodeResource(context.resources, heart)
            val bitmaps = listOf(trap, cheese, heart)
            _statee.value = _statee.value.copy(bitmaps = bitmaps)
        }
    }

    //TOM AND JERRY ENRTY AT BOTTOM
    fun openingAnimation() {
        _jerry.value = _jerry.value.copy(jerryPositiony = 550.dp,)
        _tom.value = _tom.value.copy(tomPositiony = 700.dp)
        if (jerry.value.jerryTouched == 0 && statee.value.highscore > 0) {
            _tom.value = _tom.value.copy(tomPositionyTiming = 2000, tomPositiony = 1000.dp)
        }
    }

    //TOM CLOSING ON JERRY
    @Composable
    fun closeToJerry() {
        if (jerry.value.jerryTouched > 0) {
            LaunchedEffect(Unit) {
            _tom.value = _tom.value.copy(
                tomPositionyTiming = 200,
                tomPositiony = 700.dp
            )
        }
        }
        if (jerry.value.jerryTouched >= state.value.obstacleLimit!!) {
            _tom.value = _tom.value.copy(
                tomPositionyTiming = 200,
                tomPositiony = 590.dp
            )
        }

    }

    //OBSTACLE ANIMATION
    private var xPosition2 = mutableListOf<Dp>()
    @Composable
    private fun FetchObstacleCourse(){
        LaunchedEffect(Unit){
            var i = 0
            repeat(20){xPosition2.add(-it.dp)}
            val extent = obstacleCourseExtent(20)
            val obstacleCourse_fromAPI = RetrofitInstance.api.obstacleCourse(extent).body()?.obstacleCourse
            _state.value = _state.value.copy(obstacleCourse = obstacleCourse_fromAPI)

            for(path in state.value.obstacleCourse!!){
                when (path) {
                    "L" -> { xPosition2[i] = 5.dp; i += 1}
                    "M" -> { xPosition2[i] = 120.dp; i += 1 }
                    "R" -> { xPosition2[i] = 240.dp; i += 1 }
                    "B" -> { xPosition2[i] = (-200).dp; i += 1 }
                }
            }
            Log.d("Track" , obstacleCourse_fromAPI.toString())
        }
    }
    private var index = 0
    @Composable
    fun ObstacleAnimation(dimension:WindowInfo){
        if(!statee.value.gamePause) {
            if (obstacle.value.yPosition1 < dimension.screenHeightinDp && index < 21) {
                _obstacle.value =
                    _obstacle.value.copy(yPosition1 = obstacle.value.yPosition1 + obstacle.value.speed)
            } else {
                activation[0] = -1
                _obstacle.value =
                    _obstacle.value.copy(yPosition1 = (-150).dp, xPosition1 = xPosition[index])
                index += 1
                if (obstacle.value.xPosition1 < 0.dp) {
                    val (bitmap, xPosition) = notObstacle(0)
                    _obstacle.value = _obstacle.value.copy(image1 = bitmap , xPosition1 = xPosition)
                } else {
                    _obstacle.value = _obstacle.value.copy(image1 = state.value.bitmapObstacle)
                }
                if(randomWordBoolean){
                    val randomIndexx = Random.nextInt(wordArray.size)
                    _obstacle.value = _obstacle.value.copy(char = wordArray[randomIndexx])
                }
            }

            if (obstacle.value.yPosition2 < dimension.screenHeightinDp && index < 21) {
                _obstacle.value =
                    _obstacle.value.copy(yPosition2 = obstacle.value.yPosition2 + obstacle.value.speed)
            } else {
                activation[1] = -1
                _obstacle.value = _obstacle.value.copy(
                    yPosition2 = obstacle.value.yPosition1 - 240.dp,
                    xPosition2 = xPosition[index]
                )
                index += 1
                if (obstacle.value.xPosition2 < 0.dp) {
                    val (bitmap, xPosition) = notObstacle(1)
                    _obstacle.value = _obstacle.value.copy(image2 = bitmap , xPosition2 = xPosition)
                } else {
                    _obstacle.value = _obstacle.value.copy(image2 = state.value.bitmapObstacle)
                }
            }

            if (obstacle.value.yPosition3 < dimension.screenHeightinDp && index < 21) {
                _obstacle.value =
                    _obstacle.value.copy(yPosition3 = obstacle.value.yPosition3 + obstacle.value.speed)
            } else {
                activation[2] = -1
                _obstacle.value = _obstacle.value.copy(
                    yPosition3 = obstacle.value.yPosition2 - 240.dp,
                    xPosition3 = xPosition[index]
                )
                index += 1
                if (obstacle.value.xPosition3 < 0.dp) {
                    val (bitmap, xPosition) = notObstacle(2)
                    _obstacle.value = _obstacle.value.copy(image3 = bitmap , xPosition3 = xPosition)
                } else {
                    _obstacle.value = _obstacle.value.copy(image3 = state.value.bitmapObstacle)
                }
            }

            if (obstacle.value.yPosition4 < dimension.screenHeightinDp && index < 21) {
                _obstacle.value =
                    _obstacle.value.copy(yPosition4 = obstacle.value.yPosition4 + obstacle.value.speed)
            } else {
                activation[3] = -1
                _obstacle.value = _obstacle.value.copy(
                    yPosition4 = obstacle.value.yPosition3 - 240.dp,
                    xPosition4 = xPosition[index]
                )
                index += 1
                if (obstacle.value.xPosition4 < 0.dp) {
                    val (bitmap, xPosition) = notObstacle(3)
                    _obstacle.value = _obstacle.value.copy(image4 = bitmap , xPosition4 = xPosition)
                } else {
                    _obstacle.value = _obstacle.value.copy(image4 = state.value.bitmapObstacle)
                }
            }

            if(randomWordBoolean){
                _obstacle.value = _obstacle.value.copy(wordPositiony = obstacle.value.yPosition1 - 100.dp, wordPositionx = obstacle.value.xPosition1 + 65.dp)
            }

            if (index == 11) {
                FetchObstacleCourse()
            }
            if (index == 20) {
                xPosition = xPosition2.toMutableList()
                Log.d("Track", "Changed")
                index = 0
            }
        }
    }

    //SPEED CHANGE
    @Composable
    fun changeSpeed(){
        if(statee.value.highscore%2==0 && obstacle.value.speed<2.9.dp){
            LaunchedEffect(Unit){
                _obstacle.value = _obstacle.value.copy(speed = obstacle.value.speed + 0.4.dp)
            }
        }
    }

    //TRACK CHANGE ON TAP
    fun trackChange(coordinate: Float , dimension:WindowInfo){
        if (statee.value.touchMode) {
            when (coordinate) {
                in (dimension.screenWidth / 2 - dimension.screenWidth / 10)..dimension.screenWidth / 2 + dimension.screenWidth / 10 -> {
                    changeTrack(GameStates.Track.Middle)
                }

                in (dimension.screenWidth / 4 - dimension.screenWidth / 10 - 50F)..dimension.screenWidth / 4 + dimension.screenWidth / 10 - 50F -> {
                    changeTrack(GameStates.Track.Left)
                }

                in (3 * dimension.screenWidth / 4 - dimension.screenWidth / 10 + 50F)..3 * dimension.screenWidth / 4 + dimension.screenWidth / 10 + 50F -> {
                    changeTrack(GameStates.Track.Right)
                }
            }
        }    }
    private fun changeTrack(changeTrack: GameStates.Track) {
        if (jerry.value.jerryTrack != changeTrack) {
            when (changeTrack) {
                GameStates.Track.Middle -> { _jerry.value = _jerry.value.copy(jerryPositionx = 100.dp, jerryTrack = changeTrack)
                    _tom.value = _tom.value.copy(tomPositionx = 100.dp)
                }

                GameStates.Track.Left -> { _jerry.value =_jerry.value.copy(jerryPositionx = (-5).dp, jerryTrack = changeTrack)
                    _tom.value = _tom.value.copy(
                        tomPositionx = (-5).dp
                    )
                }

                GameStates.Track.Right -> { _jerry.value = _jerry.value.copy(jerryPositionx = 220.dp, jerryTrack = changeTrack)
                    _tom.value = _tom.value.copy(tomPositionx = 220.dp)
                }
            }
        }
    }

    //OBSTACLE CROSSING JERRY
    private var track1 : GameStates.Track = GameStates.Track.Middle
    private var track2 : GameStates.Track = GameStates.Track.Middle
    private var track3 : GameStates.Track = GameStates.Track.Middle
    private var track4 : GameStates.Track = GameStates.Track.Middle
    @Composable
    fun obstacleCrossed(context: Context){
        if(obstacle.value.yPosition1 in (470.dp .. 500.dp)){
            when(obstacle.value.xPosition1){
                5.dp -> { track1 = GameStates.Track.Left}
                120.dp -> {track1 = GameStates.Track.Middle}
                240.dp -> {track1 = GameStates.Track.Right}
            }
            if(jerry.value.jerryTrack == track1 && !jerry.value.jerryJump ){
                if(activation[0]<0){
                    if(!powerup.value.heart &&!powerup.value.autoJump) {
                        LaunchedEffect(Unit) {
                            vibration(context)
                            _jerry.value =
                                _jerry.value.copy(jerryTouched = jerry.value.jerryTouched + 1)
                        }
                    }
                }else{
                    powerups(activation[0], context)
                    _obstacle.value = _obstacle.value.copy(image1 = null)
                }
            }else if(activation[0]<0){
                LaunchedEffect(Unit){_statee.value = _statee.value.copy(highscore = statee.value.highscore + 1)}
            }
        }

        if(obstacle.value.yPosition2 in (470.dp .. 500.dp)){
            when(obstacle.value.xPosition2){
                5.dp -> { track2 = GameStates.Track.Left}
                120.dp -> {track2 = GameStates.Track.Middle}
                240.dp -> {track2 = GameStates.Track.Right}
            }
            if(jerry.value.jerryTrack == track2 && !jerry.value.jerryJump){
                if(activation[1]<0) {
                    if(!powerup.value.heart &&!powerup.value.autoJump) {
                        LaunchedEffect(Unit) {
                            vibration(context)
                            _jerry.value =
                                _jerry.value.copy(jerryTouched = jerry.value.jerryTouched + 1)
                        }
                    }
                }else{
                    powerups(activation[1],context)
                    _obstacle.value = _obstacle.value.copy(image2 = null)
                }
            }else if(activation[1]<0){
                LaunchedEffect(Unit){_statee.value = _statee.value.copy(highscore = statee.value.highscore + 1)}
            }
        }
        if(obstacle.value.yPosition3 in (470.dp .. 500.dp)){
            when(obstacle.value.xPosition3){
                5.dp -> { track3 = GameStates.Track.Left}
                120.dp -> {track3 = GameStates.Track.Middle}
                240.dp -> {track3 = GameStates.Track.Right}
            }
            if(jerry.value.jerryTrack == track3 && !jerry.value.jerryJump ){
                if(activation[2]<0) {
                    if(!powerup.value.heart &&!powerup.value.autoJump) {
                        LaunchedEffect(Unit) {
                            vibration(context)
                            _jerry.value =
                                _jerry.value.copy(jerryTouched = jerry.value.jerryTouched + 1)
                        }
                    }
                }else{
                    powerups(activation[2], context)
                    _obstacle.value = _obstacle.value.copy(image3 = null)
                }
            }else if(activation[2]<0){
                LaunchedEffect(Unit){_statee.value = _statee.value.copy(highscore = statee.value.highscore + 1)}
            }
        }
        if(obstacle.value.yPosition4 in (470.dp .. 500.dp) ){
            when(obstacle.value.xPosition4){
                5.dp -> { track4 = GameStates.Track.Left}
                120.dp -> {track4 = GameStates.Track.Middle}
                240.dp -> {track4 = GameStates.Track.Right}
            }
            if(jerry.value.jerryTrack == track4 && !jerry.value.jerryJump){
                if(activation[3] < 0) {
                    if(!powerup.value.heart &&!powerup.value.autoJump) {
                        LaunchedEffect(Unit) {
                            vibration(context)
                            _jerry.value =
                                _jerry.value.copy(jerryTouched = jerry.value.jerryTouched + 1)
                        }
                    }
                }else{
                    powerups(activation[3], context)
                    _obstacle.value = _obstacle.value.copy(image4 = null)
                }
            }else if(activation[3]<0){
                LaunchedEffect(Unit){_statee.value = _statee.value.copy(highscore = statee.value.highscore + 1)}
            }
        }

        if(obstacle.value.wordPositiony in (485.dp..500.dp) && track1==jerry.value.jerryTrack && !jerry.value.jerryJump ){
            if(obstacle.value.char == wordArray[indexx]){
                indexx += 1
                obstacle.value.char = ""
            }
            if(indexx == 5){
                randomWordBoolean= false
                indexx = 0
            }
        }
    }

    //JUMP PART
    fun jumpJerry() { _jerry.value = _jerry.value.copy(jerryJump = true) }
    @Composable
    fun JerryJumping() {
        LaunchedEffect(Unit) {
            _jerry.value = _jerry.value.copy(
                jerrySize = 240.dp,
                jerryPositionx = jerry.value.jerryPositionx - 30.dp
            )
            delay(800)
            _jerry.value = _jerry.value.copy(
                jerrySize = 180.dp,
                jerryPositionx = jerry.value.jerryPositionx + 30.dp,
                jerryJump = false
            )
        }
    }
    @Composable
    fun TomJumping() {
        LaunchedEffect(Unit) {
            _tom.value = _tom.value.copy(tomSize = 240.dp, tomPositionx = tom.value.tomPositionx - 30.dp)
            delay(800)
            _tom.value = _tom.value.copy(
                tomSize = 180.dp,
                tomPositionx = tom.value.tomPositionx + 30.dp,
                tomJump = false
            )
        }
    }
    fun jumpTomAuto(){
        if(obstacle.value.yPosition1 in (625.dp .. 635.dp) && track1 == jerry.value.jerryTrack){
            _tom.value = _tom.value.copy(tomJump = true)
        }
        if(obstacle.value.yPosition2 in (625.dp .. 635.dp) && track2 == jerry.value.jerryTrack){
            _tom.value = _tom.value.copy(tomJump = true)
        }
        if(obstacle.value.yPosition3 in (625.dp .. 635.dp) && track3 == jerry.value.jerryTrack){
            _tom.value = _tom.value.copy(tomJump = true)
        }
        if(obstacle.value.yPosition4 in (625.dp .. 635.dp) && track4 == jerry.value.jerryTrack){
            _tom.value = _tom.value.copy(tomJump = true)
        }
    }

    @Composable
    fun jumpJerryAuto(){
            if (powerup.value.autoJump) {
                    if (obstacle.value.yPosition1 in (475.dp..500.dp) && track1 == jerry.value.jerryTrack && activation[0]<0) {
                        _jerry.value = _jerry.value.copy(jerryJump = true)
                    }
                    if (obstacle.value.yPosition2 in (475.dp..500.dp) && track2 == jerry.value.jerryTrack && activation[1]<0) {
                        _jerry.value = _jerry.value.copy(jerryJump = true)
                    }
                    if (obstacle.value.yPosition3 in (475.dp..500.dp) && track3 == jerry.value.jerryTrack && activation[2]<0) {
                        _jerry.value = _jerry.value.copy(jerryJump = true)
                    }
                    if (obstacle.value.yPosition4 in (475.dp..500.dp) && track4 == jerry.value.jerryTrack && activation[3]<0) {
                        _jerry.value = _jerry.value.copy(jerryJump = true)
                    }
            }
    }

    @Composable
    fun reduceautoJumpTime(){
        if(powerup.value.autoJump) {
            LaunchedEffect(Unit) {
                while (powerup.value.autoJumpTime > 0 && !statee.value.gamePause) {
                    _powerup.value =
                        _powerup.value.copy(autoJumpTime = powerup.value.autoJumpTime - 1)
                    delay(1000)
                    Log.d("HIND", powerup.value.autoJumpTime.toString())
                }
            }
            if (powerup.value.autoJumpTime == 0) {
                _powerup.value = _powerup.value.copy(autoJump = false)
            }
        }
    }

    private val position = listOf(5.dp, 120.dp, 240.dp)
    private var activation = arrayOf(-1, -1, -1, -1)
    private fun notObstacle(where : Int) : Pair<Bitmap?,Dp>{
        val randomIndexBitmap = Random.nextInt(statee.value.bitmaps.size)
        val randomIndexPosition = Random.nextInt(position.size)
        activation[where] = randomIndexBitmap
        return Pair(statee.value.bitmaps[randomIndexBitmap], position[randomIndexPosition])
    }

    @Composable
    private fun powerups(what:Int, context: Context){
        when (what) {
            2 -> { LaunchedEffect(Unit){_powerup.value = _powerup.value.copy(heart = true , heartTime = 10); powerupSound(context)} }
            1 -> { LaunchedEffect(Unit){_statee.value = _statee.value.copy(cheeseScore = statee.value.cheeseScore+1); powerupSound(context)}}
            0 -> { LaunchedEffect(Unit){codeExecuted=false; hitHindrance(); powerupSound(context)}}
        }
    }

    @Composable
    fun HeartJerryTime(){
        if(powerup.value.heart) {
            LaunchedEffect(Unit) {
                while (powerup.value.heartTime > 0) {
                    if(!statee.value.gamePause) {
                        delay(1000)
                        _powerup.value = _powerup.value.copy(heartTime = powerup.value.heartTime - 1)
                    }
                }
            }
            if (powerup.value.heartTime == 0) {
                _powerup.value = _powerup.value.copy(heart = false)
            }
        }
    }

    private var hindrancejob : Job? = null
    private var codeExecuted : Boolean = false
    private fun hitHindrance(){
        if(!codeExecuted) {
            hindrancejob = viewModelScope.launch {
                when (state.value.hitHindrance?.type) {
                    1 -> {
                        _obstacle.value =
                            _obstacle.value.copy(speed = obstacle.value.speed + state.value.hitHindrance!!.amount.dp)
                    }

                    2 -> {
                        _powerup.value = _powerup.value.copy(
                            autoJumpTime = state.value.hitHindrance!!.amount,
                            autoJump = true
                        )
                    }

                    3 -> {
                        _tom.value =
                            _tom.value.copy(tomPositiony = tom.value.tomPositiony - 40*state.value.hitHindrance!!.amount.dp)
                    }
                }
                val hitHindrance_fromAPI = RetrofitInstance.api.hitHinderance().body()
                _state.value = _state.value.copy(hitHindrance = hitHindrance_fromAPI)
                Log.d("HINDRANCEN", state.value.hitHindrance.toString())
                codeExecuted = true
            }
            if (codeExecuted) {
                hindrancejob!!.cancel()
                hindrancejob = null
                Log.d("HIND" , "cancelled")
            }
        }
    }

    private var indexx = 0
    private var wordArray = arrayOf("" , "" , "" , "", "")
    private var randomWordjob : Job? = null
    private var randomWordBoolean = false
    @Composable
    fun randomWordCall(context: Context){
        if(statee.value.highscore%5==0 && statee.value.highscore>4 &&!randomWordBoolean){
            LaunchedEffect(Unit){
                randomWordBoolean = true
                Toast.makeText(context, "Collect Word ${state.value.randomWord} in order" , Toast.LENGTH_SHORT ).show()
                for(word in state.value.randomWord!!){
                    wordArray[indexx] = word.uppercase()
                    indexx += 1
                }
                indexx = 0
                fetchRandomWord()
            }
        }
    }

    fun fetchRandomWord(){
        randomWordjob?.cancel()
        randomWordjob = viewModelScope.launch {
            val randomWordLength = randomWordLength(5)
            val randomWord_fromAPI = RetrofitInstance.api.randomWord(randomWordLength).body()?.word
            _state.value = _state.value.copy(randomWord = randomWord_fromAPI)
            Log.d("Random" , "New word ${state.value.randomWord}")
        }
    }

    private fun vibration(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect: VibrationEffect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrationEffect =
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
            vibrator.cancel()
            vibrator.vibrate(vibrationEffect)
        }
    }
    fun bg(context: Context){
        val mMediaPlayer = MediaPlayer.create(context, R.raw.background)
        mMediaPlayer.start()
    }

    private fun powerupSound(context: Context){
        val mMediaPlayer = MediaPlayer.create(context, R.raw.powerup)
        mMediaPlayer.start()
    }
}


//heart and cheese comeing for B
//memory exhaustion over a time due to courotines
//increase to 50, course length
//word appearing already covered