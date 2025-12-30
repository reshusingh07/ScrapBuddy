package com.example.scrapuncle.auth.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
 import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.uistate.AuthState
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.auth.viewmodel.OtpViewModel
import com.example.scrapuncle.ui.theme.lightBlack
import com.example.scrapuncle.ui.theme.lightGreen
import com.example.scrapuncle.ui.theme.poppinsCategoryFont
import kotlin.math.roundToInt


const val OTP_LENGTH = 6


@Composable
fun OtpScreen(
    authViewModel: AuthViewModel,
    onNavigateToCreateProfile: () -> Unit,
    onBack: () -> Unit
) {


    val authState by authViewModel.uiState.collectAsState()
    val otpViewModel: OtpViewModel = hiltViewModel()
    val uiState by otpViewModel.uiState.collectAsState()

    val isLoading = uiState.isLoading || authState is AuthState.Loading






    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onNavigateToCreateProfile()
                authViewModel.clearError()
            }

            is AuthState.Error -> {
                // 🔥 THIS IS MISSING
                otpViewModel.onAuthError((authState as AuthState.Error).message)
            }

            else -> Unit
        }
    }





    Scaffold(
        containerColor = Color(0xFFFFFFFF)
    ) { padding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                // BACK BUTTON
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            color = Color.LightGray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(7.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBack()},
//                    contentAlignment = Alignment.S,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_arrow_back),
                        contentDescription = "Back",
                        tint = Black.copy(alpha = 0.58f),
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.delivery_cycle),
                contentDescription = "Delivery Cycle Boy",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )


            Spacer(Modifier.height(8.dp))

            Text(
                text = "Verification",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.75f)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Enter your OTP code number",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            ShakeWrapper(trigger = (uiState.error != null)) { modifier ->
                // OTP BOXES
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                ) {
                    OtpInput(
                        otp = uiState.otp,
                        isError = uiState.error != null,
                        isEnabled = !isLoading,
                        onOtpChange = otpViewModel::onOtpChange
                    )

                }
            }
            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {
                    otpViewModel.submitOtp { code ->
                        authViewModel.verifyCode(code)
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .heightIn(44.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightGreen,
                    disabledContentColor = lightGreen
                )
            ) {
                if (uiState.isLoading || authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White.copy(alpha = 0.9f),
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "Verify",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Didn't you receive any code?",
                fontFamily = poppinsCategoryFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Resend Code",
                color = lightGreen,
                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.clickable {
//                    viewModel.resendCode(phone,)
//                }
            )
        }

        // ViewModel-driven errors
//        otpUiState.error?.let {
//            ErrorDialog(
//                message = it,
//                onDismiss = { otpViewModel.clearError() }
//            )
//        }
    }
}


@Composable
fun OtpInput(
    otp: String,
    isError: Boolean,
    isEnabled: Boolean,
    onOtpChange: (String) -> Unit
) {
    Box {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(OTP_LENGTH) { index ->
                OtpBox(
                    value = otp.getOrNull(index)?.toString() ?: "",
                    isFocused = otp.length == index,
                    isError = isError
                )
            }
        }

        // REAL input (hidden)
        BasicTextField(
            value = otp,
            onValueChange = onOtpChange,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
        )
    }
}


@Composable
fun OtpBox(
    value: String,
    isFocused: Boolean,
    isError: Boolean
) {
    val borderColor = when {
        isError -> Color.Red
        isFocused -> lightGreen
        value.isNotEmpty() -> lightBlack
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun ShakeWrapper(
    trigger: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    (-12f) at 50
                    12f at 100
                    (-8f) at 150
                    8f at 200
                    (-4f) at 250
                    4f at 300
                    0f at 350
                }
            )
        }
    }

    content(
        Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
    )
}

