package com.manjeet.ubuyapplication.model

data class OrderFilterOptions(
    val canceled: String,
    val closed: String,
    val complete: String,
    val holded: String,
    val pending: String,
    val processing: String
)