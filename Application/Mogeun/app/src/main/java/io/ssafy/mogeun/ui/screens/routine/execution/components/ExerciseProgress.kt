@file:Suppress("IMPLICIT_CAST_TO_ANY")

package io.ssafy.mogeun.ui.screens.routine.execution.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.routine.execution.EmgUiState
import io.ssafy.mogeun.ui.screens.routine.execution.InbodyInfo
import io.ssafy.mogeun.ui.screens.routine.execution.MuscleSensorValue
import io.ssafy.mogeun.ui.screens.routine.execution.SetProgress
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ExerciseProgress(
    emgUiState: EmgUiState,
    planInfo: List<SetProgress>,
    addSet: () -> Unit,
    removeSet: (Int) -> Unit,
    setWeight: (Int, Int) -> Unit,
    setRep: (Int, Int) -> Unit,
    startSet: (Int, Boolean) -> Unit,
    addCnt: (Int) -> Unit,
    endSet: (Int) -> Unit,
    inProgress: Boolean,
    setControl: Int,
    inbodyInfo: InbodyInfo
){
    val totalSet = planInfo.size
    val setCntList = (1..totalSet).map { it }

    var selectedTab by remember { mutableIntStateOf(0) }

    val setProgress = planInfo[selectedTab]

    val sensorData = setProgress.sensorData

    //시작 종료
    var isStarting by remember { mutableStateOf(false) }

    if(setControl == 1 && !inProgress) {
        startSet(selectedTab + 1, true)
    }
    if(setControl == 2 && inProgress) {
        endSet(selectedTab + 1)
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .height(300.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFF7F7F7)),
    ) {
        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = Color(0xFFDFEAFF))
        ) {
            SetOfRoutineRow(
                setCntList.map { "$it " + stringResource(R.string.execution_set) },
                selectedTab,
                { index -> selectedTab = index },
                inProgress,
                addSet,
                totalSet,
                removeSet,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
//            ElevatedAssistChip(
//                colors = AssistChipDefaults.elevatedAssistChipColors(
//                    containerColor = if (inProgress) MaterialTheme.colorScheme.secondary.copy(
//                        0.3f
//                    ) else MaterialTheme.colorScheme.secondary,
//                    leadingIconContentColor = MaterialTheme.colorScheme.onSecondary,
//                    labelColor = MaterialTheme.colorScheme.onSecondary
//                ),
//                enabled = !inProgress,
//                onClick = {
//                    addSet()
//                },
//                label = { Text(text = "세트 추가") },
//                leadingIcon = { Icon(Icons.Default.Add, null) },
//                modifier = Modifier.padding(horizontal = 4.dp)
//            )
        }
        Row(
            modifier = Modifier //---------------body-------------------
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (setProgress.successRep >= planInfo[selectedTab].targetRep) {
                if (inProgress) {
                    endSet(selectedTab + 1)
                    if (selectedTab < totalSet - 1) {
                        selectedTab += 1
                    }
                }
            }
            DateSelectionSection(
                onWeightChosen = { setWeight(selectedTab + 1, it.toInt()) },
                onRepChosen = { setRep(selectedTab + 1, it.toInt()) },
                preWeight = planInfo[selectedTab].targetWeight,
                preRep = planInfo[selectedTab].targetRep,
                inProgress = inProgress,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(140.dp)
            )
            Box(
                modifier = Modifier//EMG 신호 표기
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color(0xFFF7F7F7)),
            ) {
                EMGCollector(
                    emgUiState,
                    isStarting,
                    setProgress,
                    { addCnt(selectedTab + 1) },
                    inProgress,
                    sensorData,
                    inbodyInfo
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    TextButton(
                        enabled = !inProgress,
                        onClick = {
                            if(totalSet > 1) {
                                if (selectedTab == totalSet - 1) selectedTab = totalSet - 2
                                removeSet(selectedTab + 1)
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(text = stringResource(R.string.execution_remove_set))
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(4.dp)
                            .clickable {
                                if (!inProgress) startSet(selectedTab + 1, false)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null,
                            tint = if (!inProgress) Color(0xFF556FF7) else Color(0xFFDDDDDD),
                            modifier = Modifier.size(20.dp),
                        )
                        Text(text = stringResource(R.string.execution_start), fontSize = 15.sp, textAlign = TextAlign.Center)
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
                            .clickable {
                                if (inProgress) {
                                    endSet(selectedTab + 1)
                                    if (selectedTab < totalSet - 1) {
                                        selectedTab += 1
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.removecirclestop),
                            contentDescription = "contentDescription",
                            tint = if (inProgress) Color(0xFFFFD5D5) else Color(0xFFDDDDDD),
                            modifier = Modifier
                                .size(21.dp)
                                .padding(2.dp)
                        )
                        Text(text = stringResource(R.string.execution_end))
                    }
                }
            }
        }
    }
}

@Composable//header
private fun SetOfRoutineRow(
    tabs: List<String>,
    selectedTab: Int,
    onTabClick: (Int) -> Unit,
    inProgress: Boolean,
    addSet: () -> Unit,
    totalSet: Int,
    removeSet: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        containerColor = Color(0xFFDFEAFF),
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, text ->
            Tab(
                enabled = !inProgress,
                selected = selectedTab == index,
                onClick = {
                    onTabClick(index)
                },
                selectedContentColor = if(inProgress) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary,
                unselectedContentColor = if(inProgress) Color.Black.copy(alpha = 0.3f) else Color.Black,
                modifier = Modifier
                    .fillMaxHeight()
                    .size(20.dp, 36.dp)
            ) {
                Text(text = text, fontSize = 14.sp)
            }
        }
        Tab(
            enabled = true,
            selected = false,
            onClick = {
                addSet()
            },
            selectedContentColor = if(inProgress) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.primary,
            unselectedContentColor = if(inProgress) Color.Black.copy(alpha = 0.3f) else Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .size(10.dp, 36.dp)
        ) {
            Text(text = "+", fontSize = 14.sp)
        }
    }
}

