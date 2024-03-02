package io.ssafy.mogeun.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val inputForm = viewModel.inputForm
    val firstText = viewModel.firstText
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ) {
            focusManager.clearFocus()
        }
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = firstText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 24.sp
                )
                Text(
                    text = stringResource(R.string.signup_please_enter_it),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }
        when (inputForm) {
            1 -> Essential(viewModel, navController, snackbarHostState)
            2 -> Inbody(navController, viewModel, snackbarHostState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Essential(
    viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val id = viewModel.id
    val password = viewModel.password
    val checkingPassword = viewModel.checkingPassword
    val nickname = viewModel.nickname
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val validIdText = stringResource(R.string.signup_valid_id)
    val duplicateIdText = stringResource(R.string.signup_duplicate_id)
    val duplicateFirstText = stringResource(R.string.signup_duplicate_first)
    val passwordNotMatchText = stringResource(R.string.signup_password_not_match)
    val enterAllInforText = stringResource(R.string.signup_enter_all_information)
    val bodyInfoText = stringResource(R.string.signup_body_information)
    val userInfoText = stringResource(R.string.signup_user_info)

    LaunchedEffect(key1 = viewModel.inputForm) {

        when (viewModel.inputForm)
        {
            1 -> {
                viewModel.updateFirstText(userInfoText)
            }
            else -> {
                viewModel.updateFirstText(bodyInfoText)
            }
        }

    }

    LaunchedEffect(viewModel.checkEmail) {
        if(viewModel.dupEmailSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(validIdText)
                viewModel.updateDupEmailSuccess(false)
            }
        }
        if(viewModel.checkEmail == 2) {
            snackbarHostState.showSnackbar(duplicateIdText)
            viewModel.updateCheckEmail(0)
        }
    }
    LaunchedEffect(viewModel.alertDupEmail) {
        if (viewModel.alertDupEmail) {
            snackbarHostState.showSnackbar(duplicateFirstText)
            viewModel.updateAlertDupEmail(false)
        }
    }
    LaunchedEffect(viewModel.alertPassword) {
        if (viewModel.alertPassword) {
            snackbarHostState.showSnackbar(passwordNotMatchText)
            viewModel.updateAlertPassword(false)
        }
    }
    LaunchedEffect(viewModel.alertInput) {
        if (viewModel.alertInput) {
            snackbarHostState.showSnackbar(enterAllInforText)
            viewModel.updateAlertInput(false)
        }
    }
    Column(
        modifier = Modifier
            .padding(start = 28.dp, top = 28.dp, end = 28.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
    ) {
        Text(text = stringResource(R.string.login_id))
        Row {
            TextField(
                value = id,
                onValueChange = {
                    viewModel.updateId(it)
                    viewModel.updateCheckEmail(0)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    viewModel.dupEmail()
                    keyboardController?.hide()
                },
                modifier = Modifier.width(120.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(R.string.signup_duplicate_verification))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.login_password))
        TextField(
            value = password,
            onValueChange = viewModel::updatePassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.signup_confirm_password))
        TextField(
            value = checkingPassword,
            onValueChange = viewModel::updateCheckingPassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.signup_nickname))
        TextField(
            value = nickname,
            onValueChange = viewModel::updateNickname,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Preview_MultipleRadioButtons()
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "back",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(100.dp)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                                  if (viewModel.checkEmail !== 1) {
                                      viewModel.updateAlertDupEmail(true)
                                  } else if (viewModel.password != viewModel.checkingPassword) {
                                      viewModel.updateAlertPassword(true)
                                  } else if (viewModel.password == "" || viewModel.checkingPassword == "" || viewModel.nickname == "" || viewModel.selectedGender == "") {
                                      viewModel.updateAlertInput(true)
                                  } else {
                                      viewModel.updateInputForm(2)
                                  }
                            },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Text(
                            text = stringResource(R.string.signup_sign_up),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = ""
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Inbody(
    navController: NavHostController,
    viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory),
    snackbarHostState: SnackbarHostState
) {
    var heightText by remember { mutableStateOf(if(viewModel.height == null) "" else viewModel.height.toString()) }
    var weightText by remember { mutableStateOf(if(viewModel.weight == null) "" else viewModel.weight.toString()) }
    var muscleMassText by remember { mutableStateOf(if(viewModel.muscleMass == null) "" else viewModel.muscleMass.toString()) }
    var bodyFatText by remember { mutableStateOf(if(viewModel.bodyFat == null) "" else viewModel.bodyFat.toString()) }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val successSignUpText = stringResource(R.string.signup_success_signup)

    LaunchedEffect(viewModel.successSignUp) {
        if(viewModel.successSignUp) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(successSignUpText)
                viewModel.updateSuccessSignUp(false)
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(28.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(R.string.userInfo_height))
        TextField(
            value = heightText,
            onValueChange = {
                heightText = it
                if (it == "") {
                    viewModel.updateHeight(null)
                } else {
                    viewModel.updateHeight(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(R.string.userInfo_weight))
        TextField(
            value = weightText,
            onValueChange = {
                weightText = it
                if (it == "") {
                    viewModel.updateWeight(null)
                } else {
                    viewModel.updateWeight(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.userInfo_muscle_mass))
        TextField(
            value = muscleMassText,
            onValueChange = {
                muscleMassText = it
                if (it == "") {
                    viewModel.updateMuscleMass(null)
                } else {
                    viewModel.updateMuscleMass(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.userInfo_body_fat))
        TextField(
            value = bodyFatText,
            onValueChange = {
                bodyFatText = it
                if (it == "") {
                    viewModel.updateBodyFat(null)
                } else {
                    viewModel.updateBodyFat(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            maxLines = 1
        )
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.signUp()
                            navController.navigate("login")
                        },
                        modifier = Modifier
                            .width(104.dp)
                            .height(36.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(30.dp)),
                    ) {
                        Text(text = stringResource(R.string.signup_skipping), fontSize = 16.sp)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.signUp()
                            navController.navigate("login")
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Text(
                            text = stringResource(R.string.signup_sign_up),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = ""
        )
    }
}

@Composable
fun Preview_MultipleRadioButtons(viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory)) {
    val selectedGender = viewModel.selectedGender
    val isSelectedItem: (String) -> Boolean = { selectedGender == it }
    val onChangeState: (String) -> Unit = { viewModel.updateSelectedGender(it) }
    val items = listOf("m", "f")
    Column(Modifier.padding(8.dp)) {
        Text(text = stringResource(R.string.signup_choose_gender))
        Row {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedItem(item),
                            onClick = { onChangeState(item) },
                            role = Role.RadioButton
                        )
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = isSelectedItem(item),
                        onClick = null
                    )
                    if(item == "m") {
                        Text(text = stringResource(R.string.signup_male))
                    } else {
                        Text(text = stringResource(R.string.signup_female))
                    }
                }
            }
        }
    }
}