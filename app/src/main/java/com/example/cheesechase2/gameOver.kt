package com.example.cheesechase2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun gameover(useCheese: () -> Unit, playagain: () -> Unit, home: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.7f)
            .background(Color.LightGray)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp, 245.dp)
            .background(Color.White, RoundedCornerShape(35.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.gameover),
            contentDescription = null,
            modifier = Modifier.size(100.dp, 85.dp)
        )

        Text(
            text = "Jerry got caught :((",
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(1000),
            fontSize = 25.sp
        )
        Button(
            onClick = { useCheese() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
                .height(50.dp),
            colors = ButtonDefaults
                .buttonColors(containerColor = Color.hsv(30f, 1f, 0.59f))
        )
        {
            Image(
                painter = painterResource(id = R.drawable.cheese), contentDescription = null,
                modifier = Modifier
                    .size(50.dp, 50.dp)
            )
            Text(
                text = "Use Cheese",
                color = Color.hsv(62f, 1f, 1f),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(1000),
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { playagain() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
                .height(50.dp),
            colors = ButtonDefaults
                .buttonColors(containerColor = Color.hsv(195f, 0.80f, 0.94f))
        )
        {
            Text(
                text = "Play Again",
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(1000),
                fontSize = 20.sp
            )
        }
        Button(
            onClick = { home() ; playagain()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
                .height(50.dp),
            colors = ButtonDefaults
                .buttonColors(containerColor = Color.hsv(3f, 0.66f, 1f))
        )
        {
            Text(
                text = "Home",
                color = Color.White,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(1000),
                fontSize = 20.sp
            )
        }
    }
}
