package com.manjeet.ubuyapplication.model

//package com.manjeet.ubuyapplication.data.model

import androidx.compose.ui.graphics.Color
import com.manjeet.ubuyapplication.R

// DATA MODEL
data class Product(
    val name: String,
    val price: String,
    val image: Int,
    val category: String,
    val brand: String = ""
)



data class BrandItem(
    val name: String,
    val productCount: Int,
    val logoRes: Int // The resource ID for the logo
)

val brandList = listOf(
    BrandItem("Samsung", 8, R.drawable.img_34),
    BrandItem("Hub", 12, R.drawable.img_35),
    BrandItem("Apple", 8, R.drawable.img_33),
    BrandItem("Puma", 6, R.drawable.img_38),
    BrandItem("Logitech", 7, R.drawable.img_40),
    BrandItem("Nike", 12, R.drawable.img_36),
    BrandItem("Addidas", 7, R.drawable.img_37),
    BrandItem("Sony", 3, R.drawable.img_39),
    BrandItem("Zara", 11, R.drawable.img_41),

    )


data class DeviceModel(
    val name: String,
    val count: Int,
    val brandLogo: Int // e.g., R.drawable.ic_apple_logo
)

val modelList = listOf(
    DeviceModel("iPhone 17 Pro", 231, R.drawable.img_33),
    DeviceModel("iPhone 17 Pro Max", 198, R.drawable.img_33)
)


data class ColorOption(val name: String, val color: Color)

val colorOptions = listOf(
    ColorOption("White", Color.White),
    ColorOption("Beige", Color(0xFFF5F5DC)),
    ColorOption("Gray", Color.Gray),
    ColorOption("Black", Color.Black),
    ColorOption("Brown", Color(0xFF8B4513)),
    ColorOption("Red", Color.Red),
    ColorOption("Orange", Color(0xFFFFA500)),
    ColorOption("Yellow", Color.Yellow),
    ColorOption("Olive", Color(0xFF808000)),
    ColorOption("Green", Color(0xFF2E7D32)),
    ColorOption("Teal", Color(0xFF008080)),
    ColorOption("Blue", Color.Blue),
    ColorOption("Navy", Color(0xFF000080)),
    ColorOption("Purple", Color(0xFF800080)),
    ColorOption("Pink", Color(0xFFFFC0CB)),
    ColorOption("Silver", Color(0xFFC0C0C0)),
    ColorOption("Gold", Color(0xFFFFD700)),
    ColorOption("Copper", Color(0xFFB87333))
)