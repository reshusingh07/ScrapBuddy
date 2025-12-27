package com.example.scrapuncle.auth.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import com.example.scrapuncle.ui.theme.lightWhite

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFFA3D78A).copy(alpha = 0.5f),   // start
                        0.01f to Color(0xFFA3D78A).copy(alpha = 0.5f), // hold blue until 10%
                        1f to lightWhite.copy(alpha = 0.6f)          // fade to white
                    )
                )
            )
            .padding(top = 16.dp),
//        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp), // adjust vertical offset
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .padding(top = 8.dp)
                    .aspectRatio(1.2f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.girl_img),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 8.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to ScrapUncle",
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "India's first digital recycling platform",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp, top = 4.dp)

            )

            Text(
                text = "PAPERS | PLASTICS | METALS | E-WASTE",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                        onNavigateToLogin()
                          },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(44.dp),
//                    .padding(26.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3AB34A),
                    contentColor = Color.White
                )

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text = "GET START",
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right),
                        contentDescription = null,
                        tint = White.copy(alpha = 0.9f),
                        modifier = Modifier.size(24.dp)
                    )

                }
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun WelcomeScreenPreview() {
//    WelcomeScreen()
//}

