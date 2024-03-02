package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jaikeerthick.composable_graphs.color.LinearGraphColors
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.components.ElevatedGif
import io.ssafy.mogeun.ui.components.HorizontalPagerArrow
import io.ssafy.mogeun.ui.components.FatigueLineGraph
import io.ssafy.mogeun.ui.components.FatigueLineGraph2
import io.ssafy.mogeun.ui.components.MuscleTooltipIcon
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

data class MuscleFatigue(
    val set: String,
    val num: Float
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDetailScreen(
    navController: NavHostController,
    index: Int?
) {
    var exercises: List<Exercise>
    try {
        exercises = navController.previousBackStackEntry
            ?.savedStateHandle?.get<List<Exercise>>("exercises")!!
    } catch (e: NullPointerException) {
        exercises = emptyList()
    }

    var isAttached: Boolean
    try {
        isAttached = navController.previousBackStackEntry?.savedStateHandle?.get<Char>("isAttached")!! == 'Y'
    } catch (e: NullPointerException) {
        isAttached = false
    }

    val exerciseIndex = index!!

    if (!exercises.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 10.dp)
        ) {
            val pagerState = rememberPagerState(
                initialPage = exerciseIndex,
                pageCount = { exercises.size }
            )

            Column {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val coroutineScope = rememberCoroutineScope()

                    HorizontalPagerArrow(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage > 0)
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                } },
                        size = 50.dp,
                        visible = pagerState.currentPage > 0 && exercises.size > 1,
                        direction = true
                    )
                    Text(
                        exercises[pagerState.currentPage].execName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalPagerArrow(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage < exercises.size - 1)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } },
                        size = 50.dp,
                        visible = pagerState.currentPage < exercises.size - 1 && exercises.size > 1,
                        direction = false
                        )
                }
                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    ExerciseDetail(exercises[page], isAttached)
                }
            }
        }
    }
}

