package com.manjeet.ubuyapplication.model

import androidx.compose.ui.graphics.Color

// --- DATA CLASS ---

data class OrderData(
    val date: String,
    val id: String,
    val status: String,
    val price: String,
    val statusBg: Color,
    val statusColor: Color,
    val productImages: List<Int>,
)
