package com.example.cheesechase2

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline val Dp.px: Float
    @Composable get() = with(LocalDensity.current) { this@px.toPx() }
//inline val Int.dp: Dp
    //@Composable get() = with(LocalDensity.current) { this@dp.toDp() }

@Composable
fun Dimensions(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidth = configuration.screenWidthDp.dp.px,
        screenHeight = configuration.screenHeightDp.dp.px,
        screenWidthinDp = configuration.screenWidthDp.dp,
        screenHeightinDp = configuration.screenHeightDp.dp
    )
}

data class WindowInfo(
    val screenWidth: Float,
    val screenHeight: Float,
    val screenWidthinDp : Dp,
    val screenHeightinDp : Dp
)