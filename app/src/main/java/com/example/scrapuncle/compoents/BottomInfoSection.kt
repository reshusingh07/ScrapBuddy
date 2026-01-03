package com.example.scrapuncle.compoents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import com.example.scrapuncle.ui.theme.InterFontFamily


@Composable
fun BottomInfoSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(42.dp) // You can increase/decrease height as needed
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .background(Color(0xFFE5E5A6)), // Light yellow background
            contentAlignment = Alignment.Center // Centers the text in Box
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                text = "We are currently operational in Moradabad",
                fontSize = 12.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
//                fontWeight = FontWeight.Medium,
                color = Color(0xFF333322).copy(alpha = 0.8f), // Brown color
                textAlign = TextAlign.Center
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "45,39,110 Kg",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 4.dp),
            textAlign = TextAlign.Start
        )
        Text(
            text = "Recycled",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(6.dp))

        Image(
            painter = painterResource(id = R.drawable.scrapuncle_home_banner),
            contentDescription = "Bottom Banner Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomInfoSectionPreview() {
    BottomInfoSection()
}