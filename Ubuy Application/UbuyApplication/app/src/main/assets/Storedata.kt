 package com.manjeet.ubuyapplication

import com.google.gson.annotations.SerializedName


data class Storedata (

    @SerializedName("title"      ) var title     : String? = null,
    @SerializedName("url"        ) var url       : String? = null,
    @SerializedName("store_id"   ) var storeId   : Int?    = null,
    @SerializedName("website_id" ) var websiteId : Int?    = null,
    @SerializedName("selected"   ) var selected  : String? = null,
    @SerializedName("store"      ) var store     : String? = null

)