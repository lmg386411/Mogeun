package io.ssafy.mogeun.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import io.ssafy.mogeun.ui.theme.MogeunTheme

@Composable
fun ElevatedGif(imgPath: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val gifResId = context.resources.getIdentifier("z_${imgPath}", "drawable", context.packageName)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .wrapContentSize()
    ) {
        Box(
            modifier = modifier
                .background(color = Color.White)
                .padding(12.dp)
        ) {
            GlideImage(
                imageModel = { gifResId },
                imageOptions = ImageOptions(
                    contentDescription = "GIF Image",
                    contentScale = ContentScale.Crop,
                ),
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun PreviewExecutionGif() {
    MogeunTheme {
        ElevatedGif(imgPath = "dumbbell_bench_press", Modifier
            .width(300.dp)
            .height(180.dp))
    }
}