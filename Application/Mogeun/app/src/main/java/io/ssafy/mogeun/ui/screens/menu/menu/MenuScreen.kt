package io.ssafy.mogeun.ui.screens.menu.menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val withdrawalText = stringResource(R.string.menu_withdrawal_complete)
    val invalidInfoText = stringResource(R.string.userInfo_invalid_information)
    LaunchedEffect(viewModel.deleteUserSuccess) {
        if (viewModel.deleteUserSuccess == true) {
            navController.navigate("Splash")
            snackbarHostState.showSnackbar(withdrawalText)
        }
    }
    LaunchedEffect(viewModel.errorDeleteUser) {
        if (viewModel.errorDeleteUser == true) {
            snackbarHostState.showSnackbar(invalidInfoText)
        }
    }

    var exitCnt = 0
    val activity = (LocalContext.current as? Activity)
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if(exitCnt == 0) {
            exitCnt++
            coroutineScope.launch {
                snackbarHostState.showSnackbar("한번 더 누르면 앱이 종료됩니다.")
            }
        } else {
            activity?.finish()
        }
    }

    val userMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            stringResource(R.string.userInfo_modify_information),
            stringResource(R.string.menu_modify_personal_information),
            Icons.Default.ManageAccounts,
            {navController.navigate("User")},
            Color(0xFFFFF0C9),
            Position.Top
        ),
        MenuItemInfo(
            stringResource(R.string.menu_logout),
            stringResource(R.string.menu_logout_service),
            Icons.Default.Logout,
            {
                viewModel.deleteUserKey()
                navController.navigate("Splash")
            },
            Color(0xFFFFE0C9),
            Position.Mid
        ),
        MenuItemInfo(
            stringResource(R.string.menu_membership_withdrawal),
            stringResource(R.string.menu_want_stop_service),
            Icons.Default.GroupOff,
            {openAlertDialog.value = true},
            Color(0xFFFFC9C9),
            Position.Bot
        )
    )

    val serviceMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            stringResource(R.string.menu_interworking_equipment),
            stringResource(R.string.menu_connect_device),
            Icons.Default.BluetoothSearching,
            {navController.navigate("Connection")},
            Color(0xFFC9E2FF),
            Position.Single
        )
    )

    val appMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            stringResource(R.string.menu_google_assessment),
            stringResource(R.string.menu_leave_review),
            Icons.Default.RateReview,
            {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=io.ssafy.mogeun"))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                MogeunApplication.getContext().startActivity(browserIntent)
            },
            Color(0xFFC9CBFF),
            Position.Top
        ),
        MenuItemInfo(
            stringResource(R.string.app_setting),
            stringResource(R.string.menu_theme_sensor),
            Icons.Default.Settings,
            {
                navController.navigate(Screen.Setting.route)
            },
            Color(0xFFEAC9FF),
            Position.Mid
        ),
        MenuItemInfo(
            stringResource(R.string.app_information),
            stringResource(R.string.menu_version_contact),
            Icons.Default.Info,
            {
                navController.navigate(Screen.AppInfo.route)
            },
            Color(0xFFFFC9E3),
            Position.Bot
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        stickyHeader {
            LazyHeader(stringResource(R.string.userInfo_member_information))
        }
        item() {
            LazyLists(userMenus)
        }
        stickyHeader {
            LazyHeader(stringResource(R.string.menu_service_interworking))
        }
        item() {
            LazyLists(serviceMenus)
        }
        stickyHeader {
            LazyHeader(stringResource(R.string.app_information))
        }
        item() {
            LazyLists(appMenus)
        }
    }



    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    viewModel.deleteUser()
                    openAlertDialog.value = false
                },
                dialogTitle = stringResource(R.string.menu_enter_membership_information),
                icon = Icons.Default.Info
            )
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
            .clickable {
                menu.onClick()
            }
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
                    Text(text = menu.description)
                }

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Text(text = stringResource(R.string.login_id))
                TextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.updateId(it) },
                    label = {  },
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.login_password))
                TextField(
                    value = viewModel.pw,
                    onValueChange = { viewModel.updatePw(it) },
                    label = {  },
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(stringResource(R.string.menu_withdrawal))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.cancellation))
            }
        }
    )
}