//private fun ScrollButton(){}

//-------------스크롤 버튼-------------------

@Composable
fun DateSelectionSection(
    onWeightChosen: (String) -> Unit,
    onRepChosen: (String) -> Unit,
    preWeight: Int,
    preRep: Int,
    inProgress: Boolean,
    modifier: Modifier = Modifier
) {
    val kgValue = (0..300).map { it.toString() }
    val repValue = (0..100).map { it.toString() }

    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (inProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.1f))
                    .pointerInput(Unit) { }
                    .zIndex(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize(0.9f)
                .padding(10.dp)
                .shadow(1.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color(0xFFEDEDED))
                .padding(4.dp)
                .zIndex(0f)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(color = Color(0xFFC8C8C8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Kg",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                InfiniteItemsPicker(
                    items = kgValue,
                    firstIndex = (301 * 200) + preWeight - 1,
                    onItemSelected = onWeightChosen,
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(color = Color(0xFFC8C8C8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Rep")
                }
                InfiniteItemsPicker(
                    items = repValue,
                    firstIndex = (101 * 200) + preRep - 1,
                    onItemSelected = onRepChosen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun InfiniteItemsPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    firstIndex: Int,
    onItemSelected: (String) -> Unit,
) {
    // 얼마나 내렸는지 기억
    val listState = rememberLazyListState(firstIndex)
    val currentValue = remember { mutableStateOf("") }
    val previousValue = remember { mutableStateOf("$firstIndex")}

    LaunchedEffect(key1 = firstIndex) {
        if (firstIndex.toString() != currentValue.value) {
            val newPosition = (items.size * 30) + firstIndex

            listState.scrollToItem(newPosition)
        }
    }

    LaunchedEffect(key1 = !listState.isScrollInProgress) {
        if(previousValue.value != currentValue.value) {
            onItemSelected(currentValue.value)
            listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
            previousValue.value = currentValue.value
        }
    }

    Box(
        modifier = modifier
            .height(106.dp)
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            content = {
                items(count = Int.MAX_VALUE, itemContent = {
                    val index = it % items.size
                    if (it == listState.firstVisibleItemIndex + 1) {
                        currentValue.value = items[index]
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    var isEditing by remember { mutableStateOf(false) }
                    var Text by remember { mutableStateOf(String()) }

                    if (isEditing) {
                        var newText by remember { mutableStateOf(Text) }
                        val coroutineScope = rememberCoroutineScope() // CoroutineScope 생성

                        Box(
                            modifier = Modifier.wrapContentSize(unbounded = true, align = Alignment.TopStart)
                        ) {
                            TextField(
                                value = newText,
                                onValueChange = {
                                    newText = it
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number, // 숫자 입력 모드 설정
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        isEditing = false
                                        if (newText.isNotEmpty()) {
                                            val newPosition =
                                                (items.size * 30) + newText.toInt() - 1

                                            // CoroutineScope 내에서 scrollToItem 호출
                                            coroutineScope.launch {
                                                listState.scrollToItem(newPosition)
                                            }
                                        }
                                    }
                                ),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
                                singleLine = true,
                                modifier = Modifier
                                    .width(80.dp)
                                    .fillMaxHeight()
                            )
                        }
                    } else {
                        Text(
                            text = items[index],
                            modifier = Modifier
                                .alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f)
                                .background(Color(if (it == listState.firstVisibleItemIndex + 1) 0xFFDDE2FD else 0xFFFFFFFF))
                                .fillMaxWidth()
                                .clickable(
                                    enabled = it == listState.firstVisibleItemIndex + 1,
                                    onClick = {
                                        isEditing = true
                                        Text = items[index]
                                    }
                                ),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                })
            }
        )
    }
}

//--------------------------------------------

// 최신값
@Composable
fun EMGCollector(
    emgUiState: EmgUiState,
    isStarting:Boolean,
    planInfo: SetProgress,
    addCnt: () -> Unit,
    inProgress: Boolean,
    sensorData: List<MuscleSensorValue>,
    inbodyInfo: InbodyInfo
) {
    var lastLev by remember { mutableStateOf(0)}
    var lastTime by remember { mutableStateOf<Long>(0)}
    var lastCntTime by remember { mutableStateOf<Long>(0) }
    var currentLev by remember { mutableStateOf(0) }

    var scale by remember { mutableStateOf(1.0)}

    if (inbodyInfo.hasData) {
        val diff: Double = (4 - inbodyInfo.offset!!)

        scale = 1.0 + diff / 10
    }

    // CoroutineScope을 만듭니다.
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(emgUiState.emgAvg[0]) {
        if(inProgress) {
            val curTime = System.currentTimeMillis()

            if(curTime - lastTime >= 200) {
                currentLev = ((emgUiState.emgAvg[0] * scale / 90) + 1).toInt()
                if (currentLev >= 3 && lastLev < 3) {
                    if(curTime - lastCntTime > 1000) {
                        addCnt()
                        lastCntTime = curTime
                    }
                }
                lastLev = currentLev

                lastTime = curTime
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            Text(text = stringResource(R.string.execution_muscle_activity))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                Text("Lv. ${String.format("%.3f", (emgUiState.emgAvg[0] * scale / 90) + 1)}")
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size(if (emgUiState.emgAvg[0] * scale > 360) (emgUiState.emgAvg[0] * scale - 270).dp else (emgUiState.emgAvg[0] * scale % 360 / 4).dp)
                    .background(
                        when ((emgUiState.emgAvg[0] * scale / 90).toInt()) {
                            0 -> Color.White.copy(0.7f)
                            1 -> Color.Red.copy(0.7f)
                            2 -> Color.Green.copy(0.7f)
                            3 -> Color.Blue.copy(0.7f)
                            else -> Color.Magenta.copy(0.7f)
                        }
                    )
                    .wrapContentSize(Alignment.Center)
                )
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                Text("Lv. ${String.format("%.3f", (emgUiState.emgAvg[1] * scale / 90) + 1)}")
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size(if (emgUiState.emgAvg[1] * scale > 360) (emgUiState.emgAvg[1] * scale - 270).dp else (emgUiState.emgAvg[1] * scale % 360 / 4).dp)
                    .background(
                        when ((emgUiState.emgAvg[1] * scale / 90).toInt()) {
                            0 -> Color.White.copy(0.7f)
                            1 -> Color.Red.copy(0.7f)
                            2 -> Color.Green.copy(0.7f)
                            3 -> Color.Blue.copy(0.7f)
                            else -> Color.Magenta.copy(0.7f)
                        }
                    )
                    .wrapContentSize(Alignment.Center)
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            Text(text = stringResource(R.string.execution_muscle_fatigue))
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if(sensorData[0].muscleFatigue == null || sensorData[1].muscleFatigue == null) {
                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Text(text = stringResource(R.string.execution_proceed_set))
                }
            } else {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.White),
                    contentAlignment = Alignment.Center
                ){
                    Text("${String.format("%.3f", sensorData[0].muscleFatigue)}")

                }
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color.White),
                    contentAlignment = Alignment.Center
                ){
                    Text("${String.format("%.3f", sensorData[1].muscleFatigue)}")

                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFFDDE2FD)),
        ){
            Text(String.format(stringResource(R.string.execution_count) + " : ${planInfo.successRep}", emgUiState.emgAvg[0] % 90))
        }
    }
}

