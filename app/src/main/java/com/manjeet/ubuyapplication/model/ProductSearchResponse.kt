package com.manjeet.ubuyapplication.model

import com.google.gson.annotations.SerializedName

data class ProductSearchResponse (
    @SerializedName("error" ) var error : Boolean? = null,
    @SerializedName("code"  ) var code  : String?  = null,
    @SerializedName("msg"   ) var msg   : String?  = null,
    @SerializedName("data"  ) var data  : SearchData? = SearchData()
)

// 🌟 Color Variation Item Object Structure
data class ColorVariationItem(
    @SerializedName("color") val color: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("sku") val sku: String? = null
)

// 🌟 Main Products Data Class
data class Products (
    @SerializedName("ubuy_sku"         ) var ubuySku         : String?           = null,
    @SerializedName("sku"              ) var sku             : String?           = null,
    @SerializedName("discount"         ) var discount        : Int?              = null,
    @SerializedName("name"             ) var name            : String?           = null,
    @SerializedName("source"           ) var source          : String?           = null,
    @SerializedName("name_english"     ) var nameEnglish     : String?           = null,
    @SerializedName("image"            ) var image           : String?           = null,
    @SerializedName("energy_popup"     ) var energyPopup     : String?           = null,
    @SerializedName("express_delivery" ) var expressDelivery : Boolean?          = null,
    @SerializedName("brand"            ) var brand           : String?           = null,
    @SerializedName("old_price"        ) var oldPrice        : Int?              = null,
    @SerializedName("price"            ) var price           : Int?              = null,
    @SerializedName("price_range"      ) var priceRange      : String?           = null,
    @SerializedName("country"          ) var country         : String?           = null,
    @SerializedName("store"            ) var store           : String?           = null,
    @SerializedName("substore"         ) var substore        : String?           = null,
    @SerializedName("lang_code"        ) var langCode        : String?           = null,
    @SerializedName("parent_sku"       ) var parentSku       : String?           = null,
    @SerializedName("rating"           ) var rating          : Double?           = null,
    @SerializedName("rating_count"     ) var ratingCount     : Int?              = null,
    @SerializedName("rating_percent"   ) var ratingPercent   : String?           = null,
    @SerializedName("delivery"         ) var delivery        : String?           = null,
    @SerializedName("currency"         ) var currency        : String?           = null,
    @SerializedName("product_video"    ) var productVideo    : String?           = null,
    @SerializedName("bought_text"      ) var boughtText      : String?           = null,
    @SerializedName("color_variation"  ) var colorVariation  : ArrayList<ColorVariationItem> = arrayListOf(),
    @SerializedName("info"             ) var info            : ArrayList<String> = arrayListOf()
)

data class CategorySection (
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("value" ) var value : String? = null
)

data class RelatedSearch (
    @SerializedName("title") var title: String? = null
)

// 🌟 1. Price Range ya Slider ke liye Jo Object Milta Hai (Jaise filters[0])
data class FilterPriceOptions (
    @SerializedName("min_price_range"   ) var minPriceRange   : Int? = null,
    @SerializedName("max_price_range"   ) var maxPriceRange   : Int? = null,
    @SerializedName("price_slider_from" ) var priceSliderFrom : Int? = null,
    @SerializedName("price_slider_to"   ) var priceSliderTo   : Int? = null
)

// 🌟 2. Brands ya Normal Multi-Select Options Ke Liye Jo Array Milta Hai (Jaise filters[1])
data class FilterGeneralOptionItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("count") val count: Int? = null
)

// 🌟 3. MAIN FILTERS FIXED: GSON dynamic data type crash se bachane ke liye any object mapping use karega
data class Filters (
    @SerializedName("filter_key"     ) var filterKey     : String?  = null,
    @SerializedName("filter_name"    ) var filterName    : String?  = null,
    @SerializedName("selection_type" ) var selectionType : String?  = null,

    // 🛠️ FIXED: Ise Any? kiya taaki Array [] aur Object {} dono bina crash kiye map ho sakein!
    @SerializedName("options"        ) var options       : Any?     = null
)

data class Sort (
    @SerializedName("title" ) var title : String? = null,
    @SerializedName("value" ) var value : String? = null
)

data class Pagination (
    @SerializedName("totalProducts" ) var totalProducts : Int? = null,
    @SerializedName("totalPages"    ) var totalPages    : Int? = null,
    @SerializedName("itemPerPage"   ) var itemPerPage   : Int? = null,
    @SerializedName("page"          ) var page          : Int? = null
)

data class Req (
    @SerializedName("amazon_store" ) var amazonStore : String? = null,
    @SerializedName("device_type"  ) var deviceType  : String? = null,
    @SerializedName("keywords"     ) var keywords    : String? = null,
    @SerializedName("page"         ) var page        : Int?    = null,
    @SerializedName("search_type"  ) var searchType  : String? = null,
    @SerializedName("sort"         ) var sort        : String? = null,
    @SerializedName("store_id"     ) var storeId     : String? = null,
    @SerializedName("website_id"   ) var websiteId   : String? = null
)

data class FilterAndSortOptions (
    @SerializedName("sort"        ) var sort       : String? = null,
    @SerializedName("search_type" ) var searchType : String? = null
)

data class SearchData (
    @SerializedName("is_digital_card_product" ) var isDigitalCardProduct : String?                    = null,
    @SerializedName("products"                ) var products             : ArrayList<Products>        = arrayListOf(),
    @SerializedName("list_ubuy_sku"           ) var listUbuySku          : ArrayList<String>          = arrayListOf(),
    @SerializedName("category_section"        ) var categorySection      : ArrayList<CategorySection> = arrayListOf(),
    @SerializedName("related_search"          ) var relatedSearch        : RelatedSearch?             = RelatedSearch(),
    @SerializedName("filters"                 ) var filters              : ArrayList<Filters>         = arrayListOf(),
    @SerializedName("sort"                    ) var sort                 : ArrayList<Sort>            = arrayListOf(),
    @SerializedName("pagination"              ) var pagination           : Pagination?                = Pagination(),
    @SerializedName("req"                     ) var req                  : Req?                       = Req(),
    @SerializedName("lang_code"               ) var langCode             : String?                    = null,
    @SerializedName("filterAndSortOptions"    ) var filterAndSortOptions : FilterAndSortOptions?      = FilterAndSortOptions(),
    @SerializedName("is_scrape_data"          ) var isScrapeData         : Boolean?                   = null,
    @SerializedName("scrap_store"             ) var scrapStore           : Int?                       = null,
    @SerializedName("is_scroll"               ) var isScroll             : Boolean?                   = null,
    @SerializedName("did_you_mean"            ) var didYouMean           : Boolean?                   = null,
    @SerializedName("correct_query"           ) var correctQuery         : String?                    = null,
    @SerializedName("u_sr_id"                 ) var uSrId                : String?                    = null
)