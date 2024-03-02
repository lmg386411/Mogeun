package io.ssafy.mogeun.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PlainTooltipState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val muscleMap: Map<String, String> = mutableMapOf("y_abs" to "복근", "y_back" to "등", "y_biceps" to "이두", "y_calves" to "종아리", "y_chest" to "가슴", "y_hips" to "허벅지", "y_shoulders" to "어깨", "y_trapezius" to "승모근", "y_triceps" to "삼두")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleTooltipIcon(imagePath: String, container: Dp, content: Dp, type: Int) {
    val scope = rememberCoroutineScope()
    val plainTooltipState = remember { PlainTooltipState() }
    var muscleImage = ""

    Box (contentAlignment = Alignment.Center) {
        PlainTooltipBox(
            tooltip = { Text(text = muscleMap[imagePath]!!, modifier = Modifier.align(Alignment.Center)) },
            tooltipState = plainTooltipState,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = Color.Black
        ) { // content for TooltipBoxScope
            Text("")
        }
        when (type) {
            1 -> muscleImage = imagePath
            2 -> muscleImage = imagePath + "_l"
            3 -> muscleImage = imagePath + "_r"
        }
        MuscleIcon(modifier = Modifier.clickable { scope.launch { plainTooltipState.show() } }, muscleImage, container, content)
    }
}

@Composable
private fun MuscleIcon(modifier: Modifier, imagePath: String, container: Dp, content: Dp) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFFFF9F5),
                RoundedCornerShape(15.dp)
            )
            .width(container)
            .height(container),
        contentAlignment = Alignment.Center
    ) {
        val image = LocalContext.current.resources.getIdentifier(imagePath, "drawable", LocalContext.current.packageName)
        Image(
            painter = painterResource(id = image),
            contentDescription = imagePath,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(content)
                .width(content)
        )
    }
    Spacer(
        modifier = Modifier.width(10.dp)
    )
}