package io.ssafy.mogeun.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    setTheme: (useDynamic: Boolean, useSystemSetting: Boolean, useDarkMode: Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }
    LaunchedEffect(viewModel.userKey) {
        if (viewModel.userKey !== null) {
            delay(3000)
            navController.navigate("Routine")
        } else {
            delay(3000)
            navController.navigate("Login")
        }
    }
    LaunchedEffect(viewModel.settingLoaded) {
        if(viewModel.settingLoaded) {
            val useDynamic = viewModel.dynamicMode
            val useDarkMode = viewModel.darkMode
            val useLightMode = viewModel.lightMode
            val useSystemSetting = !useDarkMode && !useLightMode
            setTheme(useDynamic, useSystemSetting, useDarkMode)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(450.dp),
                alignment = Alignment.Center
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.surfaceTint,
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(R.string.slogan_1),
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.slogan_2),
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.slogan_3),
            fontSize = 24.sp
        )
    }
}