package io.ssafy.mogeun.ui.screens.record

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.screens.menu.menu.LazyHeader
import io.ssafy.mogeun.ui.screens.summary.BodyInfoSummaryCard
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun RecordScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {

    var exitCnt = 0
    val activity = (LocalContext.current as? Activity)
    val coroutineScope = rememberCoroutineScope()

    val terminateMessage = stringResource(R.string.terminate_message)
    BackHandler {
        if(exitCnt == 0) {
            exitCnt++
            coroutineScope.launch {
                snackbarHostState.showSnackbar(terminateMessage)
            }
        } else {
            activity?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalenderUI(500, navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalenderUI(
    adjacentMonths: Long = 500,
    navController: NavHostController,
    viewModel: RecordViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(adjacentMonths) }
    val endMonth = remember { currentMonth.plusMonths(adjacentMonths) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }

    val routines = viewModel.recordList.groupBy { it.date }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstMostVisibleMonth(state, viewportPercent = 90f)

        // 해당 달에 대한 루틴 수행 기록 rest api 통신
        val recordMonthlySuccess by viewModel.recordMonthlySuccess.collectAsState()
        if (!recordMonthlySuccess) {
            LaunchedEffect(viewModel.userKey) {
                Log.d("date", visibleMonth.yearMonth.toString().plus("-01"))
                viewModel.recordMonthly(visibleMonth.yearMonth.toString().plus("-01"))
            }
        }

        LaunchedEffect(visibleMonth) {
            // Clear selection if we scroll to a new month.
            selection = null
            viewModel.initRecordMonthlySuccess()
        }

        SimpleCalendarTitle(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )
        HorizontalCalendar(
            modifier = Modifier.testTag("calendar"),
            state = state,
            dayContent = { day ->
                Day(
                    day,
                    isSelected = routines[day.date.toString()].isNullOrEmpty()
                ) { clicked ->
                    selection = clicked
                }
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            },
        )
    }
    if (selection.isNotNull())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            stickyHeader {
                LazyHeader(selection?.date.toString())
            }
            val date = selection?.date
            val routineLists = routines[date.toString()].orEmpty()
            var reportKeyList: MutableList<Int> = mutableListOf()
            var routineTimeList: MutableList<String> = mutableListOf()

            for (record in viewModel.recordList) {
                val routineReports = record.routineReports.sortedBy { it.startTime }
                for (routine in routineReports) {
                    reportKeyList.add(routine.key)
                    routineTimeList.add(routine.startTime.split("T")[1].split(".")[0] + " ~ " + routine.endTime.split("T")[1].split(".")[0])
                }
            }

            if (!routineLists.isEmpty()) {
                val routineReports = routineLists[0].routineReports.sortedBy { it.startTime }
                items(items = routineReports) { routineReport ->
                    RoutineRecord(navController, routineReport.key, routineReport.startTime.split("T")[1].split(".")[0] + " ~ " + routineReport.endTime.split("T")[1].split(".")[0], routineReport.routineName, reportKeyList, routineTimeList)
                }
            }
        }
    else
        Text("")
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.arrow_left_icon),
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.toString(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.arrow_right_icon),
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        painter = icon,
        contentDescription = contentDescription,
    )
}

@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("MonthHeader"),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.toString().substring(0,3),
                fontWeight = FontWeight.Medium,
                color = if (dayOfWeek.toString().substring(0,3) == "SAT") Color.Blue else if (dayOfWeek.toString().substring(0,3) == "SUN") Color.Red else Color.Black
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .testTag("MonthDay")
            .padding(6.dp)
            .clip(CircleShape)
            .clickable(
                onClick = { onClick(day) },
            )
            .background(color = if (isSelected) MaterialTheme.colorScheme.background else if (day.position != DayPosition.MonthDate) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) Color.Unspecified else Color.White
            DayPosition.InDate -> if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.onBackground else Color.Gray
            DayPosition.OutDate -> Color.Gray
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

@Composable
fun RoutineRecord(
    navController: NavHostController,
    routineKey: Int,
    routineTime: String,
    routineName: String,
    reportKeyList: List<Int>,
    routineTimeList: List<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .wrapContentHeight()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "reportKeyList",
                    reportKeyList
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "routineTimeList",
                    routineTimeList
                )
                navController.navigate("RecordDetail/${routineKey}")
            }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(routineTime, fontWeight = FontWeight.Bold)
            Text(routineName)
        }
    }
}