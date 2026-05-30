 package com.manjeet.ubuyapplication

import com.google.gson.annotations.SerializedName


data class Orders (

    @SerializedName("order_id"                       ) var orderId                      : Int?              = null,
    @SerializedName("order_increment_id"             ) var orderIncrementId             : String?           = null,
    @SerializedName("ship_to"                        ) var shipTo                       : String?           = null,
    @SerializedName("order_date"                     ) var orderDate                    : String?           = null,
    @SerializedName("order_total"                    ) var orderTotal                   : String?           = null,
    @SerializedName("order_status"                   ) var orderStatus                  : String?           = null,
    @SerializedName("order_status_en"                ) var orderStatusEn                : String?           = null,
    @SerializedName("shipment_data"                  ) var shipmentData                 : ArrayList<String> = arrayListOf(),
    @SerializedName("full_address"                   ) var fullAddress                  : String?           = null,
    @SerializedName("shipping_method"                ) var shippingMethod               : String?           = null,
    @SerializedName("address_type"                   ) var addressType                  : String?           = null,
    @SerializedName("payment_method"                 ) var paymentMethod                : String?           = null,
    @SerializedName("consolidation_shipment_message" ) var consolidationShipmentMessage : String?           = null,
    @SerializedName("items"                          ) var items                        : ArrayList<Items>  = arrayListOf()

)