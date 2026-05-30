package com.manjeet.ubuyapplication.utils

import com.manjeet.ubuyapplication.R

// SORT OPTIONS ENUM
enum class SortOption(val displayName: String) {
    RELEVANCE("Relevance"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low")
}

fun getBrandsForCategory(categoryName: String?): List<Pair<String, Int>> {
    if (categoryName.isNullOrBlank()) return emptyList()

    val brands = when (categoryName.lowercase()) {
        "mobiles" -> listOf(
            "APPLE" to R.drawable.img_33,
            "SAMSUNG" to R.drawable.img_34
        )
        "shoes" -> listOf(
            "NIKE" to R.drawable.img_36,
            "ADIDAS" to R.drawable.img_37,
            "PUMA" to R.drawable.img_38
        )
        "men's fashion", "girl's fashion" -> listOf(
            "FASHION HUB" to R.drawable.img_35
        )
        "electronics & home" -> listOf(
            "SONY" to R.drawable.img_39,
            "LOGITECH" to R.drawable.img_40
        )
        else -> emptyList()
    }

    return if (brands.isNotEmpty()) {
        listOf("All" to R.drawable.ic_all_products) + brands
    } else {
        emptyList()
    }
}