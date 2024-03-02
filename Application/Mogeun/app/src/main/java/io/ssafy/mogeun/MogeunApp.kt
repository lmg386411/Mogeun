package io.ssafy.mogeun

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.ssafy.mogeun.ui.MogeunNavHost
import io.ssafy.mogeun.ui.Screen
import io.ssafy.mogeun.ui.rootScreen


@Composable
fun Navigation(
    setTheme: (useDynamic: Boolean, useSystemSetting: Boolean, useDarkMode: Boolean) -> Unit,
    enableDarkMode: Int
) {
    val snackbarHostState = remember{ SnackbarHostState() }

    val navController: NavHostController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    val darkmode = isSystemInDarkTheme() || enableDarkMode == 1

    val screens = arrayOf(Screen.ExplainExercise, Screen.AddRoutine, Screen.RecordDetail, Screen.ExerciseDetail, Screen.AddExercise, Screen.Execution)
    val currentRoute = navBackStackEntry.value?.destination?.route

    val currentScreen = try {
        Screen.valueOf(
            currentRoute ?: Screen.Login.name
        )
    } catch (e: Exception) {
        screens.find { it.route == currentRoute } ?: Screen.Login
    }
    Scaffold (
        topBar = {
            TopBar(navController, currentScreen, darkmode)
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(
                brush = Brush.verticalGradient(
                    if (darkmode) listOf(Color.Black, Color(0xFF210604)) else listOf(Color.White, Color(0xFFFFF7F7)),
                    startY = 100f,
                    endY = 800f
                ),
                shape = RectangleShape
            )
        ) {
            MogeunNavHost(navController, snackbarHostState, setTheme)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, currentScreen: Screen, darkmode: Boolean) {
    AnimatedVisibility(
        visible = currentScreen.topBarState.visibility,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        TopAppBar(
            title = { Text(currentScreen.title) },
            navigationIcon = {
                if (currentScreen.topBarState.backBtnVisibility) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if(darkmode) Color.Black else Color.White
            )
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController, currentScreen: Screen) {

    AnimatedVisibility(
        visible = currentScreen.bottomBarState.visibility,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar {
            rootScreen.forEach { screen ->
                NavigationBarItem(
                    selected = currentScreen.bottomBarState.originRoute == screen.route,
                    onClick = {
                        if(currentScreen.route != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(imageVector = screen.bottomBarState.imgVector!!, contentDescription = screen.route) },
                    label = { Text(screen.title) }
                )
            }
        }
    }
}