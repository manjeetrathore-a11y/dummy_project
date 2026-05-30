 package com.manjeet.ubuyapplication

import com.google.gson.annotations.SerializedName


data class OrderFilterOptions (

    @SerializedName("pending"    ) var pending    : String? = null,
    @SerializedName("processing" ) var processing : String? = null,
    @SerializedName("complete"   ) var complete   : String? = null,
    @SerializedName("canceled"   ) var canceled   : String? = null,
    @SerializedName("closed"     ) var closed     : String? = null,
    @SerializedName("holded"     ) var holded     : String? = null

)