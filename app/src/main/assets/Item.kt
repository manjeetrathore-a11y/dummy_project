 package com.manjeet.ubuyapplication

import com.google.gson.annotations.SerializedName


data class Items (

    @SerializedName("name"    ) var name   : String? = null,
    @SerializedName("image"   ) var image  : String? = null,
    @SerializedName("item_id" ) var itemId : Int?    = null

)