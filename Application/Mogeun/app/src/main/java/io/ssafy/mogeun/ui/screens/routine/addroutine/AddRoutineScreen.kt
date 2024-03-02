package io.ssafy.mogeun.ui.screens.routine.addroutine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.ListMyExerciseResponseData

@Composable
fun AddRoutineScreen(
    navController: NavHostController,
    viewModel: AddRoutineViewModel = viewModel(factory = AddRoutineViewModel.Factory),
    routineKey: Int?
) {
    val beforeScreen = 2
    LaunchedEffect(Unit){
        viewModel.getUserKey()
        viewModel.listMyExercise(routineKey)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        LazyColumn {
            viewModel.exerciseList?.let {
                itemsIndexed(it) { index, item ->
                    ExerciseList(item, navController)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 0.dp,
                ),
                onClick = { navController.navigate("addexercise/${beforeScreen}/${routineKey}") }
            ) {
                Text(stringResource(R.string.add_delete_exercise))
            }
        }
    }
}
@Composable
fun ExerciseList(item: ListMyExerciseResponseData, navController:NavHostController) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier("x_${item.imagePath}", "drawable", context.packageName)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = { imageResId },
                imageOptions = ImageOptions(
                    contentDescription = "GIF Image",
                    contentScale = ContentScale.Crop,
                ),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "${item.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    for (i in item.sensingPart) {
                        Text(text = i)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.navigate("explainexercise/${item.execKey}") }) {
                    Icon(
                        Icons.Outlined.ErrorOutline,
                        contentDescription = "Localized description",
                        modifier = Modifier.graphicsLayer(rotationZ = 180f)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}