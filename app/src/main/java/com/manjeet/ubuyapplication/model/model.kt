package com.manjeet.ubuyapplication.model
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("order_increment_id") val order_increment_id: String,
    @SerializedName("order_status") val order_status: String,
    @SerializedName("order_status_en") val order_status_en: String?,
    @SerializedName("order_date") val order_date: String,
    @SerializedName("order_total") val order_total: String,
    @SerializedName("payment_method") val payment_method: String,
    @SerializedName("shipping_method") val shipping_method: String,
    @SerializedName("ship_to") val ship_to: String,
    @SerializedName("full_address") val full_address: String,
    @SerializedName("items") val items: List<Item>, // Strictly OrderItem ki list handles karega

    //  ADDED MISSING PARAMETERS (Jinki wajah se screens error de rahi thi)
    @SerializedName("order_id") val order_id: Int = 0,
    @SerializedName("address_type") val address_type: String = "",
    @SerializedName("consolidation_shipment_message") val consolidation_shipment_message: String = "",
    @SerializedName("shipment_data") val shipment_data: List<String> = emptyList()
)

data class OrderItem(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
)