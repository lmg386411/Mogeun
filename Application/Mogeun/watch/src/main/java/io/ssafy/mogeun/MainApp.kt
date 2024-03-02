package io.ssafy.mogeun

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import io.ssafy.mogeun.components.RoutineNameChip
import io.ssafy.mogeun.ui.theme.MogeunTheme

@Composable
fun MainApp(execName: String?, timerString: String?, messageString: String?, onStart: ()->Unit, onStop: ()->Unit, clearMsg: () -> Unit, progress: Float) {

    MogeunTheme {
        val listState = rememberScalingLazyListState()

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
            startAngle = 290f,
            endAngle = 250f,
            strokeWidth = 2.dp
        )

        Scaffold(
            timeText = {
                TimeText(modifier = Modifier.scrollAway(listState))
            },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
        ) {
            ScalingLazyColumn (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState,
                scalingParams = ScalingLazyColumnDefaults.scalingParams(
                    edgeScale = 0.5f,
                    minTransitionArea = 0.65f,
                    maxTransitionArea = 0.70f
                ),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    RoutineNameChip(text = execName?:"루틴을 시작해 주세요")
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = timerString?:"00:00")
                        Button(
                            modifier = Modifier.size(ButtonDefaults.DefaultButtonSize),
                            onClick = { onStart() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                            )
                        }
                        Button(
                            modifier = Modifier.size(ButtonDefaults.DefaultButtonSize),
                            onClick = { onStop() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = null,
                            )

                        }
                    }
                }
                if(!messageString.isNullOrEmpty()) {
                    item {
                        Card(onClick = clearMsg) {
                            Text(text = "$messageString", overflow = TextOverflow.Ellipsis, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}