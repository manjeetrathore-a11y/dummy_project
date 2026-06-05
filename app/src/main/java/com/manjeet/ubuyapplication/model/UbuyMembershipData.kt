package com.manjeet.ubuyapplication.model

import com.google.gson.annotations.SerializedName

data class UbuyMembershipData (
    @SerializedName("error" ) var error : Boolean? = null,
    @SerializedName("code"  ) var code  : String?  = null,
    @SerializedName("msg"   ) var msg   : String?  = null,
    @SerializedName("data"  ) var data  : Data?    = Data()
)

data class Data(
    @SerializedName("shipping_section" ) var shippingSection : ShippingSection? = ShippingSection(),
    @SerializedName("cashback_section" ) var cashbackSection : CashbackSection? = CashbackSection(),
    @SerializedName("savings_section"  ) var savingsSection  : SavingsSection?  = SavingsSection()
)

data class ShippingSection (
    @SerializedName("headings"   ) var headings  : String?    = null,
    @SerializedName("table_data" ) var tableData : TableData? = TableData(),
    @SerializedName("filers"     ) var filers    : Filers?    = Filers(),
    @SerializedName("graph"      ) var graph     : Graph?     = Graph()
)

data class CashbackSection (
    @SerializedName("headings"   ) var headings  : String?    = null,
    @SerializedName("table_data" ) var tableData : TableData? = TableData(),
    @SerializedName("filers"     ) var filers    : Filers?    = Filers(),
    @SerializedName("graph"      ) var graph     : Graph?     = Graph()
)

// 🚀 SAVINGS SECTION (Jo aapki pehle missing thi ya incomplete thi)
data class SavingsSection(
    @SerializedName("total_savings") var totalSavings: TotalSavings? = TotalSavings(),
    @SerializedName("shipping_savings") var shippingSavings: ShippingSavings? = ShippingSavings()
)

data class TotalSavings (
    @SerializedName("heading"     ) var heading    : String? = null,
    @SerializedName("sub_heading" ) var subHeading : String? = null,
    @SerializedName("currency"    ) var currency   : String? = null,
    @SerializedName("amount"      ) var amount     : String? = null
)

data class ShippingSavings (
    @SerializedName("heading"     ) var heading    : String? = null,
    @SerializedName("sub_heading" ) var subHeading : String? = null,
    @SerializedName("currency"    ) var currency   : String? = null,
    @SerializedName("amount"      ) var amount     : String? = null
)

data class TableData (
    @SerializedName("heading" ) var heading : Heading?          = Heading(),
    @SerializedName("values"  ) var values  : ArrayList<TxnValue> = arrayListOf() // 🚀 Fixed: Name changed to TxnValue to avoid duplicate
)

data class Heading (
    @SerializedName("txn_type"      ) var txnType      : String? = null,
    @SerializedName("txn_details"   ) var txnDetails   : String? = null,
    @SerializedName("status"        ) var status       : String? = null,
    @SerializedName("credit_amount" ) var creditAmount : String? = null,
    @SerializedName("txn_date_time" ) var txnDateTime  : String? = null
)

// 🚀 Fixed: Iska naam TxnValue kar diya taaki doosri class se takraye nahi
data class TxnValue (
    @SerializedName("txn_type"      ) var txnType      : String? = null,
    @SerializedName("order_number"  ) var orderNumber  : String? = null,
    @SerializedName("status"        ) var status       : String? = null,
    @SerializedName("credit_amount" ) var creditAmount : String? = null,
    @SerializedName("txn_date_time" ) var txnDateTime  : String? = null
)

data class Filers (
    @SerializedName("options"           ) var options          : Options? = Options(),
    @SerializedName("selected_timeline" ) var selectedTimeline : String?  = null,
    @SerializedName("selected_label"    ) var selectedLabel    : String?  = null
)

// 🚀 Fixed: Numbers directly variable name nahi ban sakte, isliye text format use kiya h
data class Options (
    @SerializedName("2025" ) var year2025 : String? = null,
    @SerializedName("2026" ) var year2026 : String? = null,
    @SerializedName("all"  ) var all      : String? = null
)

data class Graph (
    @SerializedName("heading"     ) var heading    : String?             = null,
    @SerializedName("sub_heading" ) var subHeading : String?             = null,
    @SerializedName("datasets"    ) var datasets   : ArrayList<Datasets> = arrayListOf()
)

data class Datasets (
    @SerializedName("lables" ) var lables : String? = null,
    @SerializedName("color"  ) var color  : String? = null,
    @SerializedName("values" ) var values : MonthlyValues? = MonthlyValues() // 🚀 Fixed: Unique name given
)

// 🚀 Fixed: Iska naam MonthlyValues kiya aur variables ke spaces hataye hain
data class MonthlyValues (
    @SerializedName("Apr 2026" ) var apr2026 : Double? = null,
    @SerializedName("Mar 2026" ) var mar2026 : Double? = null,
    @SerializedName("Feb 2026" ) var feb2026 : Double? = null,
    @SerializedName("Jan 2026" ) var jan2026 : Double? = null,
    @SerializedName("Dec 2025" ) var dec2025 : Double? = null,
    @SerializedName("Nov 2025" ) var nov2025 : Double? = null,
    @SerializedName("Sep 2025" ) var sep2025 : Double? = null,
    @SerializedName("Oct 2024" ) var oct2024 : Double? = null,
    @SerializedName("Jul 2024" ) var jul2024 : Double? = null, // Just in case agar ye bhi object ya float ho
    @SerializedName("May 2024" ) var may2024 : Double? = null,
    @SerializedName("Apr 2024" ) var apr2024 : Double? = null
)


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
data class Items (

    @SerializedName("name"    ) var name   : String? = null,
    @SerializedName("image"   ) var image  : String? = null,
    @SerializedName("item_id" ) var itemId : Int?    = null

)