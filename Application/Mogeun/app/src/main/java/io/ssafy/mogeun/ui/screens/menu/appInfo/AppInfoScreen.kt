package io.ssafy.mogeun.ui.screens.menu.appInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ssafy.mogeun.R

@Composable
fun AppInfoScreen(
) {
    val appInfoItems = listOf<AppInfoItem>(
        AppInfoItem(stringResource(R.string.menu_app_version), "0.8.0")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.surfaceTint,
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.slogan_1),
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.slogan_2),
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.slogan_3),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(appInfoItems) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    ) {
                        Text(text = it.key, fontWeight = FontWeight.Bold)
                        Text(text = it.value, fontWeight = FontWeight.Medium)
                    }
                    Divider()
                }
            }
        }
    }
}

data class AppInfoItem(
    val key: String,
    val value: String
)