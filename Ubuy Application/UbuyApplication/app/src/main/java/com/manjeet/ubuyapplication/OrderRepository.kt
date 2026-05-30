 package com.manjeet.ubuyapplication

 import com.google.gson.annotations.SerializedName

 data class OrderRepository(
     @SerializedName("error") val error: Boolean,
     @SerializedName("code") val code: String,
     @SerializedName("msg") val msg: String,
     @SerializedName("data") val data: Data
 )