//-----------------------------------------------------

//@Preview(showBackground = true)
//@Composable
//fun PreviewEMGScreen(){
//    Column {
//        ExerciseEMGScreen()
//        FFT_ready(24)
//
//    }
//}
@Composable
//build.gradle에 //implementation ("com.github.wendykierp:JTransforms:3.1")//넣자
fun FFT_ready(N:Int){//N은 신호의 갯수
    val y = DoubleArray(N) //허수값 0

    for (i in 0 until N) {
        y[i] = 0.0
    }

    val x = DoubleArray(N) //실수값 여기에 신호를 넣자

    for (i in 0 until N) {
        x[i] = Math.sin(2 * Math.PI * 24 * 0.004 * i) + Math.sin(2 * Math.PI * 97 * 0.004 * i)
    }

    val a = DoubleArray(2 * N) //fft 수행할 배열 사이즈 2N

    for (k in 0 until N) {
        a[2 * k] = x[k] //Re
        a[2 * k + 1] = y[k] //Im
    }

    val fft = DoubleFFT_1D(N.toLong()) //1차원의 fft 수행

    fft.complexForward(a) //a 배열에 output overwrite


    val mag = DoubleArray(N / 2)
    var sum = 0.0
    for (k in 0 until N / 2) {
        mag[k] = Math.sqrt(Math.pow(a[2 * k], 2.0) + Math.pow(a[2 * k + 1], 2.0))
        sum += mag[k]
    }
    val average = DoubleArray(N / 2)
    var nowSum = 0.0
    for (k in 0 until N / 2) {
        nowSum+=mag[k];
        average[k]=nowSum/sum
    }

    val avrNumbers = average.map { it as Number }.toTypedArray()
}
