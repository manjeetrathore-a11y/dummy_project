package com.manjeet.ubuyapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manjeet.ubuyapplication.model.ProductDetailData
import com.manjeet.ubuyapplication.model.Products
import com.manjeet.ubuyapplication.model.ProductImages
import com.manjeet.ubuyapplication.model.ProductSectionsState
import com.manjeet.ubuyapplication.model.VehicleFilterData
import com.manjeet.ubuyapplication.model.ShippingBreakdownData
import com.manjeet.ubuyapplication.model.BundleInfoData
import com.manjeet.ubuyapplication.model.FastShippingUnlockData
import com.manjeet.ubuyapplication.model.WarrantyPlanData
import com.manjeet.ubuyapplication.model.Modifydata
import com.manjeet.ubuyapplication.model.OtherDetail
import com.manjeet.ubuyapplication.model.ProductReviewData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ProductDetailUiState {
    object Loading : ProductDetailUiState
    data class Success(
        val responseWrapper: com.manjeet.ubuyapplication.model.ProductDetailResponse,
        val sectionsState: ProductSectionsState,
        val modifyDataResponse: Modifydata?,
        val reviewDataResponse: ProductReviewData?
    ) : ProductDetailUiState
    data class Error(val message: String) : ProductDetailUiState
}

class ProductDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun fetchProductDetailsFromList(
        context: Context,
        productsList: List<Products>,
        productId: String
    ) {
        viewModelScope.launch {
            Log.d("UBUY_VIEWMODEL", "📥 fetchProductDetailsFromList initiated for Product ID: $productId")
            _uiState.value = ProductDetailUiState.Loading
            try {
                val gson = Gson()

                // ─── 📁 1. PARSING PROS & CONS ───
                val extendedDetailJson = try {
                    context.assets.open("product_extended_detail.json").bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    Log.e("UBUY_VIEWMODEL", "❌ Error reading product_extended_detail.json", e)
                    ""
                }

                val parsedExtendedData: Modifydata? = if (extendedDetailJson.isNotEmpty()) {
                    try { gson.fromJson(extendedDetailJson, Modifydata::class.java) } catch (e: Exception) { null }
                } else null

                // ─── 📁 2. PURE DYNAMIC PARSING FROM ASSET (REVIEWS) ───
                val reviewJson = try {
                    context.assets.open("product_detail_review.json").bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    Log.e("UBUY_VIEWMODEL", "❌ Error reading product_detail_review.json", e)
                    ""
                }

                var finalReviewData: ProductReviewData? = null
                if (reviewJson.isNotEmpty()) {
                    try {
                        val wrappedParse = gson.fromJson(reviewJson, OtherDetail::class.java)
                        if (wrappedParse?.data != null) {
                            finalReviewData = wrappedParse.data
                        }
                    } catch (e: Exception) {}
                }

                // ─── 📁 🌟 PARSING VARIATIONS WITH EXPLICIT CLASS TYPE 🌟 ───
                // 🎯 CHANGED FOR VARIATION: Uses AirVariationData to match new unique non-conflicting model structure
                val variationDataList: List<com.manjeet.ubuyapplication.model.AirVariationData> = withContext(Dispatchers.IO) {
                    try {
                        val variationJson = context.assets.open("product_detail_variation.json").bufferedReader().use { it.readText() }

                        val genericMapType = object : com.google.gson.reflect.TypeToken<Map<String, Any>>() {}.type
                        val rawMap: Map<String, Any> = gson.fromJson(variationJson, genericMapType)

                        val rawListKey = rawMap.keys.find { it.contains("variation", ignoreCase = true) || it == "data" } ?: rawMap.keys.firstOrNull() ?: ""
                        val rawListJson = gson.toJson(rawMap[rawListKey])

                        val targetListType = object : com.google.gson.reflect.TypeToken<List<com.manjeet.ubuyapplication.model.AirVariationData>>() {}.type
                        val finalExtractedList: List<com.manjeet.ubuyapplication.model.AirVariationData> = gson.fromJson(rawListJson, targetListType)

                        Log.d("UBUY_VIEWMODEL", "📦 Dynamic Variation List loaded successfully. Count: ${finalExtractedList.size}")
                        finalExtractedList
                    } catch (e: Exception) {
                        Log.e("UBUY_VIEWMODEL", "❌ Error parsing variations dynamically without class ref", e)
                        emptyList()
                    }
                }

                // ─── 🛠️ INITIAL SEED IMAGE ARRAYS ───
                val dynamicImagesList = arrayListOf(
                    ProductImages(
                        thumb = "https://m.media-amazon.com/images/I/61ChWONDLcL._AC_SX342_.jpg",
                        thumbList = "https://m.media-amazon.com/images/I/61ChWONDLcL._AC_SX342_.jpg",
                        full = "https://m.media-amazon.com/images/I/61ChWONDLcL._AC_SX569_.jpg",
                        zoom = "https://m.media-amazon.com/images/I/61ChWONDLcL._AC_SX679_.jpg"
                    )
                )

                val jsonProductImage = dynamicImagesList.first().full

                val realDetailData = ProductDetailData().apply {
                    this.productId = 225987023
                    this.sku = "B0FQH6KK7C"
                    this.ubuySku = "ubuy_B0FQH6KK7C"
                    this.parentSku = "B0H37Q3N1Q"
                    this.slug = "iphone-air-cloud-white-256gb"
                    this.productTitle = "iPhone Air Space Black"
                    this.price = 5578
                    this.originalPrice = 6644
                    this.brand = "Apple"
                    this.brandKey = "apple"
                    this.productImage = jsonProductImage
                    this.productImages = dynamicImagesList
                    this.categoryName = "Cell Phones & Accessories"
                    this.ubuyStore = "US"
                    this.ubuySubstore = "usstore"
                    this.bestSeller = false
                    this.isPrime = false
                    this.isInStock = true
                    this.productWeight = "0.9"
                    this.deliveryDays = 3
                    this.deliveryText = "Wednesday, June 17"
                    this.deliveryTextMsg = "Order now and get it around Wednesday, June 17"
                    this.customerRating = 4.8
                    this.usdPrice = "1199"
                    this.usdOriginalPrice = "1429"
                    this.source = "onesearch"
                    this.cartMaxQty = 15
                    this.isRestricted = false
                    this.productUrl = "https://stage.ubuy.com.sa/en/product/QXMD3N1P4-iphone-17-air-space-black-512-gb"
                    this.notifyShow = false
                    this.isShowWarranty = true
                    this.isShowVehicle = false
                    this.isBundleProduct = false
                    this.isActive = 1
                    this.features = arrayListOf(
                        "SUPER THIN. STRINKINGLY LIGHT. SHOCKINGLY STRONG.",
                        "MORE DURABLE THAN ANY PREVIOUS iPHONE",
                        "TWO ADVANCED CAMERAS IN ONE 48MP"
                    )
                    this.whatInTheBox = arrayListOf("USB Cable")
                    this.localWhatInTheBox = arrayListOf("USB Cable")
                    this.whatInTheBoxTitle = "What's in the box"
                    this.importantInformationTitle = "Important information"

                    // 🎯 CHANGED FOR VARIATION: Iterates using the non-conflicting unique Air Variation data structures safely
                    val legacyVariationsList = ArrayList<com.manjeet.ubuyapplication.model.VariationData>()
                    variationDataList.forEach { productVarData ->
                        val legacyOptionsList = ArrayList<com.manjeet.ubuyapplication.model.VariationOption>()
                        productVarData.options?.forEach { productOpt ->
                            val legacyOption = com.manjeet.ubuyapplication.model.VariationOption().apply {
                                this.optionId = productOpt.optionId
                                this.imgUrl = productOpt.imgUrl
                                this.value = productOpt.value
                                this.available = productOpt.available
                                this.selected = productOpt.selected
                            }
                            legacyOptionsList.add(legacyOption)
                        }
                        val legacyVariationData = com.manjeet.ubuyapplication.model.VariationData().apply {
                            this.heading = productVarData.heading
                            this.options = legacyOptionsList
                        }
                        legacyVariationsList.add(legacyVariationData)
                    }
                    this.variationData = legacyVariationsList
                    this.importantInfo = arrayListOf()
                    this.bundleProducts = arrayListOf()
                    this.isDealActive = "yes"
                }

                // Initial image setup if sync handles existing preferences
                realDetailData.variationData?.forEach { vData ->
                    if (vData.heading?.contains("Color", ignoreCase = true) == true) {
                        vData.options?.find { it.selected == true }?.let { selectedOpt ->
                            val rawUrl = selectedOpt.imgUrl
                            if (!rawUrl.isNullOrEmpty()) {
                                val cleanUrl = if (rawUrl.contains("._SS64_")) rawUrl.replace("._SS64_", "._AC_SX569_") else rawUrl
                                realDetailData.productImage = cleanUrl
                                realDetailData.productImages = arrayListOf(
                                    ProductImages(thumb = cleanUrl, thumbList = cleanUrl, full = cleanUrl, zoom = cleanUrl)
                                )
                                realDetailData.productTitle = "iPhone Air (${selectedOpt.value ?: "Selected Product"})"
                            }
                        }
                    }
                }

                val dynamicShipping = ShippingBreakdownData(
                    infoText = "All items ship from the US.",
                    estimatedDeliveryDate = realDetailData.deliveryText ?: "Wednesday, June 17",
                    fromCountry = "🇺🇸 US",
                    toCountry = "🇬🇧 UK",
                    subtotal = "£${realDetailData.price}.00",
                    standardShipping = "£19.20",
                    customsDuties = "£4.90",
                    vat = "£9.60",
                    grandTotal = "£${(realDetailData.price ?: 0) + 33}.70"
                )

                val defaultSections = ProductSectionsState(
                    vehicleFilter = VehicleFilterData(title = "Add your vehicle", warningText = "Vehicle compatibility notice"),
                    bundleInfo = BundleInfoData(title = "This bundle contains items", items = emptyList()),
                    shippingBreakdown = dynamicShipping,
                    fastShippingUnlock = FastShippingUnlockData(remainingAmount = "£534.8", progress = 0.6f),
                    warrantyPlan = WarrantyPlanData(title = "U-Care Warranty", description = "3-year coverage.")
                )

                val finalNetworkResponse = com.manjeet.ubuyapplication.model.ProductDetailResponse(
                    error = false, code = "200", msg = "Success", data = realDetailData
                )

                Log.d("UBUY_VIEWMODEL", "✅ Base Initial Success state is ready for emission.")
                _uiState.value = ProductDetailUiState.Success(
                    responseWrapper = finalNetworkResponse,
                    sectionsState = defaultSections,
                    modifyDataResponse = parsedExtendedData,
                    reviewDataResponse = finalReviewData
                )
            } catch (e: Exception) {
                Log.e("UBUY_VIEWMODEL", "💥 Fatal Error in fetchProductDetailsFromList", e)
                _uiState.value = ProductDetailUiState.Error(e.message ?: "Failed to synchronize backend json framework")
            }
        }
    }

    // ─── 🎨 🔥 FIXED VARIATION OPTION EMITTER WITH FULL PROTECTION MATRIX 🔥 ───
    fun updateSelectedVariationOption(headingName: String, selectedOptionId: String) {
        Log.d("UBUY_VIEWMODEL", "📍 [TRIGGER] updateSelectedVariationOption() called with Heading: '$headingName', OptionId: '$selectedOptionId'")

        val currentState = _uiState.value
        if (currentState is ProductDetailUiState.Success) {
            val oldProductData = currentState.responseWrapper.data ?: return

            // 1. Create a deep copy object mapping with complete field coverage
            val newProductData = ProductDetailData().apply {
                this.productId = oldProductData.productId
                this.sku = oldProductData.sku
                this.ubuySku = oldProductData.ubuySku
                this.parentSku = oldProductData.parentSku
                this.slug = oldProductData.slug
                this.productTitle = oldProductData.productTitle
                this.price = oldProductData.price
                this.originalPrice = oldProductData.originalPrice
                this.brand = oldProductData.brand
                this.brandKey = oldProductData.brandKey
                this.productImage = oldProductData.productImage
                this.productImages = oldProductData.productImages
                this.categoryName = oldProductData.categoryName
                this.ubuyStore = oldProductData.ubuyStore
                this.ubuySubstore = oldProductData.ubuySubstore
                this.bestSeller = oldProductData.bestSeller
                this.isPrime = oldProductData.isPrime
                this.isInStock = oldProductData.isInStock
                this.productWeight = oldProductData.productWeight
                this.deliveryDays = oldProductData.deliveryDays
                this.deliveryText = oldProductData.deliveryText
                this.deliveryTextMsg = oldProductData.deliveryTextMsg
                this.customerRating = oldProductData.customerRating
                this.usdPrice = oldProductData.usdPrice
                this.usdOriginalPrice = oldProductData.usdOriginalPrice
                this.source = oldProductData.source
                this.cartMaxQty = oldProductData.cartMaxQty
                this.isRestricted = oldProductData.isRestricted
                this.productUrl = oldProductData.productUrl
                this.notifyShow = oldProductData.notifyShow
                this.isShowWarranty = oldProductData.isShowWarranty
                this.isShowVehicle = oldProductData.isShowVehicle
                this.isBundleProduct = oldProductData.isBundleProduct
                this.isActive = oldProductData.isActive
                this.features = oldProductData.features
                this.whatInTheBox = oldProductData.whatInTheBox
                this.localWhatInTheBox = oldProductData.localWhatInTheBox
                this.whatInTheBoxTitle = oldProductData.whatInTheBoxTitle
                this.importantInformationTitle = oldProductData.importantInformationTitle
                this.importantInfo = oldProductData.importantInfo
                this.bundleProducts = oldProductData.bundleProducts
                this.isDealActive = oldProductData.isDealActive
            }

            // 2. Generate clean structured options mapped perfectly against current selection parameters
            val updatedVariationsList = ArrayList<com.manjeet.ubuyapplication.model.VariationData>()

            oldProductData.variationData?.forEach { oldVar ->
                val newOptionsList = ArrayList<com.manjeet.ubuyapplication.model.VariationOption>()
                val isTargetHeading = oldVar.heading?.trim()?.lowercase() == headingName.trim().lowercase()

                oldVar.options?.forEach { oldOpt ->
                    val newOpt = com.manjeet.ubuyapplication.model.VariationOption().apply {
                        this.optionId = oldOpt.optionId
                        this.imgUrl = oldOpt.imgUrl
                        this.value = oldOpt.value
                        this.available = oldOpt.available
                        // Directly resolve dynamic state tree updates
                        this.selected = if (isTargetHeading) oldOpt.optionId == selectedOptionId else oldOpt.selected
                    }
                    newOptionsList.add(newOpt)
                }

                val newVarData = com.manjeet.ubuyapplication.model.VariationData().apply {
                    this.heading = oldVar.heading
                    this.options = newOptionsList
                }
                updatedVariationsList.add(newVarData)
            }

            // Bind updated modifications to deep copy state reference pointer
            newProductData.variationData = updatedVariationsList

            // 3. Sync Dynamic Product Image & Title based on selection trees
            val matchedVariation = updatedVariationsList.find { it.heading?.trim()?.lowercase() == headingName.trim().lowercase() }
            val matchedOption = matchedVariation?.options?.find { it.optionId == selectedOptionId }

            if (matchedOption != null) {
                newProductData.productTitle = "iPhone Air (${matchedOption.value ?: "Selected Product"})"

                // If color selection updates, safely transform the image sources
                if (headingName.contains("Color", ignoreCase = true)) {
                    val rawImgUrl = matchedOption.imgUrl
                    if (!rawImgUrl.isNullOrEmpty()) {
                        val dynamicHighResUrl = if (rawImgUrl.contains("._SS64_")) {
                            rawImgUrl.replace("._SS64_", "._AC_SX569_")
                        } else {
                            rawImgUrl
                        }
                        newProductData.productImage = dynamicHighResUrl
                        newProductData.productImages = arrayListOf(
                            ProductImages(
                                thumb = dynamicHighResUrl,
                                thumbList = dynamicHighResUrl,
                                full = dynamicHighResUrl,
                                zoom = dynamicHighResUrl
                            )
                        )
                    }
                } else {
                    // 🎯 CRITICAL FIX: Retain original images when size or other attributes change
                    newProductData.productImage = oldProductData.productImage
                    newProductData.productImages = oldProductData.productImages
                }
            }

            // 4. Wrap up and fire clean synchronized state payload to screen
            val newResponseWrapper = com.manjeet.ubuyapplication.model.ProductDetailResponse(
                error = currentState.responseWrapper.error,
                code = currentState.responseWrapper.code,
                msg = currentState.responseWrapper.msg,
                data = newProductData
            )

            _uiState.value = ProductDetailUiState.Success(
                responseWrapper = newResponseWrapper,
                sectionsState = currentState.sectionsState,
                modifyDataResponse = currentState.modifyDataResponse,
                reviewDataResponse = currentState.reviewDataResponse
            )
            Log.d("UBUY_VIEWMODEL", "📢 State tree synchronized completely. Variations and image properties perfectly linked.")
        }
    }
}