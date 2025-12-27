package com.example.scrapuncle.auth.ui

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.repo.ProfileUiEvent
import com.example.scrapuncle.auth.uistate.CreateProfileUiState
import com.example.scrapuncle.auth.viewmodel.ProfileViewModel
import com.example.scrapuncle.compoents.SegmentedChips
import com.example.scrapuncle.ui.theme.lightGreen
import kotlinx.coroutines.launch


enum class Gender(val label: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onCreateAccount: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val keyboardOpen = isKeyboardOpen()


    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.submissionInProgress) {
        if (uiState.submissionInProgress) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                ProfileUiEvent.AccountCreated -> {
                    onCreateAccount()
                }

                is ProfileUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(

        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Create Your Profile",
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.9f)
                    )
                },

                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            StickyFab(
                isKeyboardOpen = keyboardOpen,
                loading = uiState.submissionInProgress,
                onClick = viewModel::onSubmit
            )
        }
    ) { padding ->

        ProfileForm(
            uiState = uiState,
            isKeyboardOpen = keyboardOpen,
            padding = padding,
            viewModel = viewModel,
            isLoading = uiState.submissionInProgress
        )
    }
}

@Composable
private fun StickyFab(
    isKeyboardOpen: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    if (!isKeyboardOpen) return

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .imePadding(),
        shape = RoundedCornerShape(50.dp),

        containerColor = Color(0xFF00A651)
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.icon_arrow_right),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
private fun ProfileForm(
    uiState: CreateProfileUiState,
    isKeyboardOpen: Boolean,
    padding: PaddingValues,
    viewModel: ProfileViewModel,
    isLoading: Boolean
) {
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    val nameRequester = remember { BringIntoViewRequester() }
    val emailRequester = remember { BringIntoViewRequester() }
    val pinRequester = remember { BringIntoViewRequester() }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        // --- Full Name ---
        item {
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChanged,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(nameRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            scope.launch {
                                nameRequester.bringIntoView()
                            }
                        }
                    },
                colors = inputColors(),
                label = {
                    Text(
                        "Full name*",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF333333),
                    )
                },
                placeholder = {
                    Text(
                        "Sandy Sin",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF333333).copy(alpha = 0.85f)
                    )
                },
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
        }


        // --- Email ID ---
        item {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(emailRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            scope.launch {
                                emailRequester.bringIntoView()
                            }
                        }
                    },
                label = {
                    Text(
                        "Email ID (optional)",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF333333),
                    )
                },
                placeholder = {
                    Text(
                        "example@gmail.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF333333).copy(alpha = 0.85f)
                    )
                },
                colors = inputColors(),
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
            )
        }

        // --- PinCode ---
        item {
            OutlinedTextField(
                value = uiState.pinCode,
                onValueChange = viewModel::onPinCodeChanged,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn()
                    .bringIntoViewRequester(pinRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            scope.launch {
                                pinRequester.bringIntoView()
                            }
                        }
                    },
                label = {
                    Text(
                        "PIN code",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF333333)
                    )
                },
                placeholder = {
                    Text(
                        "6-digit PIN",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF333333).copy(alpha = 0.85f)
                    )
                },
                colors = inputColors(),
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ))
        }

        // ---------- Gender ----------
        item {
            Text(
                text = "Are you a*",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333).copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            SegmentedChips(
                selected = uiState.gender,
                onSelected = viewModel::onGenderSelected
            )

        }


        // ---------- Checkbox ----------
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = uiState.isChecked,
                        onValueChange = viewModel::onCheckedChanged,
                        indication = null, // removes ripple/highlight
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = uiState.isChecked,
                    onCheckedChange = viewModel::onCheckedChanged,
                    modifier = Modifier.size(16.dp),
                    colors = CheckboxDefaults.colors(checkedColor = lightGreen)
                )
                Text(
                    text = "I have read and accept the Privacy Policy and Conditions of use",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black.copy(alpha = 0.9f),
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(350.dp))
        }

    }
}
