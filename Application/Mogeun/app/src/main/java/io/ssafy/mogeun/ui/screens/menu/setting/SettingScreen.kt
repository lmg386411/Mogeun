package io.ssafy.mogeun.ui.screens.menu.setting

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.AppViewModelProvider

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.Factory),
    setTheme: (useDynamic: Boolean, useSystemSetting: Boolean, useDarkMode: Boolean) -> Unit
) {
    val useDynamic = viewModel.useDynamic.collectAsState().value
    val useDarkMode = viewModel.useDarkMode.collectAsState().value
    val useLightMode = viewModel.useLightMode.collectAsState().value
    val useVirtual = viewModel.useVirtual.collectAsState().value

    LaunchedEffect(useDynamic, useDarkMode, useLightMode) {
        val useSystemSetting = !useDarkMode && !useLightMode
        Log.d("theme", "dynamic : $useDynamic, dark : $useDarkMode, light : $useLightMode")
        setTheme(useDynamic, useSystemSetting, useDarkMode)
    }

    val appMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            stringResource(R.string.setting_dynamic_color),
            stringResource(R.string.setting_color_from_background),
            Icons.Default.Palette,
            useDynamic,
            {
                viewModel.switchModes(!useDynamic, useDarkMode, useLightMode)
            },
            Color(0xFFC9E2FF),
            Position.Top
        ),
        MenuItemInfo(
            stringResource(R.string.setting_darkmode),
            stringResource(R.string.setting_force_to_use_darkmode),
            Icons.Default.DarkMode,
            useDarkMode,
            {
                viewModel.switchModes(useDynamic, !useDarkMode, if(!useDarkMode) false else useLightMode)
            },
            Color(0xFFC9CBFF),
            Position.Mid
        ),
        MenuItemInfo(
            stringResource(R.string.setting_lightmode),
            stringResource(R.string.setting_force_to_use_lightmode),
            Icons.Default.LightMode,
            useLightMode,
            {
                viewModel.switchModes(useDynamic, if(!useLightMode) false else useDarkMode, !useLightMode)
            },
            Color(0xFFFFF0C9),
            Position.Bot
        )
    )

    val sensorMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            stringResource(R.string.setting_attach_virtual_sensor),
            stringResource(R.string.setting_attach_virtual_sensor_for_test),
            Icons.Default.MonitorHeart,
            useVirtual,
            {
                if(useVirtual) {
                    viewModel.disableVirtualSensor()
                } else {
                    viewModel.enableVirtualSensor()
                }
            },
            Color(0xFFC9E2FF),
            Position.Single
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        stickyHeader {
            LazyHeader(stringResource(R.string.setting_set_theme))
        }
        item() {
            LazyLists(appMenus)
        }
        stickyHeader {
            LazyHeader(stringResource(R.string.setting_experimental_feature))
        }
        item() {
            LazyLists(sensorMenus)
        }
    }
}


enum class Position() {
    Top, Mid, Bot, Single
}

data class MenuItemInfo(
    val title: String,
    val description: String,
    val vector: ImageVector,
    val checkedState: Boolean,
    val onClick: () -> Unit,
    val color: Color,
    val position: Position,
)

@Composable
fun LazyHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(title, fontStyle = FontStyle.Italic, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun LazyLists(menus: List<MenuItemInfo>) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        for (menu in menus) {
            LazyList(menu)
        }
    }
}

@Composable
fun LazyList(menu: MenuItemInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if(menu.position != Position.Top && menu.position != Position.Single)
                Divider(
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(end = 12.dp)
                )
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(color = menu.color)
                            .fillMaxSize()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize(0.7f)
                        ) {
                            Image(
                                imageVector = menu.vector,
                                contentDescription = menu.title,
                                colorFilter = ColorFilter.tint(Color(0xFF888888))
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = menu.title, fontWeight = FontWeight.Bold)
                    Text(text = menu.description, overflow = TextOverflow.Ellipsis)
                }
                Switch(checked = menu.checkedState, onCheckedChange = {menu.onClick()})

            }
            if(menu.position != Position.Bot && menu.position != Position.Single)
                Divider(
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(end = 12.dp)
                )
        }
    }
}