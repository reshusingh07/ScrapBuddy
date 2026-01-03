package com.example.scrapuncle.auth.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.module.AccountSettingsEvent
import com.example.scrapuncle.auth.uistate.AccountSettingsUiState
import com.example.scrapuncle.auth.viewmodel.AccountSettingsViewModel
import com.example.scrapuncle.compoents.SignOutConfirmationDialog
import com.example.scrapuncle.ui.theme.Green80


@Composable
fun AccountSettingsRoute(
    viewModel: AccountSettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
//    onProfileSaved: () -> Unit,
    onSignedOut: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AccountSettingsEvent.ProfileSaved -> {
                    // Maybe show toast or snack bar
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
//                    onProfileSaved()
                }

                AccountSettingsEvent.SignedOut -> {
                    onSignedOut()
                }

                is AccountSettingsEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AccountSettingsScreen(
        uiState = uiState,
        onBack = onBack,
        onFullNameChange = viewModel::onFullNameChanged,
        onEmailChange = viewModel::onEmailChanged,
        onSave = viewModel::onSaveClicked,
        onSignOut = viewModel::onSignOutClicked
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    uiState: AccountSettingsUiState,
    onBack: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSave: () -> Unit,
    onSignOut: () -> Unit
) {



    val keyboardOpen = isKeyboardOpen()
    val borderColor = Color.LightGray.copy(alpha = 0.55f)
    var showSignOutDialog by remember { mutableStateOf(false) }
    val saveColor = if (uiState.isSaveEnabled) Green80 else Color(0xFFBFBFBF)

    val scale by animateFloatAsState(
        targetValue = if (keyboardOpen) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
//            .statusBarsPadding()
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 160.dp
            )

        ) {

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(
                                color = Color.LightGray.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(7.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onBack() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back),
                            contentDescription = "Back",
                            tint = Black.copy(alpha = 0.58f),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Account Settings",
                        fontWeight = FontWeight.SemiBold,
                        color = Black.copy(alpha = 0.92f),
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(28.dp))
            }

            // --- Full Name ---
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("Full Name", fontWeight = FontWeight.Normal, fontSize = 15.sp)

                    OutlinedTextField(
                        value = uiState.fullName,
                        onValueChange = onFullNameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(24.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_profile),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = inputColor(borderColor),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            // --- Email ---
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("Email Address", fontWeight = FontWeight.Normal, fontSize = 15.sp)

                    OutlinedTextField(

                        value = uiState.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(24.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_email),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = inputColor(borderColor),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }
                Spacer(Modifier.height(20.dp))
            }


            // --- Phone Number ---
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("Phone Number", fontWeight = FontWeight.Normal, fontSize = 15.sp)

                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(24.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_phone_cal),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Gray.copy(alpha = 0.7f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.4f),
                            cursorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("Phone number cannot be changed", fontSize = 12.sp, color = Color.Gray)

                Spacer(Modifier.height(32.dp))
            }

            item {
                SignOutCard(onSignOut = { showSignOutDialog = true })
            }

            // --- Bottom Button ---
            item {
                Spacer(modifier = Modifier.height(46.dp))
                FabTransitionAnimation(visible = !keyboardOpen) {
                    //  EXTENDED FAB when keyboard is closed
                    ExtendedFloatingActionButton(
                        onClick = onSave,
                        containerColor = saveColor,
                        contentColor = if (uiState.isSaveEnabled) White else Color.DarkGray,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .imePadding()
                            .align(Alignment.BottomEnd)
                            .fillMaxWidth()
                            .scale(scale)
                            .heightIn(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        text = {
                            Text(
                                if (uiState.isSaving) "Saving…" else "Save",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        },
                        icon = {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    color = White,
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },


                    )
                }
            }
        }
        if (showSignOutDialog) {
            SignOutConfirmationDialog(
                onDismiss = { showSignOutDialog = false },
                onConfirm = {
                    showSignOutDialog = false
                    onSignOut()
                }
            )
        }

        if (keyboardOpen) {
            //  SMALL FAB when keyboard is open
            FloatingActionButton(
                onClick = onSave,
                containerColor = saveColor,
                contentColor = White,
                shape = RoundedCornerShape(50),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = (1.5).dp,
                    pressedElevation = 2.dp
                ),
                modifier = Modifier
                    .imePadding()
                    .align(Alignment.BottomEnd)
                    .size(80.dp)
                    .padding(end = 10.dp, bottom = 12.dp)
                    .scale(scale)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.icon_arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = White.copy(alpha = 1f)
                    )
                }
            }
        }
    }
}



@Composable
fun SignOutCard(onSignOut: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF1F1).copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFFF9C9C).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onSignOut()
            }
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.icon_logout),
                contentDescription = "Logout",
                tint = Color.Red.copy(alpha = 0.75f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Text(
                    text = "Sign Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red.copy(0.85f),
                    lineHeight = 22.sp
                )
                Text(
                    text = "Sign out from this device",
                    fontSize = 13.sp,
                    color = Color.Red.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right_ios),
                contentDescription = "Next",
                tint = Color.Red.copy(alpha = 0.75f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FabTransitionAnimation(visible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 1 },
        exit = fadeOut() + slideOutVertically { it / 1 },
        content = { content() }
    )
}

@Composable
fun inputColor(borderColor: Color) = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    focusedBorderColor = borderColor,
    unfocusedBorderColor = borderColor,
    focusedLabelColor = Black,
    unfocusedLabelColor = Color.Gray,
    cursorColor = Black
)




//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AccountSettingsPreview() {
//    AccountSettingsScreen(
//        initialName = "Reshu Singh",
//        initialEmail = "reshu70173@gmail.com",
//        phoneNumber = "+917017391045",
//        onBack = {},
//        onSave = { name, email ->
//            // Preview only — no action
//        },
//        onSignOut = {}
//    )
//}
