package com.example.cheesechase2

import android.content.Context
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun gamePage(viewModel: GameViewModel, dimension: WindowInfo, context: Context, navController: NavController){
    val obstacle = viewModel.obstacle.value
    val jerry = viewModel.jerry.value
    val tom = viewModel.tom.value

    //Background Color
    Box(modifier = Modifier.background(color = Color.hsv(220f,0.7f,1f)))

    //Track Image
    Image(painter = painterResource(id = R.drawable.track), contentDescription = null,
        modifier = Modifier.fillMaxSize())

    //Obstacles+Traps
    Box(modifier = Modifier.fillMaxSize()) {
        obstacle.image1?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
                .offset(obstacle.xPosition1, obstacle.yPosition1)
                .size(150.dp, 150.dp))
        }
        obstacle.image2?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
                .offset(obstacle.xPosition2, obstacle.yPosition2)
                .size(150.dp, 150.dp))
        }
        obstacle.image3?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
                .offset(obstacle.xPosition3, obstacle.yPosition3)
                .size(150.dp, 150.dp))
        }
        obstacle.image4?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
                .offset(obstacle.xPosition4, obstacle.yPosition4)
                .size(150.dp, 150.dp))
        }

        Text(text = obstacle.char , modifier = Modifier.offset(obstacle.wordPositionx, obstacle.wordPositiony) , fontFamily = FontFamily.Monospace, fontSize = 35.sp,
            fontWeight = FontWeight.ExtraBold )

        viewModel.ObstacleAnimation(dimension)
        viewModel.changeSpeed()
        viewModel.HeartJerryTime()
    }

    //TOM AND JERRY PART
    val jerryTranslationy by animateDpAsState(
        targetValue = jerry.jerryPositiony,
        label = "",
        animationSpec = tween(durationMillis = 2000 , easing = LinearOutSlowInEasing)
    )
    val jerryTranslationx by animateDpAsState(
        targetValue = jerry.jerryPositionx,
        label = "",
        animationSpec = tween(durationMillis = 200 , easing = LinearOutSlowInEasing)
    )
    val tomTranslationx by animateDpAsState(
        targetValue = tom.tomPositionx,
        label = "",
        animationSpec = tween(durationMillis = 200 , easing = LinearOutSlowInEasing)
    )
    val tomTranslationy by animateDpAsState(
        targetValue = tom.tomPositiony,
        label = "",
        animationSpec = tween(durationMillis = tom.tomPositionyTiming, easing = LinearOutSlowInEasing)
    )

    if(jerry.jerryJump){viewModel.JerryJumping()}
    val jerrySize by animateDpAsState(
        targetValue = jerry.jerrySize,
        label = "",
        animationSpec = tween(durationMillis = 200 , easing = LinearOutSlowInEasing)
    )
    if(tom.tomJump){viewModel.TomJumping()}
    val tomSize by animateDpAsState(
        targetValue = tom.tomSize,
        label = "",
        animationSpec = tween(durationMillis = 200 , easing = LinearOutSlowInEasing)
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) { detectTapGestures { viewModel.trackChange(it.x, dimension) } }) {
        viewModel.state.value.bitmapJerry?.let {
            Image(
                bitmap = it.asImageBitmap(), contentDescription = null,
                modifier = Modifier
                    .size(jerrySize, jerrySize)
                    .offset(jerryTranslationx, jerryTranslationy)
                    .clickable { viewModel.jumpJerry() }
            )
        }
        viewModel.state.value.bitmapTom?.let{
            Image(bitmap = it.asImageBitmap() , contentDescription = null ,
                modifier = Modifier
                    .size(tomSize, tomSize)
                    .offset(tomTranslationx, tomTranslationy))
        }

        viewModel.openingAnimation()
        viewModel.obstacleCrossed(context)
        viewModel.closeToJerry()
        viewModel.jumpTomAuto()
        viewModel.jumpJerryAuto()
        viewModel.reduceautoJumpTime()
    }

    //SCORECARD PART
    Box(modifier= Modifier.fillMaxSize()) {
        Score(viewModel.statee.value.highscore, dimension, viewModel.statee.value.theme)
        CheeseScore(viewModel.statee.value.cheeseScore, viewModel.statee.value.theme)
        HeartTime(viewModel.powerup.value.heartTime, dimension, viewModel.statee.value.theme)
    }
    
    viewModel.randomWordCall(context = context)
    viewModel.gameOverFunction()
    if(viewModel.statee.value.gameOver){gameover({viewModel.useCheese()},{viewModel.playAgain()},{navController.navigate(Screen.frontPage.route)})}
}