package io.ssafy.mogeun.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import io.ssafy.mogeun.R

/**
 * size = Dp
 * visible = Arrow visible if true
 * direction = true: Left, false: Right
 */
@Composable
fun HorizontalPagerArrow(modifier: Modifier, size: Dp, visible: Boolean, direction: Boolean) {
    Box (
        modifier = Modifier
            .width(size)
            .height(size),
        contentAlignment = Alignment.Center
    ) {
        if (visible) {
            Image(
                modifier = modifier
                    .height(size)
                    .width(size),
                painter = if (direction) painterResource(id = R.drawable.arrow_left_icon) else painterResource(id = R.drawable.arrow_right_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        else {
            Image(
                modifier = modifier
                    .height(size)
                    .width(size),
                painter = if (direction) painterResource(id = R.drawable.arrow_left_gray_icon) else painterResource(id = R.drawable.arrow_right_gray_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}