package io.ssafy.mogeun.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import io.ssafy.mogeun.ui.screens.routine.addroutine.AddRoutineScreen
import io.ssafy.mogeun.ui.screens.login.LoginScreen
import io.ssafy.mogeun.ui.screens.menu.appInfo.AppInfoScreen
import io.ssafy.mogeun.ui.screens.record.ExerciseDetailScreen
import io.ssafy.mogeun.ui.screens.record.RecordDetailScreen
import io.ssafy.mogeun.ui.screens.record.RecordScreen
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineScreen
import io.ssafy.mogeun.ui.screens.routine.execution.ExecutionScreen
import io.ssafy.mogeun.ui.screens.menu.menu.MenuScreen
import io.ssafy.mogeun.ui.screens.signup.SignupScreen
import io.ssafy.mogeun.ui.screens.summary.SummaryScreen
import io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise.AddExerciseScreen
import io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise.ExplainExerciseScreen
import io.ssafy.mogeun.ui.screens.sample.DbSampleScreen
import io.ssafy.mogeun.ui.screens.menu.connection.ConnectionScreen
import io.ssafy.mogeun.ui.screens.menu.setting.SettingScreen
import io.ssafy.mogeun.ui.screens.menu.user.UserScreen
import io.ssafy.mogeun.ui.screens.splash.SplashScreen

@Composable
fun MogeunNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState, setTheme: (useDynamic: Boolean, useSystemSetting: Boolean, useDarkMode: Boolean) -> Unit) {
    NavHost(navController, startDestination = Screen.Splash.route) {
//        스플래시
        composable(Screen.Splash.route) { SplashScreen(navController = navController, setTheme = setTheme)}
//        루틴
        navigation(route = "Routines", startDestination = Screen.Routine.route) {
            composable(Screen.Routine.route) { RoutineScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            ) }
            composable(
                Screen.Execution.route,
                arguments = listOf(navArgument("routineKey") {type = NavType.IntType})
            ) { backStackEntry ->
                ExecutionScreen(
                    routineKey = backStackEntry.arguments?.getInt("routineKey")!!,
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
            composable(
                Screen.AddRoutine.route,
                arguments = listOf(navArgument("routineKey") {type = NavType.IntType})
            ) {backStackEntry ->
                AddRoutineScreen(navController = navController, routineKey = backStackEntry.arguments?.getInt("routineKey")) }
            composable(
                Screen.AddExercise.route,
                arguments = listOf(navArgument("beforeScreen") {type = NavType.IntType},
                    navArgument("currentRoutineKey") {type = NavType.IntType})
            ) { backStackEntry ->
                AddExerciseScreen(
                    navController = navController,
                    beforeScreen = backStackEntry.arguments?.getInt("beforeScreen"),
                    currentRoutineKey = backStackEntry.arguments?.getInt("currentRoutineKey")
                ) }

            composable(
                Screen.ExplainExercise.route,
                arguments = listOf(navArgument("image") { type = NavType.StringType })
            ) { backStackEntry ->
                ExplainExerciseScreen(navController = navController, data = backStackEntry.arguments?.getString("image"))
            }
        }
//        기록
        navigation(route = "Records", startDestination = Screen.Record.route) {
            composable(Screen.Record.route) { RecordScreen(navController = navController, snackbarHostState) }
            composable(
                Screen.RecordDetail.route,
                arguments = listOf(navArgument("reportKey") { type = NavType.StringType })
            )
            { backStackEntry ->
                RecordDetailScreen(navController = navController, reportKey = backStackEntry.arguments?.getString("reportKey"))
            }
            composable(
                Screen.ExerciseDetail.route,
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            )
            { backStackEntry ->
                ExerciseDetailScreen(navController = navController, index = backStackEntry.arguments?.getInt("index"))
            }
        }
//        요약
        composable(Screen.Summary.route) { SummaryScreen(snackbarHostState = snackbarHostState) }
//        메뉴
        navigation(route = "Menus", startDestination = Screen.Menu.route) {
            composable(Screen.Menu.route) { MenuScreen(navController = navController, snackbarHostState = snackbarHostState) }
            composable(Screen.Connection.route) { ConnectionScreen(snackbarHostState = snackbarHostState) }
            composable(Screen.Setting.route) { SettingScreen(setTheme = setTheme) }
            composable(Screen.AppInfo.route) { AppInfoScreen() }
        }

        composable(Screen.User.route) { UserScreen(navController = navController)}
        composable(Screen.Login.route) { LoginScreen(
            navController = navController,
            snackbarHostState = snackbarHostState
        ) }
        composable(Screen.Signup.route) {
            SignupScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            ) 
        }

        composable(Screen.SqlSample.route) { DbSampleScreen() }

    }
}