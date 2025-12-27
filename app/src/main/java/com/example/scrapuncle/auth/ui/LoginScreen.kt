package com.example.scrapuncle.auth.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.uistate.AuthState
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.ui.theme.lightBlack
import com.example.scrapuncle.ui.theme.lightGreen


@Composable
fun LoginScreen(
    onNavigateToOtp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val phone by viewModel.phone.collectAsState()

    val activity = LocalContext.current as? Activity


    //  Handle one-time navigation effects

    val isLoading = uiState is AuthState.Loading
    val isButtonEnabled = viewModel.canStartVerification() && !isLoading

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isLoading) {
        if (isLoading) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthState.CodeSent) {
            onNavigateToOtp()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {

        // ✅ Full image, no crop
        Image(
            painter = painterResource(R.drawable.login_img_new),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f), // adjust if needed
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            Spacer(modifier = Modifier.height(120.dp))

//            Text(
//                text = "Log in or sign up",
//                color = Color.DarkGray
//            )
//            Spacer(modifier = Modifier.height(2.dp))


                OutlinedTextField(
                    value = phone,
                    onValueChange = viewModel::onPhoneChanged,
                    enabled = !isLoading,
                    singleLine = true,
                    label = {
                        Text(
                            "Phone number",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF333333)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(36.dp),
                    shape = MaterialTheme.shapes.small,
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = "+91",
                                fontSize = 15.sp,
                                color = lightBlack,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(24.dp)
                                    .background(Color.LightGray)
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Gray,
                        disabledBorderColor = Color.LightGray,
                        disabledLabelColor = Color.Gray
                    )
                    ,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

            //  Error text under field
            if (uiState is AuthState.Error) {
                Text(
                    text = (uiState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    activity?.let {
                    viewModel.startPhoneVerification(it)
                }},
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .heightIn(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightGreen,
                    disabledContainerColor = lightGreen
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White.copy(alpha = 0.9f),
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text(
                        text = "Continue",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "By continuing, you agree to our Terms & Privacy\nPolicy.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

        }
    }
}



