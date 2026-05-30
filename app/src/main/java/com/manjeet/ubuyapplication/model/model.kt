package com.manjeet.ubuyapplication.model

data class Order(
    val order_increment_id: String,
    val order_status: String,
    val order_status_en: String?,
    val order_date: String,
    val order_total: String,
    val payment_method: String,
    val shipping_method: String,
    val ship_to: String,
    val full_address: String,
    val items: List<OrderItem>
)

data class OrderItem(
    val name: String,
    val image: String
)