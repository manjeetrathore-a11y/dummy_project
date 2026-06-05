package com.manjeet.ubuyapplication

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("orders") val orderss: List<Order>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("storedata") val storedata: List<Storedata>,
    @SerializedName("order_filter_options") val order_filter_options: OrderFilterOptions
)