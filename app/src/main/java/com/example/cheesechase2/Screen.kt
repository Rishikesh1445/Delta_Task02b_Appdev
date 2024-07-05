package com.example.cheesechase2

sealed class Screen(val route:String) {
    object frontPage: Screen("frontPage")
    object gamePage: Screen("gamePage")
}