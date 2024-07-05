package com.example.cheesechase2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cheesechase2.ui.theme.CheeseChase2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheeseChase2Theme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val viewModel = GameViewModel()
                    val dimension = Dimensions()
                    val gyroscope = Gyro(context)
                    val navController = rememberNavController()
                    viewModel.getBitmapFromResource(context, R.drawable.trap, R.drawable.cheese, R.drawable.heart)
                    NavHost(
                        navController = navController,
                        startDestination = Screen.frontPage.route
                    ) {
                        composable(route = Screen.frontPage.route) {
                            FrontPage(viewModel, context, navController, dimension)
                        }
                        composable(route = Screen.gamePage.route) {
                            gamePage(viewModel, dimension, context, navController)
                        }
                    }
                }
            }
        }
    }
}