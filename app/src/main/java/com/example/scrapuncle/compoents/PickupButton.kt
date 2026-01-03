package com.example.scrapuncle.compoents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.ui.theme.InterFontFamily
import com.example.scrapuncle.ui.theme.lightGreen


@Composable
fun PickupButton(onClick: () ->Unit) {

    Button( onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp, horizontal = 22.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 6.dp
        ),

        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = lightGreen,
            contentColor = Color.White
        )

    ) {
        Text(text = "SCHEDULE A PICKUP",
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
        )
    }

}