@Composable
fun ExerciseDetail(
    exercise: Exercise,
    isAttached: Boolean,
    viewModel: RecordViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        var leftMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var rightMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var muscleFatigueList: List<MutableList<MuscleFatigue>> = mutableListOf()
        var set = 1
        if (exercise.setResults.size == 1) {
            leftMuscleFatigueList.add(MuscleFatigue("", 0f))
            rightMuscleFatigueList.add(MuscleFatigue("", 0f))
        }
        for (setResult in exercise.setResults) {
            if (setResult.muscleFatigue!!.size > 1) {
                leftMuscleFatigueList.add(
                    MuscleFatigue(
                        set.toString() + "set",
                        setResult.muscleFatigue[0]
                    )
                )
                rightMuscleFatigueList.add(
                    MuscleFatigue(
                        set.toString() + "set",
                        setResult.muscleFatigue[1]
                    )
                )
            }
            set++
        }
        if (!leftMuscleFatigueList.isNullOrEmpty() || !rightMuscleFatigueList.isNullOrEmpty())
            muscleFatigueList = listOf(leftMuscleFatigueList, rightMuscleFatigueList)

        Box (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)) {
            ElevatedGif(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), imgPath = exercise.imagePath)
        }
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAttached) {
                ExerciseDropdown(viewModel)
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                )
                SetExplain(viewModel)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),) {
            if (viewModel.itemIndex.value == 1) {
                if (!muscleFatigueList.isNullOrEmpty())
                    MuscleFatigueCard(muscleFatigueList, exercise.parts)
                else {
                    Text("No Data")
                }
            }
            else {
                repeat(exercise.sets) {
                    SetDetail(it + 1, exercise.setResults[it], exercise.muscleImagePaths, isAttached)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MuscleFatigueCard(
    muscleFatigueList: List<MutableList<MuscleFatigue>>,
    parts: List<String>
) {
    val pagerState = rememberPagerState(pageCount = {
        2
    })
    val nameList = listOf("왼쪽 " + parts[0].split(" ")[1], "오른쪽 " + parts[0].split(" ")[1])

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()

                HorizontalPagerArrow(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(0)
                            } },
                    size = 30.dp,
                    visible = pagerState.currentPage > 0,
                    direction = true
                )
                Text(nameList[pagerState.currentPage])
                HorizontalPagerArrow(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(1)
                            } },
                    size = 30.dp,
                    visible = pagerState.currentPage < 1,
                    direction = false
                )
            }
            HorizontalPager(
                state = pagerState,
            ) { page ->
                // Our page content
                MuscleFatigueChart(muscleFatigueList[page], pagerState, page)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MuscleFatigueChart(
    muscleFatigueList: MutableList<MuscleFatigue>,
    pagerState: PagerState,
    page: Int
) {
    var trendLineVal1: Float = 0f
    var trendLineVal2: Float = 0f
    var trendLineVal3: Float = 0f
    var trendLineVal4: Int = 0
    var yAxisData2: MutableList<Float> = mutableListOf()
    val point = muscleFatigueList.map {
        it.num
    }
    for (i in 1 .. point.size) {
        trendLineVal1 += i * point[i - 1]
        trendLineVal2 += i
        trendLineVal3 += point[i - 1]
        trendLineVal4 += i * i
    }
    val a = point.size * trendLineVal1
    val b = trendLineVal2 * trendLineVal3
    val c = point.size * trendLineVal4
    val d  = trendLineVal2 * trendLineVal2
    val m = (a - b) / (c - d)
    val e = trendLineVal3
    val f = m * trendLineVal2
    val yIntercept = (e - f) / point.size
    for (i in 1 .. point.size) {
        yAxisData2.add(m * i + yIntercept)
    }
    Log.d("fatigue trend", "f(x) = ${m}x + ${yIntercept}")

    if (muscleFatigueList.isNullOrEmpty()) {
        Column(Modifier.fillMaxWidth()){
            Text("운동 기록이 없습니다.", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    else {
        Column(
            modifier = Modifier
                .graphicsLayer {
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        )

                alpha = lerp(
                    start = 0.4f,
                    stop = 1f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                )

                cameraDistance = 8 * density
                rotationY = lerp(
                    start = 0f,
                    stop = 40f,
                    fraction = pageOffset.coerceIn(-1f, 1f),
                )

                lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
            }
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp, horizontal = 1.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val style = LineGraphStyle(
                        paddingValues = PaddingValues(5.dp),
                        visibility = LinearGraphVisibility(
                            isHeaderVisible = true,
                            isXAxisLabelVisible = true,
                            isYAxisLabelVisible = true,
                            isGridVisible = true,
                            isCrossHairVisible = false
                        ),
                        colors = LinearGraphColors(
                            lineColor = MaterialTheme.colorScheme.primary,
                            pointColor = MaterialTheme.colorScheme.primary,
                            clickHighlightColor = MaterialTheme.colorScheme.inversePrimary,
                            fillGradient = null
                        ),
                        height = 200.dp,
                        yAxisLabelPosition = LabelPosition.LEFT
                    )
                    val style2 = LineGraphStyle(
                        paddingValues = PaddingValues(5.dp),
                        visibility = LinearGraphVisibility(
                            isHeaderVisible = true,
                            isXAxisLabelVisible = true,
                            isYAxisLabelVisible = true,
                            isGridVisible = true,
                            isCrossHairVisible = false
                        ),
                        colors = LinearGraphColors(
                            lineColor = Color.LightGray,
                            pointColor = MaterialTheme.colorScheme.primary,
                            clickHighlightColor = MaterialTheme.colorScheme.inversePrimary,
                            fillGradient = null
                        ),
                        height = 200.dp,
                        yAxisLabelPosition = LabelPosition.LEFT
                    )

                    if (muscleFatigueList.size == 1) {
                        val xAxisDataList = muscleFatigueList.map {
                            GraphData.String(it.set)
                        }
                        val yAxisDataList = listOf(0f, muscleFatigueList[0].num)
                        FatigueLineGraph(
                            xAxisData = xAxisDataList,
                            yAxisData = yAxisDataList,
                            style = style
                        )
                    }
                    else if (muscleFatigueList.size == 2) {
                        FatigueLineGraph(
                            xAxisData = muscleFatigueList.map {
                                GraphData.String(it.set)
                            },
                            yAxisData = muscleFatigueList.map {
                                it.num
                            },
                            style = style
                        )
                    }
                    else {
                        FatigueLineGraph2(
                            xAxisData = muscleFatigueList.map {
                                GraphData.String(it.set)
                            },
                            yAxisData1 = muscleFatigueList.map {
                                it.num
                            },
                            yAxisData2 = yAxisData2,
                            style = style,
                            style2 = style2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetDetail(
    setNum: Int,
    setDetail: SetResult,
    muscleImagePaths: List<String>,
    isAttached: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(1.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row (
            modifier = Modifier
                .clickable { expanded = !expanded },
        ) {
            val configuration = LocalConfiguration.current
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = if (!expanded) RoundedCornerShape(
                            90.dp,
                            0.dp,
                            0.dp,
                            90.dp
                        ) else RoundedCornerShape(16.dp, 0.dp, 0.dp, 0.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(setNum.toString() + "set")
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(color = Color.LightGray)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(setDetail.weight.toInt().toString() + "kg")
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(color = Color.LightGray)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = if (!expanded) RoundedCornerShape(
                            0.dp,
                            90.dp,
                            90.dp,
                            0.dp
                        ) else RoundedCornerShape(0.dp, 16.dp, 0.dp, 0.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isAttached)
                    Text(setDetail.successRep.toString() + '/' + setDetail.targetRep.toString() + "rep")
                else Text(setDetail.targetRep.toString() + "rep")
            }
        }
        if (expanded && !setDetail.muscleActivity.isNullOrEmpty() && isAttached) {
            Divider(thickness = 1.dp, color = Color.LightGray)
            MuscleActivity(setDetail.muscleActivity, muscleImagePaths[0])
        }
    }
}

@Composable
fun MuscleActivity(
    muscleActivityList: List<Float>?,
    muscleImagePath: String
) {
    val left = muscleActivityList!![0]
    val right = muscleActivityList[1]
    var balanceValue = try {
        left / (left + right)
    } catch(e: IllegalArgumentException) {
        0f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MuscleTooltipIcon(muscleImagePath, 50.dp, 40.dp, 2)
                Text("L", fontWeight = FontWeight.Bold)
                BalanceBar(balanceValue)
                Text("R", fontWeight = FontWeight.Bold)
                MuscleTooltipIcon(muscleImagePath, 50.dp, 40.dp, 3)
            }
        }
    }

}

@Composable
fun BalanceBar(balanceValue: Float) {
    Row(
        modifier = Modifier
            .width(150.dp)
            .height(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(balanceValue)
                .height(30.dp)
                .background(
                    color = when (balanceValue) {
                        in 0.41f..0.59f -> MaterialTheme.colorScheme.tertiary
                        in 0.31f..0.69f -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    }
                )
        ) {
            Text("")
        }
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .height(30.dp)
                .background(color = Color.Black)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(
                    color = when (balanceValue) {
                        in 0.41f..0.59f -> MaterialTheme.colorScheme.tertiaryContainer
                        in 0.31f..0.69f -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
        ) {
            Text("")
        }
    }
}

@Composable
fun ExerciseDropdown(viewModel: RecordViewModel) {
    val listItems = arrayOf(stringResource(R.string.exercise_detail_set_info),
        stringResource(R.string.exercise_detail_fatigue_graph))

    // state of the menu
    var expanded by remember { mutableStateOf(false) }
    var disabledItem by remember { mutableIntStateOf(viewModel.itemIndex.value) }
    var item by remember { mutableStateOf(listItems[viewModel.itemIndex.value]) }
    var itemIconIndex by remember { mutableIntStateOf(viewModel.itemIndex.value) }
    var itemIconList = listOf(R.drawable.baseline_arrow_drop_down_24, R.drawable.baseline_arrow_drop_up_24)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(5.dp)
            .clickable {
                expanded = true
                itemIconIndex = if (itemIconIndex == 0) 1
                else 0
            }
    ) {
        Row {
            Text(item)
            Spacer(
                modifier = Modifier
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Image(
                painter = painterResource(id = itemIconList[itemIconIndex]),
                contentDescription = "dropdown",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(20.dp)
            )
        }

        // drop down menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                itemIconIndex = if (itemIconIndex == 0) 1
                else 0
            }
        ) {
            // adding items
            listItems.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        disabledItem = itemIndex
                        item = itemValue
                        viewModel.itemIndex.value = itemIndex
                        itemIconIndex = if (itemIconIndex == 0) 1
                        else 0
                    },
                    enabled = (itemIndex != disabledItem),
                    text = { Text(text = itemValue) }
                )
            }
        }
    }
}

@Composable
fun SetExplain(viewModel: RecordViewModel) {
    var declarationDialogState by remember {
        mutableStateOf(false)
    }

    Image(
        modifier = Modifier.clickable { declarationDialogState = !declarationDialogState },
        painter = painterResource(id = R.drawable.info_icon),
        contentDescription = "set_info"
    )

    if (declarationDialogState)
        MinimalDialog(viewModel.itemIndex.value){ declarationDialogState = false }
}

@Composable
fun MinimalDialog(
    type: Int,
    onDismissRequest: () -> Unit
) {
    when(type) {
        0 -> Dialog1 { onDismissRequest() }
        1 -> Dialog2 { onDismissRequest() }
    }
}

@Composable
fun Dialog1(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onDismissRequest() },
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.exercise_detail_balance_description))
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                )
                Text(stringResource(R.string.exercise_detail_example))
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.exercise_detail_balanced))
                        BalanceBar(balanceValue = 0.5f)
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(stringResource(R.string.exercise_detail_biasedleft_first))
                            Text(stringResource(R.string.exercise_detail_biasedleft_second))
                        }
                        BalanceBar(balanceValue = 0.66f)
                    }
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(stringResource(R.string.exercise_detail_biasedright_first))
                            Text(stringResource(R.string.exercise_detail_biasedright_second))
                        }
                        BalanceBar(balanceValue = 0.3f)
                    }
                }
            }
        }
    }
}

@Composable
fun Dialog2(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onDismissRequest() },
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("피로도 추이 그래프 입니다.")
                Text("피로도가 높을 수록 값이 커집니다.")
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text("*주의", color = MaterialTheme.colorScheme.error)
                Text("휴식시간의 정도에 따라 값이 낮아질 수도 있습니다.")
            }
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}