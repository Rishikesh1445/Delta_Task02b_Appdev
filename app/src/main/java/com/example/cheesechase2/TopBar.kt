package com.example.cheesechase2

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var bgColor = Color.LightGray
var txtColor = Color.White
@Composable
fun Score(score: Int, dimension: WindowInfo, theme:Boolean) {
    if(!theme){
        bgColor = Color.White
        txtColor = Color.Black
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 16.dp)
    ) {
        drawRoundRect(
            color = bgColor,
            size = Size(400F, 100F),
            cornerRadius = CornerRadius(40F, 40F),
            topLeft = Offset(dimension.screenWidth - 450F, 0F)
        )
    }
    Image(
        painter = painterResource(id = R.drawable.star), contentDescription = null,
        modifier = Modifier
            .size(35.dp, 35.dp)
            .offset(250.dp, 15.dp)
    )
    Text(
        "$score", modifier = Modifier
            .fillMaxSize()
            .offset(310.dp, 15.dp), fontSize = 25.sp, color = txtColor
    )
}

@Composable
fun CheeseScore(cheese: Int, theme: Boolean) {
    if(!theme){
        bgColor = Color.White
        txtColor = Color.Black
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 16.dp)
    ) {
        drawRoundRect(
            bgColor,
            size = Size(400F, 100F),
            cornerRadius = CornerRadius(40F, 40F),
            topLeft = Offset(12F, 0F)
        )
    }
    Image(
        painter = painterResource(id = R.drawable.cheese), contentDescription = null,
        modifier = Modifier
            .size(35.dp, 35.dp)
            .offset(18.dp, 15.dp)
    )
    Text(
        "$cheese", modifier = Modifier
            .fillMaxSize()
            .offset(85.dp, 15.dp), fontSize = 25.sp, color = txtColor
    )
}

@Composable
fun HeartTime(time: Int, dimension: WindowInfo, theme: Boolean) {
    if(!theme){
        bgColor = Color.White
        txtColor = Color.Black
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 16.dp)
    ) {
        drawRoundRect(
            bgColor,
            size = Size(175F, 100F),
            cornerRadius = CornerRadius(40F, 40F),
            topLeft = Offset(dimension.screenWidth / 2 - 105, 0F)
        )
    }
    Image(
        painter = painterResource(id = R.drawable.heart), contentDescription = null,
        modifier = Modifier
            .size(20.dp, 20.dp)
            .offset(dimension.screenWidthinDp / 2 - 24.dp, 22.dp)
    )

    Text(
        "$time", modifier = Modifier
            .fillMaxSize()
            .offset(dimension.screenWidthinDp / 2, 18.dp), fontSize = 20.sp, color = txtColor
    )
}


