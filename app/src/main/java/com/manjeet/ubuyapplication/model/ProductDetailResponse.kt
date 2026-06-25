package  com.manjeet.ubuyapplication.model

import com.google.gson.annotations.SerializedName


data class ProductDetailResponse(
    @SerializedName("error") val error: Boolean? = false,
    @SerializedName("code") val code: String? = "200",
    @SerializedName("msg") val msg: String? = "",
    @SerializedName("data") val data: ProductDetailData? = null,

    @SerializedName("modify_data") val modifyData: Modifydata? = null
)

data class ProductImages(
    @SerializedName("thumb") var thumb: String? = null,
    @SerializedName("thumb-list") var thumbList: String? = null,
    @SerializedName("full") var full: String? = null,
    @SerializedName("zoom") var zoom: String? = null
)

data class Level1 (

    @SerializedName("node_id" ) var nodeId : Int?    = null,
    @SerializedName("name"    ) var name   : String? = null

)


data class Level2 (

    @SerializedName("node_id" ) var nodeId : Int?    = null,
    @SerializedName("name"    ) var name   : String? = null

)

data class Category (

    @SerializedName("level1" ) var level1 : Level1? = Level1(),
    @SerializedName("level2" ) var level2 : Level2? = Level2()

)

data class ProductSpecification (

    @SerializedName("title" ) var title : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class SellerInfo(
    val dummy: String? = null // Data class ke liye kam se kam ek property zaroori hai
)

data class ProductDealData (

    @SerializedName("deal_duration"    ) var dealDuration   : Int?    = null,
    @SerializedName("deal_text"        ) var dealText       : String? = null,
    @SerializedName("show_deal_timmer" ) var showDealTimmer : String? = null

)

data class DealPriceOptions (

    @SerializedName("list_price"      ) var listPrice     : Int? = null,
    @SerializedName("amazon_price"    ) var amazonPrice   : Int? = null,
    @SerializedName("deal_price_time" ) var dealPriceTime : Int? = null

)


data class CustomerRatingDetail(
    val dummy: String? = null
)


data class CombinationData(
    val dummy: String? = null
)



data class ProductOptions (

    @SerializedName("option_id" ) var optionId  : String?  = null,
    @SerializedName("imgUrl"    ) var imgUrl    : String?  = null,
    @SerializedName("value"     ) var value     : String?  = null,
    @SerializedName("en_value"  ) var enValue   : String?  = null,
    @SerializedName("available" ) var available : Boolean? = null,
    @SerializedName("selected"  ) var selected  : Boolean? = null

)

data class VariationData(
    @SerializedName("heading") var heading: String? = null,
    @SerializedName("options") var options: ArrayList<VariationOption> = arrayListOf()
)

data class VariationOption(
    @SerializedName("value") var value: String? = null,
    @SerializedName("option_id") var optionId: String? = null,


    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("selected") var selected: Boolean? = false,
    @SerializedName("available") var available: Boolean? = true
)

data class UspSlider (

    @SerializedName("en_title"   ) var enTitle   : String? = null,
    @SerializedName("title"      ) var title     : String? = null,
    @SerializedName("image_path" ) var imagePath : String? = null

)



data class PaymentGateways (

    @SerializedName("image_path" ) var imagePath : String? = null,
    @SerializedName("title"      ) var title     : String? = null

)

data class DietarySupplement(
    val dummy: String? = null
)


data class PrimeShippingMessage (

    @SerializedName("eng" ) var eng : String? = null,
    @SerializedName("arb" ) var arb : String? = null

)



data class NonprimeShippingMessage (

    @SerializedName("eng" ) var eng : String? = null,
    @SerializedName("arb" ) var arb : String? = null

)


data class Setting (

    @SerializedName("prime_shipping_days"       ) var primeShippingDays       : Int?                     = null,
    @SerializedName("prime_shipping_message"    ) var primeShippingMessage    : PrimeShippingMessage?    = PrimeShippingMessage(),
    @SerializedName("nonprime_shipping_days"    ) var nonprimeShippingDays    : String?                  = null,
    @SerializedName("nonprime_shipping_message" ) var nonprimeShippingMessage : NonprimeShippingMessage? = NonprimeShippingMessage()

)

data class Shipping (

    @SerializedName("setting" ) var setting : Setting? = Setting()

)


data class ProductConfig (

    @SerializedName("show_dangerous_note"         ) var showDangerousNote        : Boolean? = null,
    @SerializedName("show_dangerous_note_message" ) var showDangerousNoteMessage : String?  = null

)

data class ProductFaqs(
    val dummy: String? = null
)






data class ProductDetailData (

    @SerializedName("parent_sku"                        ) var parentSku                      : String?                         = null,
    @SerializedName("product_id"                        ) var productId                      : Int?                            = null,
    @SerializedName("slug"                              ) var slug                           : String?                         = null,
    @SerializedName("product_title"                     ) var productTitle                   : String?                         = null,
    @SerializedName("is_prime"                          ) var isPrime                        : Boolean?                        = null,
    @SerializedName("is_in_stock"                       ) var isInStock                      : Boolean?                        = null,
    @SerializedName("product_description"               ) var productDescription             : String?                         = null,
    @SerializedName("price"                             ) var price                          : Int?                            = null,
    @SerializedName("product_weight"                    ) var productWeight                  : String?                         = null,
    @SerializedName("product_images"                    ) var productImages                  : ArrayList<ProductImages>        = arrayListOf(),
    @SerializedName("features"                          ) var features                       : ArrayList<String>               = arrayListOf(),
    @SerializedName("category"                          ) var category                       : Category?                       = Category(),
    @SerializedName("product_specification"             ) var productSpecification           : ArrayList<ProductSpecification> = arrayListOf(),
    @SerializedName("seller_info"                       ) var sellerInfo                     : SellerInfo?                     = SellerInfo(),
    @SerializedName("dimensions"                        ) var dimensions                     : String?                         = null,
    @SerializedName("product_dimensions"                ) var productDimensions              : String?                         = null,
    @SerializedName("package_dimensions"                ) var packageDimensions              : String?                         = null,
    @SerializedName("max_qty"                           ) var maxQty                         : Int?                            = null,
    @SerializedName("is_active"                         ) var isActive                       : Int?                            = null,
    @SerializedName("delivery_days"                     ) var deliveryDays                   : Int?                            = null,
    @SerializedName("is_size_chart"                     ) var isSizeChart                    : Boolean?                        = null,
    @SerializedName("is_custom_id_product"              ) var isCustomIdProduct              : Boolean?                        = null,
    @SerializedName("is_pre_order"                      ) var isPreOrder                     : Boolean?                        = null,
    @SerializedName("model_number"                      ) var modelNumber                    : String?                         = null,
    @SerializedName("product_image"                     ) var productImage                   : String?                         = null,
    @SerializedName("best_seller"                       ) var bestSeller                     : Boolean?                        = null,
    @SerializedName("is_bundle_product"                 ) var isBundleProduct                : Boolean?                        = null,
    @SerializedName("bundle_products"                   ) var bundleProducts                 : ArrayList<String>               = arrayListOf(),
    @SerializedName("important_info"                    ) var importantInfo                  : ArrayList<String>               = arrayListOf(),
    @SerializedName("what_in_the_box"                   ) var whatInTheBox                   : ArrayList<String>               = arrayListOf(),
    @SerializedName("is_custom_product"                 ) var isCustomProduct                : Int?                            = null,
    @SerializedName("ship_from"                         ) var shipFrom                       : String?                         = null,
    @SerializedName("ubuy_sku"                          ) var ubuySku                        : String?                         = null,
    @SerializedName("original_price"                    ) var originalPrice                  : Int?                            = null,
    @SerializedName("ubuy_substore"                     ) var ubuySubstore                   : String?                         = null,
    @SerializedName("ubuy_store"                        ) var ubuyStore                      : String?                         = null,
    @SerializedName("customer_rating"                   ) var customerRating                 : Double?                         = null,
    @SerializedName("energy"                            ) var energy                         : String?                         = null,
    @SerializedName("energy_popup_url"                  ) var energyPopupUrl                 : ArrayList<String>               = arrayListOf(),
    @SerializedName("min_qty"                           ) var minQty                         : Int?                            = null,
    @SerializedName("tech_data"                         ) var techData                       : ArrayList<String>               = arrayListOf(),
    @SerializedName("is_non_returnable"                 ) var isNonReturnable                : Boolean?                        = null,
    @SerializedName("is_deal_active"                    ) var isDealActive                   : String?                         = null,
    @SerializedName("product_deal_data"                 ) var productDealData                : ProductDealData?                = ProductDealData(),
    @SerializedName("deal_price_options"                ) var dealPriceOptions               : DealPriceOptions?               = DealPriceOptions(),
    @SerializedName("customer_rating_detail"            ) var customerRatingDetail           : CustomerRatingDetail?           = CustomerRatingDetail(),
    @SerializedName("combination_data"                  ) var combinationData                : CombinationData?                = CombinationData(),
    @SerializedName("variation_data"                    ) var variationData                  : ArrayList<VariationData>        = arrayListOf(),
    @SerializedName("chart_data"                        ) var chartData                      : String?                         = null,
    @SerializedName("customization_data"                ) var customizationData              : ArrayList<String>               = arrayListOf(),
    @SerializedName("product_update"                    ) var productUpdate                  : String?                         = null,
    @SerializedName("eligible_for_free_shipping"        ) var eligibleForFreeShipping        : Boolean?                        = null,
    @SerializedName("shipping_pincode_validation"       ) var shippingPincodeValidation      : Boolean?                        = null,
    @SerializedName("usp_slider"                        ) var uspSlider                      : ArrayList<UspSlider>            = arrayListOf(),
    @SerializedName("important_note"                    ) var importantNote                  : String?                         = null,
    @SerializedName("payment_gateways"                  ) var paymentGateways                : ArrayList<PaymentGateways>      = arrayListOf(),
    @SerializedName("usd_price"                         ) var usdPrice                       : String?                         = null,
    @SerializedName("usd_original_price"                ) var usdOriginalPrice               : String?                         = null,
    @SerializedName("product_videos"                    ) var productVideos                  : ArrayList<String>               = arrayListOf(),
    @SerializedName("source"                            ) var source                         : String?                         = null,
    @SerializedName("cart_max_qty"                      ) var cartMaxQty                     : Int?                            = null,
    @SerializedName("is_restricted"                     ) var isRestricted                   : Boolean?                        = null,
    @SerializedName("product_url"                       ) var productUrl                     : String?                         = null,
    @SerializedName("notify_show"                       ) var notifyShow                     : Boolean?                        = null,
    @SerializedName("is_show_warranty"                  ) var isShowWarranty                 : Boolean?                        = null,
    @SerializedName("category_name"                     ) var categoryName                   : String?                         = null,
    @SerializedName("category_id"                       ) var categoryId                     : Int?                            = null,
    @SerializedName("dietary_supplement"                ) var dietarySupplement              : DietarySupplement?              = DietarySupplement(),
    @SerializedName("power_converter_message"           ) var powerConverterMessage          : String?                         = null,
    @SerializedName("shipping"                          ) var shipping                       : Shipping?                       = Shipping(),
    @SerializedName("is_show_vehicle"                   ) var isShowVehicle                  : Boolean?                        = null,
    @SerializedName("brand"                             ) var brand                          : String?                         = null,
    @SerializedName("brand_key"                         ) var brandKey                       : String?                         = null,
    @SerializedName("bundle_title_message"              ) var bundleTitleMessage             : String?                         = null,
    @SerializedName("product_config"                    ) var productConfig                  : ProductConfig?                  = ProductConfig(),
    @SerializedName("delivery_text"                     ) var deliveryText                   : String?                         = null,
    @SerializedName("delivery_text_msg"                 ) var deliveryTextMsg                : String?                         = null,
    @SerializedName("preOrderMessage"                   ) var preOrderMessage                : String?                         = null,
    @SerializedName("product_faqs"                      ) var productFaqs                    : ProductFaqs?                    = ProductFaqs(),
    @SerializedName("website_id"                        ) var websiteId                      : Int?                            = null,
    @SerializedName("dimension_api_call"                ) var dimensionApiCall               : Boolean?                        = null,
    @SerializedName("important_information"             ) var importantInformation           : ArrayList<String>               = arrayListOf(),
    @SerializedName("local_important_information"       ) var localImportantInformation      : ArrayList<String>               = arrayListOf(),
    @SerializedName("important_information_title"       ) var importantInformationTitle      : String?                         = null,
    @SerializedName("local_important_information_title" ) var localImportantInformationTitle : String?                         = null,
    @SerializedName("local_what_in_the_box"             ) var localWhatInTheBox              : ArrayList<String>               = arrayListOf(),
    @SerializedName("what_in_the_box_title"             ) var whatInTheBoxTitle              : String?                         = null,
    @SerializedName("local_what_in_the_box_title"       ) var localWhatInTheBoxTitle         : String?                         = null,
    @SerializedName("bought_text"                       ) var boughtText                     : String?                         = null,
    @SerializedName("is_customization_active"           ) var isCustomizationActive          : Boolean?                        = null,
    @SerializedName("other_addtocart_parameters"        ) var otherAddtocartParameters       : String?                         = null,
    @SerializedName("sku"                               ) var sku                            : String?                         = null,
    @SerializedName("restricted_curl_url"               ) var restrictedCurlUrl              : String?                         = null

)


// 1. The Main Master State Holder
data class ProductSectionsState(
    val vehicleFilter: VehicleFilterData? = null,
    val bundleInfo: BundleInfoData? = null,
    val shippingBreakdown: ShippingBreakdownData? = null,
    val fastShippingUnlock: FastShippingUnlockData? = null,
    val warrantyPlan: WarrantyPlanData? = null
)

// 2. Data Class for Vehicle Filter Box
data class VehicleFilterData(
    val title: String = "Add your vehicle",
    val warningText: String
)

// 3. Data Classes for Bundle Section
data class BundleInfoData(
    val title: String,
    val items: List<BundleItem>
)

data class BundleItem(
    val id: String,
    val title: String,
    val imageResId: Int // points to your drawables like R.drawable.img_adapter
)

// 4. Data Class for Shipping Cost Breakdown Box
data class ShippingBreakdownData(
    val infoText: String,
    val estimatedDeliveryDate: String,
    val fromCountry: String,
    val toCountry: String,
    val subtotal: String,
    val standardShipping: String,
    val customsDuties: String,
    val vat: String,
    val grandTotal: String
)

// 5. Data Class for Fast Shipping Progress Bar
data class FastShippingUnlockData(
    val remainingAmount: String,
    val progress: Float // A decimal number between 0.0f and 1.0f (e.g., 0.6f)
)

// 6. Data Class for Warranty Card
data class WarrantyPlanData(
    val title: String = "U-Care Warranty",
    val description: String
)


//=========================================================================
//   DETAIL SCREEN PROS AND CONS
//==========================================================================


// ─── 1. ROOT RESPONSE MODEL ───
data class Modifydata(
    @SerializedName("error") val error: Boolean? = false,
    @SerializedName("code") val code: String? = "0",
    @SerializedName("msg") val msg: String? = "",
    @SerializedName("data") val data: ModifyProductData? = ModifyProductData())

// ─── 2. DATA HOLDER BLOCK ───
data class ModifyProductData(
    @SerializedName("pros_cons") val prosCons: ProsCons? = ProsCons(),
    @SerializedName("features") val features: Features? = Features()
)

// ─── 3. PROS & CONS MODEL ───
data class ProsCons(
    @SerializedName("pros") val pros: List<String> = emptyList(),
    @SerializedName("cons") val cons: List<String> = emptyList(),
    @SerializedName("local_pros") val localPros: List<String> = emptyList(),
    @SerializedName("local_cons") val localCons: List<String> = emptyList(),
    @SerializedName("editorial_review") val editorialReview: String? = "",
    @SerializedName("local_editorial_review") val localEditorialReview: String? = ""
)

// ─── 4. FEATURES & FAQ HOLDER (Yeh aapke code mein missing tha) ───
data class Features(
    @SerializedName("quote") val quote: String? = "",
    @SerializedName("local_quote") val localQuote: String? = "",
    @SerializedName("features") val featuresList: List<String> = emptyList(),
    @SerializedName("local_features") val localFeaturesList: List<String> = emptyList(),
    @SerializedName("faq") val faq: List<ProductFaqItem> = emptyList(),
    @SerializedName("local_faq") val localFaq: List<ProductFaqItem> = emptyList(),
    @SerializedName("detail_features") val detailFeatures: List<String> = emptyList()
)

// ─── 5. SINGLE FAQ ITEM MODEL ───
data class ProductFaqItem(
    @SerializedName("question") val question: String? = "",
    @SerializedName("answer") val answer: String? = ""
)





//=================================================================================
// REVIEW AND RATING MODELS (100% CRASH-FREE FIXED)
//=================================================================================

data class OtherDetail(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("msg") var msg: String? = null,
    // 🔥 FIXED: JSON mein key "modifyData" hai, "data" nahi!
    @SerializedName("modifyData") var data: ProductReviewData? = ProductReviewData()
)

data class RatingBreakdown(
    @SerializedName("1") var star1: Int? = 0,
    @SerializedName("2") var star2: Int? = 0,
    @SerializedName("3") var star3: Int? = 0,
    @SerializedName("4") var star4: Int? = 0,
    @SerializedName("5") var star5: Int? = 0
)

data class ProductCustomerRatingDetail(
    @SerializedName("total_rating") var totalRating: Double? = 0.0,
    @SerializedName("total_review") var totalReview: Int? = 0,
    @SerializedName("rating_breakdown") var ratingBreakdown: RatingBreakdown? = RatingBreakdown()
)

data class RecommendationSummary(
    @SerializedName("recommend_percentage") var recommendPercentage: Int? = null,
    @SerializedName("complaint_rate") var complaintRate: String? = null
)

data class ReviewData(
    @SerializedName("nickname") var nickname: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("rating") var rating: Int? = null,
    @SerializedName("verified_purchase") var verifiedPurchase: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("detail") var detail: String? = null,
    @SerializedName("img_array") var imgArray: List<String> = emptyList()
)

data class ReviewActions(
    @SerializedName("read_all_reviews") var readAllReviews: Boolean? = null,
    @SerializedName("write_review") var writeReview: Boolean? = null
)

data class UserReview(
    @SerializedName("customer_name") var customerName: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("review_date") var reviewDate: String? = null,
    @SerializedName("rating") var rating: Int? = null,
    @SerializedName("review_badge") var reviewBadge: String? = null,
    @SerializedName("review_images") var reviewImages: List<String> = emptyList(),
    @SerializedName("review_text") var reviewText: String? = null
)

data class PaymentMethods(
    @SerializedName("image_path") var imagePath: String? = null,
    @SerializedName("title") var title: String? = null
)

data class InstallmentProvider(
    @SerializedName("name") var name: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("learn_more_text") var learnMoreText: String? = null,
    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("learn_more_url") var learnMoreUrl: String? = null
)

data class WorkWellWith(
    @SerializedName("product_title") var productTitle: String? = null,
    @SerializedName("img_url") var imgUrl: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("description") var description: String? = null
)

data class ChooseProduct(
    @SerializedName("title") var title: String? = null,
    @SerializedName("features") var features: List<String> = emptyList()
)

data class ProductReviewData(
    @SerializedName("what_stands_out") var whatStandsOut: String? = null,
    @SerializedName("ai_related_search_keyword") var aiRelatedSearchKeyword: String? = null,
    @SerializedName("local_what_stands_out") var localWhatStandsOut: String? = null,
    @SerializedName("what_stands_out_title") var whatStandsOutTitle: String? = null,
    @SerializedName("local_what_stands_out_title") var localWhatStandsOutTitle: String? = null,
    @SerializedName("who_should_buy") var whoShouldBuy: String? = null,
    @SerializedName("local_who_should_buy") var localWhoShouldBuy: String? = null,
    @SerializedName("who_should_buy_title") var whoShouldBuyTitle: String? = null,
    @SerializedName("local_who_should_buy_title") var localWhoShouldBuyTitle: String? = null,
    @SerializedName("nutritionist_take") var nutritionistTake: String? = null,
    @SerializedName("local_nutritionist_take") var localNutritionistTake: String? = null,
    @SerializedName("nutritionist_take_title") var nutritionistTakeTitle: String? = null,
    @SerializedName("local_nutritionist_take_title") var localNutritionistTakeTitle: String? = null,
    @SerializedName("product_ai_description") var productAiDescription: String? = null,
    @SerializedName("relatedproducts") var relatedproducts: String? = null,
    @SerializedName("otherproducts") var otherproducts: String? = null,
    @SerializedName("customer_rating_detail") var customerRatingDetail: ProductCustomerRatingDetail? = ProductCustomerRatingDetail(),
    @SerializedName("recommendation_summary") var recommendationSummary: RecommendationSummary? = RecommendationSummary(),
    @SerializedName("review_data") var reviewData: List<ReviewData> = emptyList(),
    @SerializedName("review_actions") var reviewActions: ReviewActions? = ReviewActions(),
    @SerializedName("user_review") var userReview: UserReview? = UserReview(),
    @SerializedName("payment_methods") var paymentMethods: List<PaymentMethods> = emptyList(),
    @SerializedName("installment_provider") var installmentProvider: InstallmentProvider? = InstallmentProvider(),
    @SerializedName("related_brand") var relatedBrand: List<String> = emptyList(),
    @SerializedName("related_category") var relatedCategory: List<String> = emptyList(),
    @SerializedName("work_well_with") var workWellWith: List<WorkWellWith> = emptyList(),
    @SerializedName("login_user_review") var loginUserReview: List<String> = emptyList(),
    @SerializedName("choose_product") var chooseProduct: ChooseProduct? = ChooseProduct(),
    @SerializedName("faq") var faqList: List<com.manjeet.ubuyapplication.model.ProductFaqItem> = emptyList(),
)

//===================    PRODUCT VARIATION =================================//

// 1. Root Level Response Wrapper
data class ProductListResponse (
    @SerializedName("data") var data : ArrayList<AirData> = arrayListOf()
)

// 2. Pricing Models
data class AirPrice (
    @SerializedName("final_price_usd") var finalPriceUsd : Double? = null,
    @SerializedName("shipping_in_product") var shippingInProduct : Int? = null,
    @SerializedName("non_prime_difference") var nonPrimeDifference : Int? = null,
    @SerializedName("final_price") var finalPrice : Int? = null
)

data class AirListPrice (
    @SerializedName("final_price_usd") var finalPriceUsd : Int? = null,
    @SerializedName("shipping_in_product") var shippingInProduct : Int? = null,
    @SerializedName("non_prime_difference") var nonPrimeDifference : Int? = null,
    @SerializedName("final_price") var finalPrice : Int? = null
)

data class AirSavePrice (
    @SerializedName("final_price_usd") var finalPriceUsd : Int? = null,
    @SerializedName("shipping_in_product") var shippingInProduct : Int? = null,
    @SerializedName("non_prime_difference") var nonPrimeDifference : Int? = null,
    @SerializedName("final_price") var finalPrice : Int? = null
)

data class AirAllPrices (
    @SerializedName("price") var price : AirPrice? = AirPrice(),
    @SerializedName("list_price") var listPrice : AirListPrice? = AirListPrice(),
    @SerializedName("save_price") var savePrice : AirSavePrice? = AirSavePrice(),
    @SerializedName("amazon_price") var amazonPrice : Double? = null,
    @SerializedName("amazon_shipping_price") var amazonShippingPrice : Int? = null
)

data class AirPriceArray (
    @SerializedName("price") var price : AirPrice? = AirPrice(),
    @SerializedName("list_price") var listPrice : AirListPrice? = AirListPrice(),
    @SerializedName("save_price") var savePrice : AirSavePrice? = AirSavePrice(),
    @SerializedName("amazon_price") var amazonPrice : Double? = null,
    @SerializedName("amazon_shipping_price") var amazonShippingPrice : Int? = null
)

// 3. Customer Rating Detail (🎯 Prefixed to avoid Redeclaration)
data class AirCustomerRatingDetail (
    @SerializedName("average_rating") var averageRating: Double? = null
)

// 4. Product Media Components
data class AirProductImages (
    @SerializedName("thumb") var thumb : String? = null,
    @SerializedName("thumb-list") var thumbList : String? = null,
    @SerializedName("full") var full : String? = null,
    @SerializedName("zoom") var zoom : String? = null
)

data class AirProductVideos (
    @SerializedName("video_url") var videoUrl : String? = null,
    @SerializedName("video_preview") var videoPreview : String? = null
)

// 5. Technical Specifications Blocks
data class AirProductSpecification (
    @SerializedName("title") var title : String? = null,
    @SerializedName("value") var value : String? = null
)

data class AirTechData (
    @SerializedName("title") var title : String? = null,
    @SerializedName("value") var value : String? = null
)

// 6. Variants Matrix Blocks (Options & VariationData prefixed)
data class AirOptions (
    @SerializedName("option_id") var optionId : String? = null,
    @SerializedName("imgUrl") var imgUrl : String? = null,
    @SerializedName("value") var value : String? = null,
    @SerializedName("en_value") var enValue : String? = null,
    @SerializedName("available") var available : Boolean? = null,
    @SerializedName("selected") var selected : Boolean? = null
)

data class AirVariationData (
    @SerializedName("heading") var heading : String? = null,
    @SerializedName("options") var options : ArrayList<AirOptions> = arrayListOf()
)

// 7. Main Core Item Data Class Model (🎯 Renamed to AirData to kill conflicts)
data class AirData (
    @SerializedName("sku") var sku : String? = null,
    @SerializedName("ubuy_sku") var ubuySku : String? = null,
    @SerializedName("ship_from") var shipFrom : String? = null,
    @SerializedName("parent_sku") var parentSku : String? = null,
    @SerializedName("product_title") var productTitle : String? = null,
    @SerializedName("product_image") var productImage : String? = null,
    @SerializedName("is_prime") var isPrime : Boolean? = null,
    @SerializedName("is_restricted") var isRestricted : Boolean? = null,
    @SerializedName("ubuy_store") var ubuyStore : String? = null,
    @SerializedName("ubuy_substore") var ubuySubstore : String? = null,
    @SerializedName("slug") var slug : String? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("original_price") var originalPrice : Int? = null,
    @SerializedName("all_prices") var allPrices : AirAllPrices? = AirAllPrices(),
    @SerializedName("brand") var brand : String? = null,
    @SerializedName("category") var category : ArrayList<String> = arrayListOf(),
    @SerializedName("is_in_stock") var isInStock : Boolean? = null,
    @SerializedName("customer_rating_detail") var customerRatingDetail : AirCustomerRatingDetail? = AirCustomerRatingDetail(),
    @SerializedName("product_images") var productImages : ArrayList<AirProductImages> = arrayListOf(),
    @SerializedName("product_videos") var productVideos : ArrayList<AirProductVideos> = arrayListOf(),
    @SerializedName("features") var features : ArrayList<String> = arrayListOf(),
    @SerializedName("product_specification") var productSpecification : ArrayList<AirProductSpecification> = arrayListOf(),
    @SerializedName("product_id") var productId : Int? = null,
    @SerializedName("product_weight") var productWeight : Int? = null,
    @SerializedName("short_description") var shortDescription : String? = null,
    @SerializedName("max_qty") var maxQty : Int? = null,
    @SerializedName("delivery_days") var deliveryDays : Int? = null,
    @SerializedName("delivery_text") var deliveryText : String? = null,
    @SerializedName("delivery_text_msg") var deliveryTextMsg : String? = null,
    @SerializedName("tech_data") var techData : ArrayList<AirTechData> = arrayListOf(),
    @SerializedName("variation_data") var variationData : ArrayList<AirVariationData> = arrayListOf(),
    @SerializedName("price_array") var priceArray : AirPriceArray? = AirPriceArray(),
    @SerializedName("min_qty") var minQty : Int? = null,
    @SerializedName("usd_price") var usdPrice : String? = null,
    @SerializedName("usd_original_price") var usdOriginalPrice : String? = null,
    @SerializedName("brand_key") var brandKey : String? = null,
    @SerializedName("product_url") var productUrl : String? = null,
    @SerializedName("is_pre_order") var isPreOrder : Boolean? = null,
    @SerializedName("is_trending") var isTrending : Boolean? = null,
    @SerializedName("best_seller") var bestSeller : Boolean? = null,
    @SerializedName("is_non_returnable") var isNonReturnable : Boolean? = null,
    @SerializedName("eligible_for_free_shipping") var eligibleForFreeShipping : Boolean? = null,
    @SerializedName("power_converter_message") var powerConverterMessage : String? = null
)