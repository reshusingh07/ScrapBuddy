package com.example.scrapuncle.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import com.example.scrapuncle.ui.theme.lightGreen
import com.example.scrapuncle.ui.theme.lightWhite
import com.example.scrapuncle.ui.theme.poppinsCategoryFont
import com.google.common.io.Files.append

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE6C9).copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.4f)
                    )
                )
            )
            .padding(horizontal = 20.dp)
    ) {

        // 🔥 HERO IMAGE (dominant)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 340.dp, max = 420.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.delivery_boy),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
        }

        // 🔥 TEXT BLOCK
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Sell your recyclables\nonline with ")

                    withStyle(
                        style = SpanStyle(
                            color = lightGreen, // green (change if needed)
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("ScrapUncle!")
                    }
                },
                fontFamily = poppinsCategoryFont,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Paper - Plastics - Metals - Appliances",
                fontSize = 14.sp,
                fontFamily = poppinsCategoryFont,
                fontWeight = FontWeight.Medium,
                color = Color.Gray.copy(alpha = 0.9f)
            )
        }


        // 🔥 CTA BUTTON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .weight(0.18f),
//            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(44.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightGreen
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen{}
}

