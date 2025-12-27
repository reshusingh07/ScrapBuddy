package com.example.scrapuncle.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R


@Composable
fun RateScreen() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(getCategoryList()) { item ->
            RateItemCard(
                img = item.img,
                category = item.category,
                price = item.price
            )
        }

    }
}

@Composable
fun RateItemCard(
    img: Int,
    category: String,
    price: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* future details */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB).copy(alpha = 0.45f)
        ),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))

    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(img),
                    contentDescription = category,
                    modifier = Modifier.size(30.dp)
                )
            }

    Spacer(Modifier.width(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black.copy(alpha = 0.78f)
                    )
                    Text(
                        text = "Per unit",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                PriceChip(price)
            }

        }
    }
}

@Composable
fun RateHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Check Rates",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.8f)
        )
        Text(
            text = "Updated scrap prices near you",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}


@Composable
fun PriceChip(price: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE6F4EA),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)

    ) {
        Text(
            text = price,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF1B5E20),
            fontWeight = FontWeight.Medium
        )
    }
}


data class Category(val img: Int, val category: String, val price: String)


fun getCategoryList(): List<Category> = listOf(
    Category(R.drawable.newspaper, "Newspaper", "₹14 / KG"),
    Category(R.drawable.steel, "Iron", "₹26 / KG"),
    Category(R.drawable.plastic, "Other Plastic", "₹12 / KG"),
    Category(R.drawable.copies_book, "Copies / Book", "₹12 / KG"),
    Category(R.drawable.box, "Cardboard", "₹8 / KG"),
    Category(R.drawable.brand, "Clothes", "₹4 / KG"),
    Category(R.drawable.brass, "Brass", "₹305 / KG"),
    Category(R.drawable.briefcase, "Office Paper", "₹14 / KG"),
    Category(R.drawable.can, "Aluminium", "₹105 / KG"),
    Category(R.drawable.copper, "Copper", "₹425 / KG"),
    Category(R.drawable.fish_hook, "Steel Utensils", "RS 40/KG"),
    Category(R.drawable.glass_bottle, "Glass Bottles", "RS 8/KG"),

    Category(R.drawable.inverter, "Inverter/Stabilizer", "RS 81/KG"),

    Category(R.drawable.computer_cpu, "Computer CPU", "RS 225/PIECE"),
    Category(R.drawable.computer, "LCD Monitor", "RS 30/KG"),
    Category(R.drawable.scrap_laptop, "Scrap Laptop", "RS 300/PIECE"),
    Category(R.drawable.battery, "Battery", "RS 81/KG"),
    Category(R.drawable.microwave, "Microwave", "RS 350/PIECE"),
    Category(R.drawable.motor, "Motor(Copper Coil", "RS 35/KG"),
    Category(R.drawable.ceiling_fan, "Ceiling Fan", "RS 38/KG"),
    Category(R.drawable.printer, "Printer/Scanner/Fax Machine", "RS 42/KG"),

    Category(R.drawable.cooler, "Iron Cooler", "RS 30/KG"),
    Category(R.drawable.cooler, "Plastic Cooler", "RS 15/KG"),

    Category(R.drawable.fridge, "Single Door Fridge", "RS 1100/PIECE"),
    Category(R.drawable.fridge, "Double Door Fridge", "RS 1300/PIECE"),

    Category(R.drawable.air_conditioner, "Split AC Copper Coil 1.5 TON", "RS 4150/PIECE"),
    Category(R.drawable.air_conditioner, "Split/Window AC 1 TON", "RS 3000/PIECE"),
    Category(R.drawable.air_conditioner, "Window/Split AC 2 TON", "RS 5600/PIECE"),
    Category(R.drawable.motorbike, "Bike", "RS 2100/PIECE")

)


