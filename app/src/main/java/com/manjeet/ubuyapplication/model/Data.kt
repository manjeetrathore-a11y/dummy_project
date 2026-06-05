package com.manjeet.ubuyapplication.model

import com.google.gson.annotations.SerializedName
import com.manjeet.ubuyapplication.Storedata

data class Dataa(
    @SerializedName("orders") val orders: List<Order>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("storedata") val storedata: List<Storedata>,
    @SerializedName("order_filter_options") val order_filter_options: OrderFilterOptions
)