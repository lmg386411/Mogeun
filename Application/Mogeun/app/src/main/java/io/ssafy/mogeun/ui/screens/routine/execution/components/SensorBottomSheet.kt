package io.ssafy.mogeun.ui.screens.routine.execution.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.routine.execution.SensorState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorBottomSheet(state: Boolean, hide: () -> Unit, navToConnection: () -> Unit, sensorState: SensorState, sensingPart: String) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val leftResId = context.resources.getIdentifier("${sensingPart}_l", "drawable", context.packageName)
    val rightResId = context.resources.getIdentifier("${sensingPart}_r", "drawable", context.packageName)


    if (state) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        hide()
                    }
                }
            },
            sheetState = sheetState
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = { navToConnection() },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(text = stringResource(R.string.execution_connect_sensor))
                }
                ElevatedButton(
                    onClick = {
                        coroutineScope.launch { hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                hide()
                            }
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(R.string.execution_close))
                }
            }
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                ) {
                    Text(
                        "L",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 12.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = leftResId),
                                    null,
                                    Modifier.fillMaxSize(0.7f)
                                )
                            }
                            Divider(
                                Modifier
                                    .fillMaxHeight(0.9f)
                                    .width(1.dp)
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = rightResId),
                                    null,
                                    Modifier.fillMaxSize(0.7f)
                                )
                            }
                        }
                        Divider(Modifier.fillMaxWidth())
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.heart_rate),
                                    null,
                                    Modifier.fillMaxSize(0.7f),
                                    colorFilter = if(sensorState.connectedDevices[0].isNotNull()) ColorFilter.tint(Color(0xff00ff54)) else ColorFilter.tint(Color(0xffff0054))
                                )
                            }
                            Divider(
                                Modifier
                                    .fillMaxHeight(0.9f)
                                    .width(1.dp)
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.heart_rate),
                                    null,
                                    Modifier.fillMaxSize(0.7f),
                                    colorFilter = if(sensorState.connectedDevices[1].isNotNull()) ColorFilter.tint(Color(0xff00ff54)) else ColorFilter.tint(Color(0xffff0054))
                                )
                            }
                        }
                    }
                    Text(
                        "R",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 20.dp, top = 12.dp)
                    )
                }
            }
        }
    }
}