package com.manjeet.ubuyapplication

data class Order(
    val address_type: String,
    val consolidation_shipment_message: String,
    val full_address: String,
    val items: List<Item>,
    val order_date: String,
    val order_id: Int,
    val order_increment_id: String,
    val order_status: String,
    val order_status_en: String,
    val order_total: String,
    val payment_method: String,
    val ship_to: String,
    val shipment_data: List<Any>,
    val shipping_method: String
)