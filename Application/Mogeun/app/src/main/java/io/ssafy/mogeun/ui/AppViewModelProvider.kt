package io.ssafy.mogeun.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.ui.screens.login.LoginViewModel
import io.ssafy.mogeun.ui.screens.record.RecordViewModel
import io.ssafy.mogeun.ui.screens.sample.DbSampleViewModel
import io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise.AddExerciseViewModel
import io.ssafy.mogeun.ui.screens.routine.execution.ExecutionViewModel
import io.ssafy.mogeun.ui.screens.menu.connection.ConnectionViewModel
import io.ssafy.mogeun.ui.screens.menu.menu.MenuViewModel
import io.ssafy.mogeun.ui.screens.menu.setting.SettingViewModel
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineViewModel
import io.ssafy.mogeun.ui.screens.splash.SplashScreen
import io.ssafy.mogeun.ui.screens.splash.SplashViewModel
import io.ssafy.mogeun.ui.screens.summary.SummaryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            LoginViewModel(
                mogeunApplication().container.userDataRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            RecordViewModel(
                mogeunApplication().container.recordRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            ExecutionViewModel(
                mogeunApplication().container.executionRepository,
                mogeunApplication().container.emgDataRepository,
                mogeunApplication().container.routineRepository,
                mogeunApplication().container.bleRepository,
                mogeunApplication().container.keyRepository,
                mogeunApplication().container.dataLayerRepository,
                mogeunApplication().container.userDataRepository
            )
        }
        initializer {
            DbSampleViewModel(
                mogeunApplication().container.emgDataRepository
            )
        }
        initializer {
            AddExerciseViewModel(
                mogeunApplication().container.keyRepository,
                mogeunApplication().container.routineRepository
            )
        }
        initializer {
            SummaryViewModel(
                mogeunApplication().container.summaryRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            MenuViewModel(
                mogeunApplication().container.keyRepository,
                mogeunApplication().container.userDataRepository
            )
        }
        initializer {
            ConnectionViewModel(
                mogeunApplication().container.bleRepository,
            )
        }
        initializer {
            SettingViewModel(
                mogeunApplication().container.bleRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            RoutineViewModel(
                mogeunApplication().container.userDataRepository,
                mogeunApplication().container.keyRepository,
                mogeunApplication().container.routineRepository,
            )
        }
        initializer {
            SplashViewModel(
                mogeunApplication().container.keyRepository,
            )
        }
    }
}

fun CreationExtras.mogeunApplication(): MogeunApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
