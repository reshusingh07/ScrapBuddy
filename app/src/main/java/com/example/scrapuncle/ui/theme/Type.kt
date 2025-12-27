package com.example.scrapuncle.ui.theme



import com.example.scrapuncle.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val poppinsCategoryFont = FontFamily(

    Font(R.font.poppins_black, weight = FontWeight.Black),
    Font(R.font.poppins_black_italic, weight = FontWeight.Black),
    Font(R.font.poppins_bold, weight = FontWeight.Bold),
    Font(R.font.poppins_bold_italic, weight = FontWeight.Bold),
    Font(R.font.poppins_light, weight = FontWeight.Light),
)