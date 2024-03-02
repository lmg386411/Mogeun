package io.ssafy.mogeun.ui.screens.routine.execution

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import com.google.android.gms.wearable.Wearable
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.components.AlertDialogCustom
import io.ssafy.mogeun.ui.components.ElevatedGif
import io.ssafy.mogeun.ui.screens.routine.execution.components.ExerciseProgress
import io.ssafy.mogeun.ui.screens.routine.execution.components.RoutineProgress
import io.ssafy.mogeun.ui.screens.routine.execution.components.SensorBottomSheet
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExecutionScreen(viewModel: ExecutionViewModel = viewModel(factory = AppViewModelProvider.Factory), routineKey: Int, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val messageClient by lazy { Wearable.getMessageClient(MogeunApplication.getContext()) }

    val emgState by viewModel.emgState.collectAsState()
    val sensorState by viewModel.sensorState.collectAsState()
    val routineState by viewModel.routineState.collectAsState()
    val elapsedTime by viewModel.elaspedTime.collectAsState()
    val setControl by viewModel.setControl.collectAsState()
    val inbodyInfo by viewModel.inbodyInfo.collectAsState()

    var openEndDialog by remember { mutableStateOf(false) }
    var openNoSensorDialog by remember { mutableStateOf(false) }

    var tempPlanKey by remember { mutableStateOf(0)}
    var tempIdx by remember { mutableStateOf(0)}

    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        openEndDialog = true
    }

    LaunchedEffect(Unit) {
        viewModel.getUserKey()
        viewModel.getSensorVal()
        viewModel.launchWearApp()
    }

    LaunchedEffect(viewModel.userKey) {
        if(viewModel.userKey.isNotNull()) {
            viewModel.getInbodyInfo()
        }
    }

    DisposableEffect(Unit) {
        messageClient.addListener(viewModel)
        coroutineScope.launch {
            val ret = async {
                viewModel.getPlanList(routineKey)
            }.await()
            viewModel.getSetOfRoutine()
        }
        viewModel.noticeStartOfRoutine()
        this.onDispose {
            viewModel.resetRoutine()
            messageClient.removeListener(viewModel)

            coroutineScope.launch {
                viewModel.deleteEmgData()
            }
            viewModel.noticeEndOfRoutine()
        }
    }

    if(routineState.planList == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        }
    } else {
        val routineSize = routineState.planList!!.data.size
        val pagerState = rememberPagerState { routineSize }

        if(routineState.planDetails.isNotEmpty()) {
            Column {
                HorizontalPager(
                    pagerState,
                    userScrollEnabled = !routineState.setInProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    val plan = routineState.planList!!.data[page]
                    val imgPath = plan.imagePath

                    viewModel.noticeExerciseName(routineState.planList!!.data[pagerState.currentPage].name)

                    val scrollState = rememberScrollState()

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(12.dp)
                        ) {
                            ElevatedGif(imgPath = imgPath,
                                Modifier
                                    .width(300.dp)
                                    .height(200.dp))
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 40.dp)
                        ) {
                            Text("${plan.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            FilledTonalIconButton(
                                enabled = !routineState.setInProgress,
                                onClick = viewModel::showBottomSheet,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(44.dp)
                            ){
                                Icon(painter = painterResource(id = R.drawable.heart_rate), null, modifier = Modifier.fillMaxSize(0.8f))
                            }
                        }

                        val startRoutineMsg = stringResource(R.string.execution_start_routine)

                        ExerciseProgress(
                            emgState,
                            routineState.planDetails[page].setOfRoutineDetail,
                            {viewModel.addSet(plan.planKey)},
                            {idx -> viewModel.removeSet(plan.planKey, idx)},
                            {idx, weight -> viewModel.setWeight(plan.planKey, idx, weight)},
                            {idx, rep -> viewModel.setRep(plan.planKey, idx, rep)},
                            { idx, isRemote ->
                                if(!routineState.routineInProgress) {
                                    if(!sensorState.connectedDevices[0].isNotNull() && !sensorState.connectedDevices[1].isNotNull()) {
                                        if(isRemote) {
                                            coroutineScope.launch {
                                                viewModel.startRoutine(routineKey, isAttached = "N")
                                                snackbarHostState.showSnackbar(startRoutineMsg)
                                            }
                                            viewModel.startSet(plan.planKey, idx)
                                        } else {
                                            tempPlanKey = plan.planKey
                                            tempIdx = idx
                                            openNoSensorDialog = true
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            viewModel.startRoutine(routineKey)
                                            snackbarHostState.showSnackbar(startRoutineMsg)
                                        }
                                        viewModel.startSet(plan.planKey, idx)
                                    }
                                } else {
                                    viewModel.startSet(plan.planKey, idx)
                                }
                            },
                            {idx -> viewModel.addCnt(plan.planKey, idx)},
                            {idx -> viewModel.endSet(plan.planKey, idx)},
                            routineState.setInProgress,
                            setControl,
                            inbodyInfo
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                viewModel.noticeProgress(pagerState.currentPage + 1, routineSize)
                RoutineProgress(pagerState.currentPage + 1, routineSize, elapsedTime, {openEndDialog = true}, routineState.setInProgress, {coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }}, {coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }}, routineSize, pagerState.currentPage)

                SensorBottomSheet(state = routineState.showBottomSheet, hide = viewModel::hideBottomSheet, navToConnection = {navController.navigate("Connection")}, sensorState = sensorState, sensingPart = routineState.planList!!.data[pagerState.currentPage].mainPart.imagePath)
            }
        }
    }
    when {
        openEndDialog -> {
            val routineEndedMsg = stringResource(R.string.execution_routine_ended_msg)
            val routineCanceledMsg = stringResource(R.string.execution_routine_canceled_msg)
            val routineCanceledWatchMsg = stringResource(R.string.execution_routine_canceled_watch_msg)
            AlertDialogCustom(
                onDismissRequest = {
                    openEndDialog = false
                                   },
                onConfirmation = {
                    openEndDialog = false
                    viewModel.endRoutine()
                    if (routineState.hasValidSet) {
                        navController.navigate("RecordDetail/${routineState.reportKey}") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(routineEndedMsg)
                        }
                    } else {
                        navController.popBackStack()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(routineCanceledMsg)
                        }
                        viewModel.noticeTimer("00:00")
                        viewModel.noticeExerciseName(routineCanceledWatchMsg)
                    }

                                 },
                dialogTitle = if (routineState.hasValidSet) {
                    stringResource(R.string.execution_end_today_routine)
                } else {
                    stringResource(R.string.execution_cancel_routine_process)
                },
                dialogText = if (routineState.hasValidSet) {
                    stringResource(R.string.execution_save_procedure_and_end_routine)
                } else {
                    stringResource(R.string.execution_cancel_procedure_and_end_routine)
                },
                icon = Icons.Default.PauseCircle,
                iconColor = Color.Blue
            )
        }
    }
    when {
        openNoSensorDialog -> {
            val startRoutineMsg = stringResource(id = R.string.execution_start_routine)
            AlertDialogCustom(
                onDismissRequest = {
                    openNoSensorDialog = false
                },
                onConfirmation = {
                    openNoSensorDialog = false

                    coroutineScope.launch {
                        viewModel.startRoutine(routineKey, isAttached = "N")
                        snackbarHostState.showSnackbar(startRoutineMsg)
                    }
                    viewModel.startSet(tempPlanKey, tempIdx)
                },
                dialogTitle = stringResource(R.string.execution_no_sensor_connected),
                dialogText = stringResource(R.string.execution_would_you_like_to_start),
                icon = Icons.Default.PauseCircle,
                iconColor = Color.Blue
            )
        }
    }
    when {
        viewModel.fatigueWarning -> {
            AlertDialogCustom(
                onDismissRequest = {
                    viewModel.fatigueWarning = false
                },
                onConfirmation = {
                    viewModel.fatigueWarning = false
                },
                dialogTitle = "피로도가 높습니다!",
                dialogText = "휴식을 취하거나 다른 운동을 진행하세요",
                icon = Icons.Default.Error,
                iconColor = Color.Red
            )
        }
    }
}