package io.ssafy.mogeun.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun RoutineNameChip(text: String) {
    Chip(
        onClick = {},
        label = {
            Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        icon = {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color(0xFFFB743C), RoundedCornerShape(20.dp))
            ) {
                Icon(
                    imageVector = Icons.Rounded.FitnessCenter,
                    contentDescription = "fitness icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
    )
}