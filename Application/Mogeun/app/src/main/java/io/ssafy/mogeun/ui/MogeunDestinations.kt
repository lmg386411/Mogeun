package io.ssafy.mogeun.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.ui.graphics.vector.ImageVector
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.R

data class TopBarState(
    val visibility: Boolean,
    val backBtnVisibility: Boolean
)

data class BottomBarState(
    val visibility: Boolean,
    val imgVector: ImageVector? = null,
    val originRoute: String? = null,
)

enum class Screen(
    val route: String,
    val title: String,
    val topBarState: TopBarState,
    val bottomBarState: BottomBarState
) {
    Splash(
        route = "Splash",
        title = MogeunApplication.getContext().getString(R.string.screen_loading),
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    Routine(
        route = "Routine",
        title = MogeunApplication.getContext().getString(R.string.screen_routine),
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, Icons.Default.AvTimer, "Routine")
    ),
    Execution(
    route = "Execution/{routineKey}",
    title = MogeunApplication.getContext().getString(R.string.screen_execution),
    topBarState = TopBarState(visibility = true, backBtnVisibility = false),
    bottomBarState = BottomBarState(false)
    ),
    Record(
        route = "Record",
        title = MogeunApplication.getContext().getString(R.string.screen_record),
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, Icons.Default.CalendarMonth, "Record")
    ),
    RecordDetail(
        route = "RecordDetail/{reportKey}",
        title = MogeunApplication.getContext().getString(R.string.screen_record_detail),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Record")
    ),
    ExerciseDetail(
        route = "ExerciseDetail/{index}",
        title = MogeunApplication.getContext().getString(R.string.screen_exercise_detail),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Record")
    ),
    Summary(
        route = "Summary",
        title = MogeunApplication.getContext().getString(R.string.screen_summary),
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, Icons.Default.StickyNote2, "Summary")
    ),
    Menu(
        route = "Menu",
        title = MogeunApplication.getContext().getString(R.string.screen_menus),
        topBarState = TopBarState(visibility = true, backBtnVisibility = false),
        bottomBarState = BottomBarState(true, Icons.Default.Menu, "Menu")
    ),
    User(
        route = "User",
        title = MogeunApplication.getContext().getString(R.string.screen_user_info),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Setting")
    ),
    Login(
        route = "Login",
        title = MogeunApplication.getContext().getString(R.string.screen_login),
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    Signup(
        route = "Signup",
        title = MogeunApplication.getContext().getString(R.string.screen_signup),
        topBarState = TopBarState(visibility = false, backBtnVisibility = false),
        bottomBarState = BottomBarState(false)
    ),
    AddRoutine(
        route = "AddRoutine/{routineKey}",
        title = MogeunApplication.getContext().getString(R.string.screen_routine_management),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Routine")
    ),
    AddExercise(
        route = "AddExercise/{beforeScreen}/{currentRoutineKey}",
        title = MogeunApplication.getContext().getString(R.string.screen_add_exercise),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Routine")
    ),
    ExplainExercise(
        route = "ExplainExercise/{image}",
        title = MogeunApplication.getContext().getString(R.string.screen_explain_exercise),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Routine")
    ),
    SqlSample(
        route = "SqlSample",
        title = "sql 테스트 페이지",
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true)
    ),
    Connection(
        route = "Connection",
        title = MogeunApplication.getContext().getString(R.string.screen_connection),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(false)
    ),
    Setting(
        route = "Setting",
        title = MogeunApplication.getContext().getString(R.string.screen_setting),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Menu")
    ),
    AppInfo(
        route = "AppInfo",
        title = MogeunApplication.getContext().getString(R.string.screen_appinfo),
        topBarState = TopBarState(visibility = true, backBtnVisibility = true),
        bottomBarState = BottomBarState(true, originRoute = "Menu")
    )
}

val rootScreen = arrayOf(Screen.Record, Screen.Routine, Screen.Summary, Screen.Menu)