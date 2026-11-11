package com.rohan.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rohan.todo.ui.AddEditScreen
import com.rohan.todo.ui.CircularReveal
import com.rohan.todo.ui.HomeScreen
import com.rohan.todo.ui.HomeViewModel
import com.rohan.todo.ui.SplashContent
import com.rohan.todo.ui.theme.Background
import com.rohan.todo.ui.theme.RTodoTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dao = (application as TodoApp).database.taskDao()
        setContent {
            RTodoTheme {
                val navController = rememberNavController()
                val vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory(dao))
                var showSplash by remember { mutableStateOf(true) }
                val reveal = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    delay(400)
                    reveal.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))
                    showSplash = false
                }

                Box(modifier = Modifier.fillMaxSize().background(Background)) {
                    if (showSplash) {
                        SplashContent()
                    } else {
                        CircularReveal(revealProgress = reveal.value) {
                            NavHost(navController = navController, startDestination = "home") {
                                composable("home") {
                                    HomeScreen(
                                        viewModel = vm,
                                        onAddClick = { navController.navigate("add") },
                                        onEditClick = { id -> navController.navigate("edit/$id") }
                                    )
                                }
                                composable("add") {
                                    AddEditScreen(vm, null) { navController.popBackStack() }
                                }
                                composable("edit/{id}") { entry ->
                                    val id = entry.arguments?.getString("id")?.toLongOrNull()
                                    AddEditScreen(vm, id) { navController.popBackStack() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
