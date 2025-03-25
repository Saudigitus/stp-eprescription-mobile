package org.saudigitus.e_prescription.presentation.screens.login


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Http
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.components.AppSnackbarHost


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSync: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = {
            AppSnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 32.dp, top = 54.dp, end = 32.dp, bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ms_stp_logo),
                contentDescription = null,
                alignment = Alignment.TopStart,
                modifier = Modifier.size(200.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                )
                Spacer(modifier = Modifier.size(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.45f)
                        .height(5.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(0.25f),
                            shape = RoundedCornerShape(32.dp)
                        )
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = stringResource(R.string.login_info),
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                )
            }

            TextField(
                value = loginUiState.serverUrl,
                onValueChange = viewModel::serverUrl,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loginUiState.isLoading,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Http, contentDescription = null)
                },
                trailingIcon = {
                    if (!loginUiState.isServerUrlValid()) {
                        Icon(
                            Icons.Filled.Error,
                            stringResource(R.string.error_wrong_server_url),
                            tint = Color.Red
                        )
                    }
                },
                isError = loginUiState.isServerUrlValid(),
                label = {
                    Text(text = stringResource(R.string.server_url))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = loginUiState.username,
                onValueChange = viewModel::username,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loginUiState.isLoading,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                },
                trailingIcon = {
                    if (!loginUiState.isUserNameValid()) {
                        Icon(
                            Icons.Filled.Error,
                            stringResource(R.string.invalid_username),
                            tint = Color.Red
                        )
                    }
                },
                isError = loginUiState.isUserNameValid(),
                label = {
                    Text(text = stringResource(R.string.username))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = loginUiState.password,
                onValueChange = viewModel::password,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loginUiState.isLoading,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Password, contentDescription = null)
                },
                trailingIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!loginUiState.isPasswordValid()) {
                            Icon(
                                Icons.Filled.Error,
                                stringResource(R.string.invalid_password),
                                tint = Color.Red
                            )
                        }
                        val icon = if (loginUiState.passwordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }

                        IconButton(onClick = {
                            viewModel.passwordVisibility(!loginUiState.passwordVisible)
                        }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    }
                },
                isError = loginUiState.isPasswordValid(),
                label = {
                    Text(text = stringResource(R.string.password))
                },
                visualTransformation = if (loginUiState.passwordVisible) {
                    VisualTransformation.None
                } else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            AnimatedVisibility(
                visible = !loginUiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = viewModel::login,
                    modifier = Modifier.fillMaxWidth()
                        .height(54.dp),
                    enabled = loginUiState.enableLoginBtn(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Login,
                            contentDescription = stringResource(R.string.login)
                        )
                        Text(text = stringResource(R.string.login))
                    }
                }
            }

            AnimatedVisibility(
                visible = loginUiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }
        }
        LaunchLoginResult(
            loginResult = viewModel.loginResult,
            scope,
            snackbarHostState = snackbarHostState,
            onNavigateToSync
        )
    }
}

@Composable
fun LaunchLoginResult(
    loginResult: SharedFlow<LoginResult>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onNavigateToSync: () -> Unit
) {
    LaunchedEffect(Unit) {
        loginResult.collectLatest {
            if (it.success != null) {
                onNavigateToSync.invoke()
            } else if (it.error != null) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = it.error
                    )
                }
            }
        }
    }
}
