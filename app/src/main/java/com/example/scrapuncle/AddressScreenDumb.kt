//package com.example.scrapuncle
//
//import android.widget.Toast
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.spring
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.relocation.BringIntoViewRequester
//import androidx.compose.foundation.relocation.bringIntoViewRequester
//import androidx.compose.foundation.selection.toggleable
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.ripple.rememberRipple
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.FloatingActionButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.LargeTopAppBar
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion.Black
//import androidx.compose.ui.graphics.Color.Companion.White
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardCapitalization
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.scrapuncle.auth.module.ScheduleEvent
//import com.example.scrapuncle.auth.repo.ProfileUiEvent
//import com.example.scrapuncle.auth.ui.inputColors
//import com.example.scrapuncle.auth.ui.isKeyboardOpen
//import com.example.scrapuncle.auth.uistate.AddAddressUiState
//import com.example.scrapuncle.auth.uistate.CreateProfileUiState
//import com.example.scrapuncle.auth.viewmodel.AddAddressViewModel
//import com.example.scrapuncle.auth.viewmodel.ProfileViewModel
//import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
//import com.example.scrapuncle.ui.theme.lightGreen
//import kotlinx.coroutines.launch
//
//
//enum class AddressAs(val label: String) {
//    MALE("Male"),
//    FEMALE("Female"),
//    OTHER("Other")
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateProfileScreen1(
//    viewModel: AddAddressViewModel = hiltViewModel(),
//    scheduleViewModel: ScheduleViewModel,
//    onSchedulePickup: () -> Unit = {}
//) {
//
//    val uiState = viewModel.uiState.collectAsState()
//    val state = uiState.value
//    val context = LocalContext.current
//
//
//    val keyboardOpen = isKeyboardOpen()
//
//
//
//    LaunchedEffect(state.error) {
//        state.error?.let { msg ->
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//            viewModel.clearError()
//        }
//    }
//
//
//    LaunchedEffect(state.saveSuccess) {
//        val newAddressId = viewModel.uiState.value.addresses.lastOrNull()?.id
//
//        if (state.saveSuccess == true) {
//
//            // IMPORTANT: refresh address list inside ScheduleViewModel
////            scheduleViewModel.reloadAddresses()
//
//            // Select this new address as current address for pickup
//            if (newAddressId != null) {
//                scheduleViewModel.onEvent(
//                    ScheduleEvent.SelectAddress(newAddressId)
//                )
//            }
//
//            onSchedulePickup()
//            viewModel.clearSaveResult()
//        }
//    }
//
//
//    val scale by animateFloatAsState(
//        targetValue = if (keyboardOpen) 0.85f else 1f,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        ),
//        label = ""
//    )
//
//    val scrollBehavior =
//        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
//
//    Scaffold(
//
//        modifier = Modifier
//            .nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            LargeTopAppBar(
//                title = {
//                    Text(
//                        text = "Add Address Details",
//                        fontWeight = FontWeight.Medium,
//                        color = Color.Black.copy(alpha = 0.9f)
//                    )
//                },
//                navigationIcon = {
//                    Surface(
//                        onClick = { onSchedulePickup() },
//                        shape = RoundedCornerShape(7.dp),
//                        color = Color.LightGray.copy(alpha = 0.1f),
////                        tonalElevation = 0.dp,
////                        shadowElevation = 0.dp
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .padding(2.dp)
//                                .size(26.dp),
//                            contentAlignment = Alignment.Center,
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.icon_arrow_back),
//                                contentDescription = "Back",
//                                tint = Black.copy(alpha = 0.58f),
//                            )
//                        }
//                    }
//                },
//                scrollBehavior = scrollBehavior,
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = White,
//                    scrolledContainerColor = White
//                )
//            )
//        },
//        floatingActionButton = {
//            StickyFab(
//                isKeyboardOpen = keyboardOpen,
//                loading = state.isSaving,
//                onClick = viewModel::saveAddress
//            )
//        }
//    ) { padding ->
//
//        ProfileForm(
//            uiState = state,
//            isKeyboardOpen = keyboardOpen,
//            padding = padding,
//            viewModel = viewModel
//        )
//    }
//}
//
//@Composable
//private fun StickyFab(
//    isKeyboardOpen: Boolean,
//    loading: Boolean,
//    onClick: () -> Unit
//) {
//    if (!isKeyboardOpen) return
//
//    FloatingActionButton(
//        onClick = onClick,
//        modifier = Modifier
//            .imePadding(),
//        shape = RoundedCornerShape(50),
//        contentColor = White,
//        containerColor = Color(0xFF00A651),
//        elevation = FloatingActionButtonDefaults.elevation(
//            defaultElevation = (1.5).dp,
//            pressedElevation = 2.dp
//        ),
//    ) {
//        if (loading) {
//            CircularProgressIndicator(
//                color = White,
//                strokeWidth = 2.dp,
//                modifier = Modifier.size(22.dp)
//            )
//        } else {
//            Icon(
//                painter = painterResource(R.drawable.icon_arrow_right),
//                contentDescription = null,
//                tint = White,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//    }
//}
//
//
//@Composable
//private fun ProfileForm(
//    uiState: AddAddressUiState,
//    isKeyboardOpen: Boolean,
//    padding: PaddingValues,
//    viewModel: AddAddressViewModel
//) {
//    val focusManager = LocalFocusManager.current
//    val bringIntoViewRequester = remember { BringIntoViewRequester() }
//    val scope = rememberCoroutineScope()
//
//    val nameRequester = remember { BringIntoViewRequester() }
//    val emailRequester = remember { BringIntoViewRequester() }
//    val pinRequester = remember { BringIntoViewRequester() }
//
//
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(padding)
//            .padding(horizontal = 24.dp),
//        verticalArrangement = Arrangement.spacedBy(18.dp),
//        contentPadding = PaddingValues(bottom = 24.dp)
//    ) {
//
//        // --- Full Address ---
//        item {
//            OutlinedTextField(
//                value = uiState.fullAddress,
//                onValueChange = viewModel::onAddressChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .bringIntoViewRequester(nameRequester)
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            scope.launch {
//                                nameRequester.bringIntoView()
//                            }
//                        }
//                    },
//                colors = inputColors(),
//                label = {
//                    Text(
//                        "Full Address",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color(0xFF333333),
//
//                        )
//                },
//                shape = MaterialTheme.shapes.small,
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next,
//                    capitalization = KeyboardCapitalization.Words
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                ),
//            )
//        }
//
//
//        // --- Landmark ---
//        item {
//            OutlinedTextField(
//                value = uiState.landmark,
//                onValueChange = viewModel::onLandmarkChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .bringIntoViewRequester(emailRequester)
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            scope.launch {
//                                emailRequester.bringIntoView()
//                            }
//                        }
//                    },
//                colors = inputColors(),
//                label = {
//                    Text(
//                        "Landmark (optional)",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color(0xFF333333),
//                    )
//                },
//                shape = MaterialTheme.shapes.small,
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next,
//                    capitalization = KeyboardCapitalization.Words
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                ),
//            )
//        }
//
//        // --- City ---
//        item {
//            OutlinedTextField(
//                value = uiState.city,
//                onValueChange = viewModel::onCityChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .bringIntoViewRequester(nameRequester)
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            scope.launch {
//                                nameRequester.bringIntoView()
//                            }
//                        }
//                    },
//                colors = inputColors(),
//                label = {
//                    Text(
//                        "City*",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color(0xFF333333),
//                    )
//                },
//                shape = MaterialTheme.shapes.small,
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next,
//                    capitalization = KeyboardCapitalization.Words
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                )
//            )
//        }
//
//        // --- State ---
//        item {
//            OutlinedTextField(
//                value = uiState.state,
//                onValueChange = viewModel::onStateChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .bringIntoViewRequester(nameRequester)
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            scope.launch {
//                                nameRequester.bringIntoView()
//                            }
//                        }
//                    },
//                colors = inputColors(),
//                label = {
//                    Text(
//                        "State*",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color(0xFF333333),
//                    )
//                },
//                shape = MaterialTheme.shapes.small,
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next,
//                    capitalization = KeyboardCapitalization.Words
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                )
//            )
//        }
//
//
//        // --- PinCode ---
//        item {
//            OutlinedTextField(
//                value = uiState.pinCode,
//                onValueChange = viewModel::onPinCodeChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .bringIntoViewRequester(pinRequester)
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            scope.launch {
//                                pinRequester.bringIntoView()
//                            }
//                        }
//                    },
//
//                colors = inputColors(),
//                label = {
//                    Text(
//                        "Pin Code*",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = Color(0xFF333333),
//                    )
//                },
//
//                shape = MaterialTheme.shapes.small,
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done,
//                    capitalization = KeyboardCapitalization.Words
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                )
//            )
//        }
//        // ---------- Gender ----------
//        item {
//            Text(
//                text = "Are you a*",
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium,
//                color = Color(0xFF333333).copy(alpha = 0.8f)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
////            SegmentedChips(
////                selected = uiState.gender,
////                onSelected = { viewModel.onGenderSelected(it) }
////            )
//
//        }
//
//
//        // ---------- Checkbox ----------
//
//        item {
//            Spacer(modifier = Modifier.height(350.dp))
//        }
//
//    }
//}
