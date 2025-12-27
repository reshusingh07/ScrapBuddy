package com.example.scrapuncle.compoents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppToast(
    message: String,
    iconRes: Int? = null,
    duration: Long = 1800L

) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(message) {
        visible = true
        delay(duration)
        visible = false
    }


    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .shadow(6.dp, RoundedCornerShape(14.dp))
                .background(Color.White, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconRes?.let { resId ->
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        tint = Color(0xFF00A651),
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(Modifier.width(15.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(0.75f),
                    )
                }
            }
        }
    }
}


