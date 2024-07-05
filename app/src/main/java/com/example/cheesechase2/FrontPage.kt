package com.example.cheesechase2

import android.content.Context
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FrontPage(viewModel: GameViewModel, context: Context, navController: NavController, dimension: WindowInfo){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.hsv(359f, 0.84f, 0.49f)))
    val circleOneRadius = remember { Animatable(0f) }
    val circleTwoRadius = remember { Animatable(0f) }
    val circleThreeRadius = remember { Animatable(0f) }
    val circleThreeColor = remember { Animatable(Color.hsv(22f, 0.7f, 1f)) }
    val imageSize = remember { Animatable(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    var boolText by remember { mutableStateOf(false) }
    var boolClicked by remember { mutableStateOf(false) }
    if(!viewModel.state.value.datafetched){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = viewModel.state.value.text,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
            )
        }
    }else{
        LaunchedEffect(Unit){
            viewModel.bg(context)
            launch {
                circleOneRadius.animateTo(
                    dimension.screenWidth / 2 + 150f,
                    tween(600, easing = EaseInOutBounce)
                )
            }
            launch {
                circleTwoRadius.animateTo(
                    dimension.screenWidth / 2 - 50f,
                    tween(600, easing = EaseInOutBounce)
                )
            }
            launch {
                circleThreeRadius.animateTo(
                    dimension.screenWidth / 2 - 200f,
                    tween(600, easing = EaseInOutBounce)
                );
                imageSize.animateTo(300f, tween(600, easing = EaseInOutBounce)); boolText = true
            }
        }
        if (boolClicked) {
            LaunchedEffect(Unit) {
                launch {
                    imageSize.animateTo(0f, tween(0, easing = EaseInElastic));
                    circleOneRadius.animateTo(
                        dimension.screenWidth / 2 + 350f,
                        tween(600, easing = EaseInElastic)
                    )
                }
                launch {
                    circleTwoRadius.animateTo(
                        dimension.screenWidth / 2 + 150f,
                        tween(600, easing = EaseInElastic)
                    );
                    circleThreeColor.animateTo(Color.hsv(185f, 0.81f, 1f))
                }
                launch {
                    boolText = false;circleThreeRadius.animateTo(
                    dimension.screenWidth / 2 + 800f,
                    tween(600, easing = EaseInElastic)
                );
                    delay(1000);navController.navigate(Screen.gamePage.route)
                }
            }
        }

        Canvas(modifier = Modifier
            .fillMaxSize()
            .clickable { boolClicked = true }) {
            drawCircle(
                color = Color.hsv(358f, 0.81f, 0.65f),
                radius = circleOneRadius.value
            )
            drawCircle(
                color = Color.hsv(358f, 0.81f, 0.9f),
                radius = circleTwoRadius.value
            )
            drawCircle(
                color = circleThreeColor.value,
                radius = circleThreeRadius.value
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.frontpage), contentDescription = null,
                modifier = Modifier.size(imageSize.value.dp)
            )
            if (boolText) {
                val textAlpha by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 400, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
                Text(
                    "TAP ANYWHERE TO BEGIN",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alpha(textAlpha)
                )
            }
        }
    }
}