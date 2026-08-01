package com.example.scrapuncle.auth.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.auth.uistate.AuthDestination
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.ui.theme.lightGreen
import com.example.scrapuncle.ui.theme.lightWhite
import com.example.scrapuncle.ui.theme.poppinsCategoryFont
import kotlinx.coroutines.delay


private const val SPLASH_ANIMATION_MILLIS = 2800L


@Composable
fun Splash(
    authViewModel: AuthViewModel,
    onFinished: (AuthDestination) -> Unit
) {


    var startAnimation by remember { mutableStateOf(false) }

    // BOUNCE SCALE animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,  // bounce effect
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "scaleAnim"
    )



    LaunchedEffect(Unit) {
        startAnimation = true

        // Ask on every visit, not once per process. Sign-out lands back here while the
        // Activity-scoped AuthViewModel still holds the previous session's answer.
        authViewModel.refreshSession()

        // Start the lookup first, then spend the animation waiting on it, so the check
        // is usually finished by the time the animation is. Only a genuinely slow lookup
        // holds the splash past its normal length, and it is never cut short into a guess.
        delay(SPLASH_ANIMATION_MILLIS)

        onFinished(authViewModel.awaitAuthDestination())
    }


    SplashContent(scale = scale, isVisible = startAnimation)
}


@Composable
private fun SplashContent(
    scale: Float,
    isVisible: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGreen),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Scrap\nUncle",
            fontSize = 80.sp,
            lineHeight = 70.sp,
            letterSpacing = (-5).sp,
            fontFamily = poppinsCategoryFont,
            fontWeight = FontWeight.Bold,
            color = lightWhite,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (isVisible) 1f else 0f
            }
                .animateContentSize(animationSpec = tween(900))
        )

    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FontTextingPreview() {
    SplashContent(scale = 1f, isVisible = true)
}